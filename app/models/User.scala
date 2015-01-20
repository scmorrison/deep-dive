package models

case class User(
  id: Option[Long],
  email: String,
  password: String,
  name: String
)


object User {

  import anorm._
  import play.api.db.DB
  import anorm.SqlParser.{ scalar,matches,long,str,flatten,get }

  def findOneById(id: Long): Option[User] = {

    import play.api.Play.current

    DB.withConnection { implicit c =>

      val userStrings: List[(Long, String, String, String)] = SQL(
      """
      select id, email, password, name from dd_user u
      where u.id = {id};
      """
      ).on("id" -> id)
        .as (long("id")~ str("email") ~ str("password") ~ str("name") map(flatten) * )

      userStrings.headOption.map { f =>
        User(Option(f._1), f._2, f._3, f._4)
      }
    }

  }

  def findOneByEmailAndPassword(email: String, password: String): Option[User] = {
    // TODO: another stub
    Some(User(Some(3l), email, password, "Takeshi Tanaka"))
  }
}
