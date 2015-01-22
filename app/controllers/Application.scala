package controllers

import play.api.mvc._
import jp.t2v.lab.play2.auth.{AuthElement, LoginLogout}
import models.Role.{NormalUser, Administrator}
import play.api.data._
import play.api.data.Forms._
import services.AccountService
import security.AuthConfigImpl

object Application extends Controller with AuthElement with LoginLogout with AuthConfigImpl {


  // example unsecured action
  def index = Action {
    Ok(views.html.index("Deep Dive!"))
  }

  // TODO: this will be a secured page for a user
  def main = StackAction(AuthorityKey -> NormalUser) { implicit request =>
    val user = loggedIn
    val title = "message main"
    Ok(views.html.index(title))
  }

  // TODO: this will be a secured page for an administrator
  def write = StackAction(AuthorityKey -> Administrator) { implicit request =>
    val user = loggedIn
    val title = "write message"
    Ok(views.html.index(title))
  }

  // TODO: future login form
  val loginForm = Form {
    mapping("email" -> email, "password" -> text)(AccountService.authenticate)(_.map( u => (u.email, "")))
      .verifying("Invalid email or password", result => result.isDefined)
  }

  // TODO: login action
  def login = Action { implicit request =>
    Ok(views.html.index("hello"))
  }

  // TODO: logout action
  def logout = Action { implicit request =>
    // do something
    Ok(views.html.index("hello"))
  }

  // TODO: authenticate action
  def authenticate = Action { implicit request =>
    Ok(views.html.index("hello"))
  }

}
