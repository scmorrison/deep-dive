package models

case class Account(id: Int, email: String, password: String, name: String, role: Role)

object Account {

  def findById(id: Int): Option[Account] = {

    val stub: Option[Account] = None
    stub

  }

  def authenticate(email: String, password: String): Option[Account] = {
    val stub: Option[Account] = None
    stub
  }

}
