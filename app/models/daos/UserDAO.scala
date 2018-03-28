package models.daos

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import forms.auth.UserSearchForm
import models.User
import models.tables.{ DbLoginInfo, DbUser, LoginInfoTable }
import slick.lifted.Query

import scala.concurrent.Future

/**
 * Give access to the user object.
 */
trait UserDAO {

  def loginInfoQuery(loginInfo: LoginInfo): Query[LoginInfoTable, DbLoginInfo, Seq]

  def find(loginInfo: LoginInfo): Future[Option[User]]

  def find(userID: String): Future[Option[User]]

  def find(): Future[Seq[DbUser]]

  def find(data: UserSearchForm.Data): Future[Seq[DbUser]]

  def save(user: User): Future[User]

  def edit(user: User): Future[User]

  def delete(userID: String): Future[Int]

  def all(): Future[Seq[DbUser]]
}
