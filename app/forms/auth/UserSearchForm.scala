package forms.auth

import play.api.data.Form
import play.api.data.Forms._

object UserSearchForm {

  val form = Form(
    mapping(
      "userName" -> text,
      "note" -> text
    )(Data.apply)(Data.unapply)
  )

  case class Data(
    userName: String,
    note: String
  )
}