package controllers

import play.api.mvc._
import jp.t2v.lab.play2.auth.AuthElement
import models.Role.{NormalUser, Administrator}

object Application extends Controller with AuthElement with AuthConfigImpl {

  def index = Action {
    Ok(views.html.index("Deep Dive!"))
  }

  def main = StackAction(AuthorityKey -> NormalUser) { implicit request =>
    val user = loggedIn
    val title = "message main"
    Ok(views.html.index(title))
  }

  def list = StackAction(AuthorityKey -> NormalUser) { implicit request =>
    val user = loggedIn
    val title = "all messages"
    Ok(views.html.index(title))
  }

  def detail(id: Int) = StackAction(AuthorityKey -> NormalUser) { implicit request =>
    val user = loggedIn
    val title = "messages detail "
    Ok(views.html.index(title + id))
  }

  def write = StackAction(AuthorityKey -> Administrator) { implicit request =>
    val user = loggedIn
    val title = "write message"
    Ok(views.html.index(title))
  }

}
