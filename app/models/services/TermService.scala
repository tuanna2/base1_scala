package models.services

import forms.auth.TermSearchForm
import models.tables.{ Term }

import scala.concurrent.Future

/**
 * Handles actions to users.
 */
trait TermService {

  def retrieve(code: String): Future[Option[Term]]

  def find(page: Int = 1): Future[Seq[Term]]

  def find(formData: TermSearchForm.Data): Future[Seq[Term]]

  def save(term: Term): Future[Int]

  def all(): Future[Seq[Term]]

  def edit(term: Term): Future[Int]

  def delete(code: String): Future[Int]

  def count(): Future[Int]
}
