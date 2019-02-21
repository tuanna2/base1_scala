package forms.auth

import play.api.data.Form
import play.api.data.Forms._

object StudentSearchForm {
  val form = Form(
    mapping(
      "id" -> text,
      "name" -> text,
      "clas" -> text
    )(Data.apply)(Data.unapply)
  )

  case class Data(
    id: String,
    name: String,
    clas: String)
}
