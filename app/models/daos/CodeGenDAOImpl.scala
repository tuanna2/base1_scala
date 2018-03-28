package models.daos

import javax.inject.Inject

import models.daos.CodeGenDAOImpl.codeGen
import models.tables.{ CodeGen, CodeGenTable }
import play.api.db.slick.DatabaseConfigProvider
import slick.backend.DatabaseConfig
import slick.driver.{ JdbcProfile, PostgresDriver }
import slick.jdbc.JdbcBackend

import scala.concurrent.ExecutionContext.Implicits.global
import slick.lifted.{ Rep, TableQuery }

import scala.concurrent.Future

class CodeGenDAOImpl @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends CodeGenDAO {

  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db

  import dbConfig.driver.api._

  override def find(name: String): Future[Option[CodeGen]] = db.run {
    codeGen.filter(_.name === name).result.headOption
  }

  override def edit(code: CodeGen): Future[Int] = db.run(codeGen.insertOrUpdate(code))
}

/**
 * The companion object.
 */
object CodeGenDAOImpl {

  private val codeGen = TableQuery[CodeGenTable]

}

