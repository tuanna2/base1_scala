package models.daos

import javax.inject.Inject

import forms.auth.TermSearchForm
import models.daos.TermDAOImpl._
import models.tables._
import play.api.db.slick.DatabaseConfigProvider
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import slick.jdbc.JdbcBackend
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TermDAOImpl @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends TermDAO {

  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db

  import dbConfig.driver.api._

  override def find(code: String): Future[Option[Term]] = db.run {
    terms.filter(_.code === code).result.headOption
  }

  override def find(page: Int = 1, limit: Int = 5): Future[Seq[Term]] = db.run {
    terms.filter(_.isDeleted === 0).drop((page - 1) * limit).take(limit).result
  }

  override def find(formData: TermSearchForm.Data): Future[Seq[Term]] = {
    if (formData.termCode.nonEmpty && formData.termType.nonEmpty && formData.note.nonEmpty) {
      val action = terms.filter(data => data.code.like("%" + formData.termCode + "%") && data.termType.like("%" + formData.termType + "%") && data.note.like("%" + formData.note + "%")).result
      db.run(action)
    } else if (formData.termCode.nonEmpty && formData.termType.nonEmpty) {
      val action = terms.filter(data => data.code.like("%" + formData.termCode + "%") && data.termType.like("%" + formData.termType + "%")).result
      db.run(action)
    } else if (formData.termCode.nonEmpty && formData.note.nonEmpty) {
      val action = terms.filter(data => data.code.like("%" + formData.termCode + "%") && data.note.like("%" + formData.note + "%")).result
      db.run(action)
    } else if (formData.termType.nonEmpty && formData.note.nonEmpty) {
      val action = terms.filter(data => data.termType.like("%" + formData.termType + "%") && data.note.like("%" + formData.note + "%")).result
      db.run(action)
    } else if (formData.termCode.nonEmpty) {
      val action = terms.filter(_.code.like("%" + formData.termCode + "%")).result
      db.run(action)
    } else if (formData.termType.nonEmpty) {
      val action = terms.filter(_.termType.like("%" + formData.termType + "%")).result
      db.run(action)
    } else {
      val action = terms.filter(_.note.like("%" + formData.note + "%")).result
      db.run(action)
    }
  }

  override def save(term: Term): Future[Int] = db.run(terms.insertOrUpdate(term))

  override def all(): Future[Seq[Term]] = db.run(terms.result)

  override def edit(term: Term): Future[Int] = db.run(terms.insertOrUpdate(term))

  override def delete(code: String): Future[Int] = db.run(terms.filter(_.code === code).delete)

  override def count(): Future[Int] = db.run {
    terms.length.result
  }
}

/**
 * The companion object.
 */
object TermDAOImpl {

  private val terms = TableQuery[TermTable]

}
