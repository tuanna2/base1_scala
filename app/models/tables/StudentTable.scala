package models.tables

import slick.driver.PostgresDriver.api._
import slick.lifted.ProvenShape

case class Student(
  id: String,
  name: Option[String],
  age: Option[Int],
  clas: Option[String],
  createdBy: Option[String],
  createdAt: Option[java.sql.Date],
  updatedBy: Option[String],
  updatedAt: Option[java.sql.Date]
)

class StudentTable(tag: Tag) extends Table[Student](tag, "student") {
  def id: Rep[String] = column[String]("id", O.PrimaryKey)

  def name: Rep[Option[String]] = column[Option[String]]("name")

  def age: Rep[Option[Int]] = column[Option[Int]]("age")

  def clas: Rep[Option[String]] = column[Option[String]]("class")

  def createdBy: Rep[Option[String]] = column[Option[String]]("created_by")

  def createdAt: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("created_at")

  def updatedBy: Rep[Option[String]] = column[Option[String]]("updated_by")

  def updatedAt: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("updated_at")

  override def * : ProvenShape[Student] = (id, name, age, clas, createdBy, createdAt, updatedBy, updatedAt) <> (Student.tupled, Student.unapply)
}
