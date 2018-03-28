package models.services

import java.util.UUID

import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.impl.providers.CommonSocialProfile
import forms.auth.UserSearchForm
import models.User
import models.tables.DbUser

import scala.concurrent.Future

/**
 * Handles actions to users.
 */
trait UserService extends IdentityService[User] {

  def retrieve(id: String): Future[Option[User]]

  def save(user: User): Future[User]

  def all(): Future[Seq[DbUser]]

  def find(): Future[Seq[DbUser]]

  def find(formData: UserSearchForm.Data): Future[Seq[DbUser]]

  def edit(user: User): Future[User]

  def delete(userId: String): Future[Int]
  //  def save(profile: CommonSocialProfile): Future[User]
}
