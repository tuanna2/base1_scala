package forms.auth

import play.api.data.Form
import play.api.data.Forms._

/**
 * The form which handles the sign up process.
 */
object SignUpForm {

  /**
   * A play framework form.
   */
  val form = Form(
    mapping(
      "userName" -> nonEmptyText,
      "department" -> nonEmptyText,
      "note" -> nonEmptyText,
      "password" -> nonEmptyText
    )(Data.apply)(Data.unapply)
  )

  /**
   * The form data.
   *
   * @param userName The first name of a user.
   * @param department The last name of a user.
   * @param note The email of the user.
   * @param password The password of the user.
   */
  case class Data(
    userName: String,
    department: String,
    note: String,
    password: String)
}
