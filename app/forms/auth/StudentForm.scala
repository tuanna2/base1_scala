package forms.auth

import play.api.data.Form
import play.api.data.Forms._

object StudentForm {
  val form = Form(
    mapping(
      "id" -> text.verifying("student.id.required", { _.nonEmpty }),
      "name" -> text.verifying("student.name.required", { _.nonEmpty }),
      "age" -> text.verifying("student.age.required", { _.nonEmpty }),
      "clas" -> text.verifying("student.clas.required", { _.nonEmpty })
    )(Data.apply)(Data.unapply)
  )

  case class Data(
    id: String,
    name: String,
    age: String,
    clas: String)
}
