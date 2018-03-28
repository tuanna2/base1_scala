package models.services

import models.tables.{ CodeGen }

import scala.concurrent.Future

/**
 * Handles actions to users.
 */
trait CodeGenService {

  def generate(name: String): Future[String]

  def update(name: String): Future[Int]

}
