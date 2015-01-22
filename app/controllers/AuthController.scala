package controllers

import play.api.mvc._
import jp.t2v.lab.play2.auth.LoginLogout
import play.api.data._
import play.api.data.Forms._
import services.AccountService

object AuthController extends Controller with LoginLogout with AuthConfigImpl {

  val loginForm = Form {
    mapping("email" -> email, "password" -> text)(AccountService.authenticate)(_.map( u => (u.email, "")))
      .verifying("Invalid email or password", result => result.isDefined)
  }

  def login = Action { implicit request =>
    Ok(views.html.index("hello"))
  }

  def logout = Action { implicit request =>
    // do something
    Ok(views.html.index("hello"))
  }

  def authenticate = Action { implicit request =>
    Ok(views.html.index("hello"))
  }

}
