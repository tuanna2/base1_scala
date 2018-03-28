package models.daos

import java.util.UUID
import javax.inject.Inject

import com.mohiva.play.silhouette.api.LoginInfo
import forms.auth.UserSearchForm
import models.User
import models.daos.UserDAOImpl._
import models.tables._
import play.api.db.slick.DatabaseConfigProvider
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import slick.jdbc.JdbcBackend
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Give access to the user object.
 */
class UserDAOImpl @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends UserDAO {

  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db

  import dbConfig.driver.api._

  def loginInfoQuery(loginInfo: LoginInfo): Query[LoginInfoTable, DbLoginInfo, Seq] = {
    loginInfos.filter(dbLoginInfo => dbLoginInfo.providerId === loginInfo.providerID && dbLoginInfo.providerKey === loginInfo.providerKey)
  }

  def find(loginInfo: LoginInfo): Future[Option[User]] = {
    val userQuery = for {
      dbLoginInfo <- loginInfoQuery(loginInfo)
      dbUserLoginInfo <- userLoginInfos.filter(_.loginInfoId === dbLoginInfo.id)
      dbUser <- users.filter(_.userId === dbUserLoginInfo.userId)
    } yield dbUser

    db.run(userQuery.result.headOption).map { dbUserOption =>
      dbUserOption.map {
        user => User(user.userId, loginInfo, user.userName, user.department, user.note, user.isDeleted, user.createdBy, user.createdAt, user.updatedBy, user.updatedAt)
      }
    }
  }

  def find(userID: String): Future[Option[User]] = {
    {
      val userQuery = for {
        dbUser <- users.filter(_.userId === userID)
        dbUserLoginInfo <- userLoginInfos.filter(_.userId === dbUser.userId)
        dbLoginInfo <- loginInfos.filter(_.id === dbUserLoginInfo.loginInfoId)
      } yield (dbUser, dbLoginInfo)

      db.run(userQuery.result.headOption).map { resultOption =>
        resultOption.map {
          case (user, loginInfo) => User(
            user.userId,
            LoginInfo(loginInfo.providerId, loginInfo.providerKey),
            user.userName,
            user.department,
            user.note,
            user.isDeleted,
            user.createdBy,
            user.createdAt,
            user.updatedBy,
            user.updatedAt
          )
        }
      }
    }
  }

  def save(user: User): Future[User] = {
    val dbUser = DbUser(user.userId.toString, user.userName, user.department, user.note, user.isDeleted, user.createdBy, user.createdAt, user.updatedBy, user.updatedAt)
    val dbLoginInfo = DbLoginInfo(None, user.loginInfo.providerID, user.loginInfo.providerKey)

    val loginInfoAction = {
      val retrieveLoginInfo = loginInfos.filter(
        info => info.providerId === user.loginInfo.providerID &&
          info.providerKey === user.loginInfo.providerKey
      ).result.headOption

      val insertLoginInfo = loginInfos.returning(loginInfos.map(_.id)).
        into((info, id) => info.copy(id)) += dbLoginInfo

      for {
        loginInfoOption <- retrieveLoginInfo
        loginInfo <- loginInfoOption.map(DBIO.successful).getOrElse(insertLoginInfo)
      } yield loginInfo
    }

    val actions = (for {
      _ <- users.insertOrUpdate(dbUser)
      loginInfo <- loginInfoAction
      _ <- userLoginInfos += DbUserLoginInfo(dbUser.userId, loginInfo.id.get)
    } yield ()).transactionally

    db.run(actions).map(_ => user)
  }

  def all(): Future[Seq[DbUser]] = db.run(users.result)

  override def find() = db.run {
    users.filter(_.isDeleted === 0).result
  }

  override def edit(user: User): Future[User] = {
    val dbUser = DbUser(user.userId.toString, user.userName, user.department, user.note, user.isDeleted, user.createdBy, user.createdAt, user.updatedBy, user.updatedAt)
    db.run(users.insertOrUpdate(dbUser)).map(_ => user)
  }

  override def delete(userID: String) = db.run(users.filter(_.userId === userID).delete)

  override def find(formData: UserSearchForm.Data) = {
    if (formData.userName.nonEmpty && formData.note.nonEmpty) {
      val action = users.filter(data => data.userName.like("%" + formData.userName + "%") && data.note.like("%" + formData.note + "%")).result
      db.run(action)
    } else if (formData.userName.nonEmpty) {
      val action = users.filter(_.userName.like("%" + formData.userName + "%")).result
      db.run(action)
    } else {
      val action = users.filter(_.note.like("%" + formData.note + "%")).result
      db.run(action)
    }
  }
}

object UserDAOImpl {

  private val loginInfos = TableQuery[LoginInfoTable]
  private val users = TableQuery[UserTable]
  private var userLoginInfos = TableQuery[UserLoginInfoTable]

}
