package forms.auth

import play.api.data.Form
import play.api.data.Forms._

object UserForm {

  val form = Form(
    mapping(
      "userId" -> nonEmptyText,
      "userName" -> text.verifying("user.name.required", { _.nonEmpty }),
      "department" -> text.verifying("user.department.required", { _.nonEmpty }),
      "note" -> text,
      "password" -> text.verifying("user.password.required", { _.nonEmpty })

    )(Data.apply)(Data.unapply)
  )

  case class Data(
    userId: String,
    userName: String,
    department: String,
    note: String,
    password: String
  )
}