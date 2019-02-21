package models.daos

import javax.inject.Inject

import forms.auth.StudentSearchForm
import models.daos.StudentDAOImpl._
import models.tables._
import play.api.db.slick.DatabaseConfigProvider
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import slick.jdbc.JdbcBackend
import slick.lifted.TableQuery
import scala.concurrent.Future

class StudentDAOImpl @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends StudentDAO {

  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db

  import dbConfig.driver.api._

  override def find(id: String): Future[Option[Student]] = db.run {
    students.filter(_.id === id).result.headOption
  }

  override def find(page: Int = 1, limit: Int = 5): Future[Seq[Student]] = db.run {
    students.result
  }

  override def find(formData: StudentSearchForm.Data): Future[Seq[Student]] = {
    db.run(students.filter(data => data.id.like("%" + formData.id + "%") && data.name.like("%" + formData.name + "%") && data.clas.like("%" + formData.clas + "%")).result)
  }

  override def save(student: Student): Future[Int] = db.run(students.insertOrUpdate(student))

  override def all(): Future[Seq[Student]] = db.run(students.result)

  override def edit(student: Student): Future[Int] = db.run(students.insertOrUpdate(student))

  override def delete(id: String): Future[Int] = db.run(students.filter(_.id === id).delete)

  override def count(): Future[Int] = db.run {
    students.length.result
  }
}

/**
 * The companion object.
 */
object StudentDAOImpl {

  private val students = TableQuery[StudentTable]

}
