package models.daos

import forms.auth.StudentSearchForm
import models.tables.Student
import scala.concurrent.Future

trait StudentDAO {

  def find(id: String): Future[Option[Student]]

  def find(page: Int, limit: Int = 5): Future[Seq[Student]]

  def find(formData: StudentSearchForm.Data): Future[Seq[Student]]

  def save(term: Student): Future[Int]

  def all(): Future[Seq[Student]]

  def edit(term: Student): Future[Int]

  def delete(code: String): Future[Int]

  def count(): Future[Int]
}