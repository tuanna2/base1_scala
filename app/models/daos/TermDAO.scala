package models.daos

import com.mohiva.play.silhouette.api.LoginInfo
import forms.auth.TermSearchForm
import models.tables.Term
import slick.lifted.Query

import scala.concurrent.Future

/**
 * Give access to the user object.
 */
trait TermDAO {

  def find(code: String): Future[Option[Term]]

  def find(page: Int = 1, limit: Int = 5): Future[Seq[Term]]

  def find(formData: TermSearchForm.Data): Future[Seq[Term]]

  def save(term: Term): Future[Int]

  def all(): Future[Seq[Term]]

  def edit(term: Term): Future[Int]

  def delete(code: String): Future[Int]

  def count(): Future[Int]
}
