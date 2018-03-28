package models.tables

import slick.driver.PostgresDriver.api._
import slick.lifted.ProvenShape

case class CodeGen(
  name: String,
  alias: String,
  currentCode: Int
)

class CodeGenTable(tag: Tag) extends Table[CodeGen](tag, "code_gen") {

  def name: Rep[String] = column[String]("name", O.PrimaryKey)

  def alias: Rep[String] = column[String]("alias")

  def currentCode: Rep[Int] = column[Int]("current_code")

  override def * : ProvenShape[CodeGen] = (name, alias, currentCode) <> (CodeGen.tupled, CodeGen.unapply)

}

