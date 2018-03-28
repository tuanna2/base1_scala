package models

import java.util.UUID

import com.mohiva.play.silhouette.api.{ Identity, LoginInfo }

case class User(
  userId: String,
  loginInfo: LoginInfo,
  userName: Option[String],
  department: Option[String],
  note: Option[String],
  isDeleted: Option[Int],
  createdBy: Option[Int],
  createdAt: java.sql.Date,
  updatedBy: Option[Int],
  updatedAt: java.sql.Date
) extends Identity
