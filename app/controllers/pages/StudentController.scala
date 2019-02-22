package controllers.pages

import javax.inject.Inject

import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.impl.providers.SocialProviderRegistry
import controllers.{ WebJarAssets, auth }
import forms.auth.{ SignUpForm, StudentForm, StudentSearchForm }
import models.services.{ CodeGenService, StudentService, UserService }
import models.tables.Student
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.mvc.{ Action, AnyContent, Controller }
import utils.auth.DefaultEnv

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class StudentController @Inject() (
  val messagesApi: MessagesApi,
  val userService: UserService,
  codeGenService: CodeGenService,
  val studentService: StudentService,
  silhouette: Silhouette[DefaultEnv],
  socialProviderRegistry: SocialProviderRegistry,
  implicit val webJarAssets: WebJarAssets)
  extends Controller with I18nSupport {

  def index: Action[AnyContent] = silhouette.SecuredAction.async { implicit request =>
    val page = request.getQueryString("page").getOrElse("1").toInt
    for {
      students <- studentService.all
    } yield {
      Ok(views.html.student.index(request.identity, students, StudentSearchForm.form))
    }
  }

  def pagin(offset: Int): Action[AnyContent] = silhouette.SecuredAction.async { implicit request =>
    for {
      data <- studentService.find(offset)
      length <- studentService.count
    } yield {
      Ok(views.html.student.pagin(request.identity, length, data, StudentSearchForm.form))
    }
  }

  def search: Action[AnyContent] = silhouette.SecuredAction.async { implicit request =>

    StudentSearchForm.form.bindFromRequest.fold(
      form => Future.successful(Ok),
      data => {
        for {
          students <- studentService.find(data)
        } yield Ok(views.html.student.index(request.identity, students, StudentSearchForm.form.bindFromRequest))
      }
    )
  }

  def delete(id: String): Action[AnyContent] = silhouette.SecuredAction.async { implicit request =>
    studentService.delete(id).map {
      rs =>
        Ok("{code: 200}")
    }
  }

  def edit(id: String): Action[AnyContent] = silhouette.SecuredAction.async { implicit request =>
    StudentForm.form.bindFromRequest.fold(
      form => {
        println("Student ERR " + form)
        for {
          student <- studentService.retrieve(id)
        } yield BadRequest(views.html.student.edit(form, Some(student.get)))

        //        Future.successful(BadRequest(views.html.term.edit(form)))
      },
      data => {
        println("Student" + data)
        studentService.retrieve(data.id).flatMap {
          case Some(mstudent) =>
            val newstudent = mstudent.copy(name = Some(data.name), age = Some(data.age.toInt), clas = Some(data.clas))
            studentService.edit(newstudent)
            val result = Redirect(routes.StudentController.editView(data.id)).flashing("info" -> "Success")
            Future.successful(result)
          case None =>
            val r = Redirect(routes.StudentController.editView(data.id)).flashing("error" -> "Error。")
            Future.successful(r)
        }
      }
    )
  }

  def add: Action[AnyContent] = silhouette.SecuredAction.async { implicit request =>
    StudentForm.form.bindFromRequest.fold(
      form => {
        Future.successful(BadRequest(views.html.student.add(form)).flashing("error" -> "登録がd出来ませんでした。"))
      },
      data => {
        for {
          code <- codeGenService.generate("term_job")
          student = Student(
            id = code,
            name = Some(data.name),
            age = Some(data.age.toInt),
            clas = Some(data.clas),
            createdBy = Some(request.identity.userId),
            createdAt = Some(new java.sql.Date(System.currentTimeMillis())),
            updatedBy = Some(request.identity.userId),
            updatedAt = Some(new java.sql.Date(System.currentTimeMillis()))
          )
          rs <- studentService.save(student)
          rs1 <- codeGenService.update("term_job")
        } yield {
          Redirect(routes.StudentController.addView()).flashing("info" -> "登録が成功しました。")
        }
      }
    )
  }

  /*
    ------------------------------------------------------------------------------------------------
    CREATE VIEW
    ------------------------------------------------------------------------------------------------
    */

  def editView(id: String): Action[AnyContent] = silhouette.SecuredAction.async { implicit request =>
    for {
      student <- studentService.retrieve(id)
      data = Map(
        "id" -> student.get.id,
        "name" -> student.get.name.getOrElse(""),
        "age" -> (if (student.get.age.nonEmpty) student.get.age.get.toString else ""),
        "clas" -> student.get.clas.getOrElse("")
      )
      form = StudentForm.form.bind(data)
    } yield Ok(views.html.student.edit(form, Some(student.get)))
  }

  def addView: Action[AnyContent] = silhouette.SecuredAction.async { implicit request =>
    Future.successful(Ok(views.html.student.add(StudentForm.form)))
  }
}
