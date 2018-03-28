package controllers.auth

import java.util.UUID
import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.services.AvatarService
import com.mohiva.play.silhouette.api.util.PasswordHasherRegistry
import com.mohiva.play.silhouette.impl.providers._
import controllers.{ WebJarAssets, auth }
import forms.auth.SignUpForm
import models.User
import models.services.{ AuthTokenService, CodeGenService, UserService }
import play.api.Logger
import play.api.i18n.{ I18nSupport, Messages, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.mailer.{ Email, MailerClient }
import play.api.mvc.{ Action, AnyContent, Controller }
import utils.auth.DefaultEnv

import scala.concurrent.Future

/**
 * The `Sign Up` controller.
 *
 * @param messagesApi            The Play messages API.
 * @param silhouette             The Silhouette stack.
 * @param userService            The user service implementation.
 * @param authInfoRepository     The auth info repository implementation.
 * @param authTokenService       The auth token service implementation.
 * @param avatarService          The avatar service implementation.
 * @param passwordHasherRegistry The password hasher registry.
 * @param mailerClient           The mailer client.
 * @param webJarAssets           The webjar assets implementation.
 */
class SignUpController @Inject() (
  val messagesApi: MessagesApi,
  silhouette: Silhouette[DefaultEnv],
  userService: UserService,
  authInfoRepository: AuthInfoRepository,
  authTokenService: AuthTokenService,
  avatarService: AvatarService,
  codeGenService: CodeGenService,
  passwordHasherRegistry: PasswordHasherRegistry,
  mailerClient: MailerClient,
  implicit val webJarAssets: WebJarAssets)
  extends Controller with I18nSupport {

  /**
   * Views the `Sign Up` page.
   *
   * @return The result to display.
   */
  def view: Action[AnyContent] = silhouette.UnsecuredAction.async { implicit request =>
    Future.successful(Ok(views.html.auth.signUp(SignUpForm.form)))
  }

  /**
   * Handles the submitted form.
   *
   * @return The result to display.
   */
  def submit: Action[AnyContent] = silhouette.UnsecuredAction.async { implicit request =>
    SignUpForm.form.bindFromRequest.fold(
      form => Future.successful(BadRequest(views.html.auth.signUp(form))),
      data => {
        val result = Redirect(auth.routes.SignUpController.view()).flashing("info" -> Messages("sign.up.email.sent", data.userName))
        val loginInfo = LoginInfo(CredentialsProvider.ID, data.userName)
        userService.retrieve(loginInfo).flatMap {
          case Some(user) =>
            val url = auth.routes.SignInController.view().absoluteURL()
            Logger.debug(s"DANG KY=$url")
            //            mailerClient.send(Email(
            //              subject = Messages("email.already.signed.up.subject"),
            //              from = Messages("email.from"),
            //              to = Seq(data.email),
            //              bodyText = Some(views.txt.emails.alreadySignedUp(user, url).body),
            //              bodyHtml = Some(views.html.emails.alreadySignedUp(user, url).body)
            //            ))

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
                isDeleted = None,
                createdBy = None,
                createdAt = new java.sql.Date(System.currentTimeMillis()),
                updatedBy = None,
                updatedAt = new java.sql.Date(System.currentTimeMillis())
              )
              user <- userService.save(user.copy())
              authInfo <- authInfoRepository.add(loginInfo, authInfo)
              authToken <- authTokenService.create(user.userId)
            } yield {
              //              val url = auth.routes.ActivateAccountController.activate(authToken.id).absoluteURL()
              //              Logger.debug(s"DANG KY=$url")
              //              mailerClient.send(Email(
              //                subject = Messages("email.sign.up.subject"),
              //                from = Messages("email.from"),
              //                to = Seq(data.email),
              //                bodyText = Some(views.txt.emails.signUp(user, url).body),
              //                bodyHtml = Some(views.html.emails.signUp(user, url).body)
              //              ))

              silhouette.env.eventBus.publish(SignUpEvent(user, request))
              result
            }
        }
      }
    )
  }
}
