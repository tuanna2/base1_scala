package forms.auth

import play.api.data.Form
import play.api.data.Forms._

object TermSearchForm {
  val form = Form(
    mapping(
      "termCode" -> text,
      "termType" -> text,
      "note" -> text
    )(Data.apply)(Data.unapply)
  )

  case class Data(
    termCode: String,
    termType: String,
    note: String)
}
