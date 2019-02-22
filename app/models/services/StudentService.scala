package models.services

import forms.auth.StudentSearchForm
import models.tables.{ Student }

import scala.concurrent.Future

/**
 * Handles actions to users.
 */
trait StudentService {

  def retrieve(id: String): Future[Option[Student]]

  def find(page: Int): Future[Seq[Student]]

  def save(student: Student): Future[Int]

  def all(): Future[Seq[Student]]

  def edit(student: Student): Future[Int]

  def delete(id: String): Future[Int]

  def find(formData: StudentSearchForm.Data): Future[Seq[Student]]

  def count(): Future[Int]
}
