package models.services

import java.util.UUID
import javax.inject.Inject

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.providers.CommonSocialProfile
import forms.auth.UserSearchForm
import models.User
import models.daos.UserDAO
import models.tables.DbUser
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.Future

/**
 * Handles actions to users.
 *
 * @param userDAO The user DAO implementation.
 */
class UserServiceImpl @Inject() (userDAO: UserDAO) extends UserService {

  override def retrieve(id: String) = userDAO.find(id)

  def retrieve(loginInfo: LoginInfo): Future[Option[User]] = userDAO.find(loginInfo)

  override def save(user: User) = userDAO.save(user)

  override def all(): Future[Seq[DbUser]] = userDAO.all()

  override def find() = userDAO.find()

  override def find(formData: UserSearchForm.Data): Future[Seq[DbUser]] = userDAO.find(formData)

  override def edit(user: User) = userDAO.edit(user)

  override def delete(userId: String) = userDAO.delete(userId)

  //  def save(profile: CommonSocialProfile) = {
  //    userDAO.find(profile.loginInfo).flatMap {
  //      case Some(user) => // Update user with profile
  //        userDAO.save(user.copy(
  //          userName = profile.,
  //          lastName = profile.lastName,
  //          fullName = profile.fullName,
  //          email = profile.email,
  //          avatarURL = profile.avatarURL
  //        ))
  //      case None => // Insert a new user
  //        userDAO.save(User(
  //          userID = UUID.randomUUID(),
  //          loginInfo = profile.loginInfo,
  //          firstName = profile.firstName,
  //          lastName = profile.lastName,
  //          fullName = profile.fullName,
  //          email = profile.email,
  //          avatarURL = profile.avatarURL,
  //          activated = true
  //        ))
  //    }
  //  }
}
