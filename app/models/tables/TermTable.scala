package models.tables

import slick.driver.PostgresDriver.api._
import slick.lifted.ProvenShape

case class Term(
  code: String,
  termType: Option[String],
  name: Option[String],
  note: Option[String],
  isDeleted: Option[Int],
  createdBy: Option[String],
  createdAt: Option[java.sql.Date],
  updatedBy: Option[String],
  updatedAt: Option[java.sql.Date]
)

class TermTable(tag: Tag) extends Table[Term](tag, "term") {

  def code: Rep[String] = column[String]("term_code", O.PrimaryKey)

  def termType: Rep[Option[String]] = column[Option[String]]("term_type")

  def name: Rep[Option[String]] = column[Option[String]]("term_name")

  def note: Rep[Option[String]] = column[Option[String]]("note")

  def isDeleted: Rep[Option[Int]] = column[Option[Int]]("is_deleted")

  def createdBy: Rep[Option[String]] = column[Option[String]]("created_by")

  def createdAt: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("created_at")

  def updatedBy: Rep[Option[String]] = column[Option[String]]("updated_by")

  def updatedAt: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("updated_at")

  override def * : ProvenShape[Term] = (code, termType, name, note, isDeleted, createdBy, createdAt, updatedBy, updatedAt) <> (Term.tupled, Term.unapply)

}

