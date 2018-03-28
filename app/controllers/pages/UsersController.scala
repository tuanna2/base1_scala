package controllers.pages

import java.util.UUID
import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.services.AvatarService
import com.mohiva.play.silhouette.api.util.PasswordHasherRegistry
import com.mohiva.play.silhouette.impl.providers._
import controllers.{ WebJarAssets, auth }
import forms.auth.{ SignUpForm, TermForm, UserForm, UserSearchForm }
import models.User
import models.services.{ AuthTokenService, CodeGenService, TermService, UserService }
import models.tables.Term
import play.api.Logger
import play.api.i18n.{ I18nSupport, Messages, MessagesApi }
import play.api.mvc.{ Action, AnyContent, Controller }
import utils.auth.DefaultEnv

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UsersController @Inject() (
  val messagesApi: MessagesApi,
  val userService: UserService,
  val termService: TermService,
  silhouette: Silhouette[DefaultEnv],
  socialProviderRegistry: SocialProviderRegistry,
  authInfoRepository: AuthInfoRepository,
  authTokenService: AuthTokenService,
  codeGenService: CodeGenService,
  passwordHasherRegistry: PasswordHasherRegistry,
  implicit val webJarAssets: WebJarAssets)
  extends Controller with I18nSupport {

  def index: Action[AnyContent] = silhouette.SecuredAction.async { implicit request =>
    for {
      users <- userService.find
    } yield Ok(views.html.users.index(request.identity, users, UserSearchForm.form))

  }

  def find: Action[AnyContent] = silhouette.SecuredAction.async { implicit request =>
    for {
      users <- userService.find
    } yield Ok(views.html.users.index(request.identity, users, UserSearchForm.form))
  }

  def delete(id: String): Action[AnyContent] = silhouette.SecuredAction.async { implicit request =>
    userService.delete(id).map {
      rs =>
        Ok("{code: 200}")
    }
  }

  def search: Action[AnyContent] = silhouette.SecuredAction.async { implicit request =>

    UserSearchForm.form.bindFromRequest.fold(
      form => Future.successful(Ok),
      data => {
        for {
          users <- userService.find(data)
        } yield Ok(views.html.users.index(request.identity, users, UserSearchForm.form.bindFromRequest))
      }
    )
  }

  def edit(id: String): Action[AnyContent] = silhouette.SecuredAction.async { implicit request =>
    UserForm.form.bindFromRequest.fold(
      form => {
        for {
          user <- userService.retrieve(id)
        } yield BadRequest(views.html.users.edit(form, Some(user.get))).flashing("error" -> "ユーザが変更出来ません。")
        //        Future.successful(BadRequest(views.html.users.edit(form)))
      },
      data => {
        println(data)
        val result = Redirect(routes.UsersController.editView(data.userId)).flashing("info" -> "変更が成功しました。")
        val loginInfo = LoginInfo(CredentialsProvider.ID, data.userName)
        println(loginInfo)
        userService.retrieve(loginInfo).flatMap {
          case Some(user) =>
            printf("vao some")
            val authInfo = passwordHasherRegistry.current.hash(data.password)

            val newuser = user.copy(department = Some(data.department), note = Some(data.note)) //, isDeleted = Some(data.isDeleted.toInt))
            for {
              user <- userService.save(newuser.copy())
              authInfo <- authInfoRepository.add(loginInfo, authInfo)
              //              authToken <- authTokenService.create(user.userId)
            } yield {
              silhouette.env.eventBus.publish(SignUpEvent(newuser, request))
              result
            }
          case None =>
            val r2 = Redirect(routes.UsersController.editView(data.userId)).flashing("error" -> "ユーザ名が変更出来ません。")
            Future.successful(r2)
        }
      }
    )
  }

  def add: Action[AnyContent] = silhouette.SecuredAction.async { implicit request =>
    UserForm.form.bindFromRequest.fold(
      form => Future.successful(BadRequest(views.html.users.add(form))),
      data => {
        val result = Redirect(routes.UsersController.addView()).flashing("info" -> "登録が成功しました。")
        val loginInfo = LoginInfo(CredentialsProvider.ID, data.userName)
        userService.retrieve(loginInfo).flatMap {
          case Some(user) =>
            Future.successful(result)
          case None =>
            val authInfo = passwordHasherRegistry.current.hash(data.password)

            for {
              code <- codeGenService.generate("user")
              user = User(
                userId = code,
                loginInfo = loginInfo,
                userName = Some(data.userName),
                department = Some(data.department),
                note = Some(data.note),
                isDeleted = Some(0),
                createdBy = None,
                createdAt = new java.sql.Date(System.currentTimeMillis()),
                updatedBy = None,
                updatedAt = new java.sql.Date(System.currentTimeMillis())
              )
              user <- userService.save(user.copy())
              authInfo <- authInfoRepository.add(loginInfo, authInfo)
              //              authToken <- authTokenService.create(user.userId)
              rs1 <- codeGenService.update("user")
            } yield {
              silhouette.env.eventBus.publish(SignUpEvent(user, request))
              result
            }
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
      user <- userService.retrieve(id)

      data = Map(
        "userId" -> user.get.userId.toString,
        "userName" -> user.get.userName.getOrElse(""),
        "department" -> user.get.department.getOrElse(""),
        "password" -> "xxxxxxx",
        "note" -> user.get.note.getOrElse("")
      //        "isDeleted" -> user.get.isDeleted.toString
      )
      form = UserForm.form.bind(data)
    } yield Ok(views.html.users.edit(form, Some(user.get)))
  }

  def addView: Action[AnyContent] = silhouette.SecuredAction.async { implicit request =>
    Future.successful(Ok(views.html.users.add(UserForm.form)))
  }
}
