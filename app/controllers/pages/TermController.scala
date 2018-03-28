package controllers.pages

import javax.inject.Inject

import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.impl.providers.SocialProviderRegistry
import controllers.{ WebJarAssets, auth }
import forms.auth.{ SignUpForm, TermForm, TermSearchForm }
import models.services.{ CodeGenService, TermService, UserService }
import models.tables.Term
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.mvc.{ Action, AnyContent, Controller }
import utils.auth.DefaultEnv

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TermController @Inject() (
  val messagesApi: MessagesApi,
  val userService: UserService,
  codeGenService: CodeGenService,
  val termService: TermService,
  silhouette: Silhouette[DefaultEnv],
  socialProviderRegistry: SocialProviderRegistry,
  implicit val webJarAssets: WebJarAssets)
  extends Controller with I18nSupport {

  def index: Action[AnyContent] = silhouette.SecuredAction.async { implicit request =>
    val page = request.getQueryString("page").getOrElse("1").toInt
    for {
      terms <- termService.find(page)
    } yield {
      Ok(views.html.term.index(request.identity, terms, TermSearchForm.form))
    }
  }

  def find: Action[AnyContent] = silhouette.SecuredAction.async { implicit request =>
    val page = request.getQueryString("page").getOrElse("1").toInt
    for {
      terms <- termService.find(page)
    } yield Ok(views.html.term.index(request.identity, terms, TermSearchForm.form))
  }

  def search: Action[AnyContent] = silhouette.SecuredAction.async { implicit request =>

    TermSearchForm.form.bindFromRequest.fold(
      form => Future.successful(Ok),
      data => {
        for {
          terms <- termService.find(data)
        } yield Ok(views.html.term.index(request.identity, terms, TermSearchForm.form.bindFromRequest))
      }
    )
  }

  def delete(code: String): Action[AnyContent] = silhouette.SecuredAction.async { implicit request =>
    termService.delete(code).map {
      rs =>
        Ok("{code: 200}")
    }
  }

  def softDelete(code: String): Action[AnyContent] = silhouette.SecuredAction.async { implicit request =>
    for {
      term <- termService.retrieve(code)
    } yield {
      val newTerm = term.get.copy(isDeleted = Some(1))
      termService.edit(newTerm)
      Ok(views.html.term.add(TermForm.form))
    }
  }

  def edit(code: String): Action[AnyContent] = silhouette.SecuredAction.async { implicit request =>
    TermForm.form.bindFromRequest.fold(
      form => {
        println("TERM ERR " + form)
        for {
          term <- termService.retrieve(code)
        } yield BadRequest(views.html.term.edit(form, Some(term.get)))

        //        Future.successful(BadRequest(views.html.term.edit(form)))
      },
      data => {
        println("TERM" + data)
        termService.retrieve(data.termCode).flatMap {
          case Some(mterm) =>
            val newterm = mterm.copy(name = Some(data.termName), termType = Some(data.termType), note = Some(data.note))
            termService.edit(newterm)
            val result = Redirect(routes.TermController.editView(data.termCode)).flashing("info" -> "変更が成功しました。")
            Future.successful(result)
          case None =>
            val r = Redirect(routes.TermController.editView(data.termCode)).flashing("error" -> "変更が成功しました。")
            Future.successful(r)

        }
      }
    )
  }

  def add: Action[AnyContent] = silhouette.SecuredAction.async { implicit request =>
    TermForm.form.bindFromRequest.fold(
      form => {
        Future.successful(BadRequest(views.html.term.add(form)).flashing("error" -> "登録がd出来ませんでした。"))
      },
      data => {
        for {
          code <- codeGenService.generate("term_job")
          term = Term(
            code = code,
            termType = Some(data.termType),
            name = Some(data.termName),
            note = Some(data.note),
            isDeleted = Some(0),
            createdBy = Some(request.identity.userId),
            createdAt = Some(new java.sql.Date(System.currentTimeMillis())),
            updatedBy = Some(request.identity.userId),
            updatedAt = Some(new java.sql.Date(System.currentTimeMillis()))
          )
          rs <- termService.save(term)
          rs1 <- codeGenService.update("term_job")
        } yield {
          Redirect(routes.TermController.addView()).flashing("info" -> "登録が成功しました。")
        }
      }
    )
  }

  /*
  ------------------------------------------------------------------------------------------------
  CREATE VIEW
  ------------------------------------------------------------------------------------------------
  */

  def editView(code: String): Action[AnyContent] = silhouette.SecuredAction.async { implicit request =>
    for {
      term <- termService.retrieve(code)
      data = Map(
        "termCode" -> term.get.code,
        "termName" -> term.get.name.getOrElse(""),
        "termType" -> term.get.termType.getOrElse(""),
        "note" -> term.get.note.getOrElse("")
      )
      form = TermForm.form.bind(data)
    } yield Ok(views.html.term.edit(form, Some(term.get)))
  }

  def addView: Action[AnyContent] = silhouette.SecuredAction.async { implicit request =>
    Future.successful(Ok(views.html.term.add(TermForm.form)))
  }
}
