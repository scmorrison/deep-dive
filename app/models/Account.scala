package models

sealed trait Role

object Role {

  case object Administrator extends Role
  case object NormalUser extends Role

  def valueOf(value: String): Role = value match {
    case "Administrator" => Administrator
    case "NormalUser"    => NormalUser
    case _ => throw new IllegalArgumentException()
  }
}

case class Account(
  id: Option[Long],
  email: String,
  password: Option[String],
  name: String,
  role: Role
) {

  def getRole(): Role = {
    return this.role
  }
}

trait AccountRepository {

  def findOneByEmailAndPassword(email: String, password: String): Option[Account]
}

object AnormAccountRepository extends AccountRepository {

  import anorm._
  import anorm.SqlParser._
  import play.api.db.DB
  import play.api.Play.current

  val roleParser: RowParser[Role] = {

    get[String]("name") map {
      case name => Role.valueOf(name)
    }
  }

  val accountParser: RowParser[Account] = {

    long("id") ~ str("email") ~ str("name") map {
      case i~e~n => Account(id=Some(i),email=e,password=null,name=n, Role.NormalUser)
    }
  }

  def findOneByEmailAndPassword(email: String, password: String): Option[Account] = {
    DB.withConnection{ implicit c =>
      val maybeAccount: Option[Account] = SQL(
        """
        select id, email, name from dd_user where email={email} and password={password};
        """
      ).on(
        'email -> email,
        'password -> password
      ).as(accountParser.singleOpt)

      maybeAccount
    }
  }

  def findOneById(id: Long): Option[Account] = {
    DB.withConnection { implicit c =>
      val maybeAccount: Option[Account] = SQL(
        """
        select id, email, name from user where id={id};
        """
      ).on(
        'id -> id
      ).as(accountParser.singleOpt)
      maybeAccount
    }
  }

}
