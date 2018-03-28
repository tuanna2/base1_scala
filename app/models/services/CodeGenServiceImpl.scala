package models.services

import javax.inject.Inject

import models.daos.CodeGenDAO
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

class CodeGenServiceImpl @Inject() (codeGenDAO: CodeGenDAO) extends CodeGenService {

  override def generate(name: String): Future[String] = {
    codeGenDAO.find(name).map { code =>
      code.get.alias + generateNumber(code.get.currentCode)
    }
  }

  def generateNumber(number: Int): String = {
    val num = number + 1
    if (num < 10)
      "000".concat(num.toString)
    else if (num < 100)
      "00".concat(num.toString)
    else if (num < 1000)
      "0".concat(num.toString)
    else
      num.toString
  }

  override def update(name: String): Future[Int] = {
    for {
      code <- codeGenDAO.find(name)
      rs <- codeGenDAO.edit(code.get.copy(currentCode = code.get.currentCode + 1))
    } yield rs
  }
}
