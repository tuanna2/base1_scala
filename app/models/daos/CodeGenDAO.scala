package models.daos

import models.tables.{ CodeGen }

import scala.concurrent.Future

/**
 * Give access to the user object.
 */
trait CodeGenDAO {

  def find(name: String): Future[Option[CodeGen]]

  def edit(code: CodeGen): Future[Int]
}
