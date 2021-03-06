package models.tables

import slick.driver.PostgresDriver.api._
import slick.lifted.ProvenShape

case class DbUser(
  userId: String,
  userName: Option[String],
  department: Option[String],
  note: Option[String],
  isDeleted: Option[Int],
  createdBy: Option[Int],
  createdAt: java.sql.Date,
  updatedBy: Option[Int],
  updatedAt: java.sql.Date
)

case class DbLoginInfo(
  id: Option[Long],
  providerId: String,
  providerKey: String
)

case class DbUserLoginInfo(
  userId: String,
  loginInfoId: Long
)

case class DbPasswordInfo(
  hasher: String,
  password: String,
  salt: Option[String],
  loginInfoId: Long
)

case class DbOAuth1Info(
  id: Option[Long],
  token: String,
  secret: String,
  loginInfoId: Long
)

case class DbOAuth2Info(
  id: Option[Long],
  accessToken: String,
  tokenType: Option[String],
  expiresIn: Option[Int],
  refreshToken: Option[String],
  loginInfoId: Long
)

case class DbOpenIDInfo(
  id: String,
  loginInfoId: Long
)

case class DbOpenIDAttribute(
  id: String,
  key: String,
  value: String
)

class UserTable(tag: Tag) extends Table[DbUser](tag, "users") {

  def userId: Rep[String] = column[String]("user_id", O.PrimaryKey)

  def userName: Rep[Option[String]] = column[Option[String]]("user_name")

  def department: Rep[Option[String]] = column[Option[String]]("department")

  def note: Rep[Option[String]] = column[Option[String]]("note")

  def isDeleted: Rep[Option[Int]] = column[Option[Int]]("is_deleted")

  def createdBy: Rep[Option[Int]] = column[Option[Int]]("created_by")

  def createdAt: Rep[java.sql.Date] = column[java.sql.Date]("created_at")

  def updatedBy: Rep[Option[Int]] = column[Option[Int]]("updated_by")

  def updatedAt: Rep[java.sql.Date] = column[java.sql.Date]("updated_at")

  override def * : ProvenShape[DbUser] = (userId, userName, department, note, isDeleted, createdBy, createdAt, updatedBy, updatedAt) <> (DbUser.tupled, DbUser.unapply)

}

class LoginInfoTable(tag: Tag) extends Table[DbLoginInfo](tag, "login_info") {

  def id: Rep[Option[Long]] = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)

  def providerId: Rep[String] = column[String]("provider_id")

  def providerKey: Rep[String] = column[String]("provider_key")

  override def * : ProvenShape[DbLoginInfo] = (id, providerId, providerKey) <> (DbLoginInfo.tupled, DbLoginInfo.unapply)

}

class UserLoginInfoTable(tag: Tag) extends Table[DbUserLoginInfo](tag, "user_login_info") {

  def userId: Rep[String] = column[String]("user_id")

  def loginInfoId: Rep[Long] = column[Long]("login_info_id")

  def * : ProvenShape[DbUserLoginInfo] = (userId, loginInfoId) <> (DbUserLoginInfo.tupled, DbUserLoginInfo.unapply)

}

class PasswordInfoTable(tag: Tag) extends Table[DbPasswordInfo](tag, "password_info") {

  def hasher: Rep[String] = column[String]("hasher")

  def password: Rep[String] = column[String]("password")

  def salt: Rep[Option[String]] = column[Option[String]]("salt")

  def loginInfoId: Rep[Long] = column[Long]("login_info_id")

  def * : ProvenShape[DbPasswordInfo] = (hasher, password, salt, loginInfoId) <> (DbPasswordInfo.tupled, DbPasswordInfo.unapply)

}

class OAuth1InfoTable(tag: Tag) extends Table[DbOAuth1Info](tag, "oauth1_info") {

  def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def token: Rep[String] = column[String]("token")

  def secret: Rep[String] = column[String]("secret")

  def loginInfoId: Rep[Long] = column[Long]("login_info_id")

  def * : ProvenShape[DbOAuth1Info] = (id.?, token, secret, loginInfoId) <> (DbOAuth1Info.tupled, DbOAuth1Info.unapply)

}

class OAuth2InfoTable(tag: Tag) extends Table[DbOAuth2Info](tag, "oauth2_info") {

  def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def accessToken: Rep[String] = column[String]("access_token")

  def tokenType: Rep[Option[String]] = column[Option[String]]("token_type")

  def expiresIn: Rep[Option[Int]] = column[Option[Int]]("expires_in")

  def refreshToken: Rep[Option[String]] = column[Option[String]]("refresh_token")

  def loginInfoId: Rep[Long] = column[Long]("login_info_id")

  def * : ProvenShape[DbOAuth2Info] = (id.?, accessToken, tokenType, expiresIn, refreshToken, loginInfoId) <> (DbOAuth2Info.tupled, DbOAuth2Info.unapply)

}

class OpenIDInfoTable(tag: Tag) extends Table[DbOpenIDInfo](tag, "open_id_info") {

  def id: Rep[String] = column[String]("id", O.PrimaryKey)

  def loginInfoId: Rep[Long] = column[Long]("login_info_id")

  def * : ProvenShape[DbOpenIDInfo] = (id, loginInfoId) <> (DbOpenIDInfo.tupled, DbOpenIDInfo.unapply)

}

class OpenIDAttributeTable(tag: Tag) extends Table[DbOpenIDAttribute](tag, "open_id_attributes") {

  def id: Rep[String] = column[String]("id")

  def key: Rep[String] = column[String]("key")

  def value: Rep[String] = column[String]("value")

  def * : ProvenShape[DbOpenIDAttribute] = (id, key, value) <> (DbOpenIDAttribute.tupled, DbOpenIDAttribute.unapply)

}
