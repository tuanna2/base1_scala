package forms.auth

import play.api.data.Form
import play.api.data.Forms._

object TermForm {
  val form = Form(
    mapping(
      "termCode" -> text.verifying("term.code.required", { _.nonEmpty }),
      "termType" -> text.verifying("term.type.required", { _.nonEmpty }),
      "termName" -> text.verifying("term.name.required", { _.nonEmpty }),
      "note" -> text
    )(Data.apply)(Data.unapply)
  )

  case class Data(
    termCode: String,
    termType: String,
    termName: String,
    note: String)
}
