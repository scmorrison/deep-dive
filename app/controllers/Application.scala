package controllers

import java.util.UUID
import play.api.libs.json._
import play.api.mvc._
import jp.t2v.lab.play2.auth.{AuthElement, LoginLogout}
import models.Role.{NormalUser, Administrator}
import play.api.data._
import play.api.data.Forms._
import play.cache._
import services.AccountService
import models.AnormAccountRepository
import security.AuthConfigImpl

object Application extends Controller with AuthElement with LoginLogout with AuthConfigImpl {

  val accountService = new AccountService(AnormAccountRepository)
  val AuthTokenCookieKey = "XSRF-TOKEN"
  implicit val app: play.api.Application = play.api.Play.current

  lazy val CacheExpiration =
    app.configuration.getInt("cache.expiration").getOrElse(60 /*seconds*/ * 2 /* minutes */)

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


  case class Login(email: String, password: String)

  val loginForm = Form(
    mapping(
      "email" -> email,
      "password" -> nonEmptyText
    )(Login.apply)(Login.unapply)
  )

  implicit class ResultWithToken(result: Result) {
    def withToken(token: (String, Long)): Result = {
      Cache.set(token._1, token._2, CacheExpiration)
      result.withCookies(Cookie(AuthTokenCookieKey, token._1, None, httpOnly = false))
    }

    def discardingToken(token: String): Result = {
      Cache.remove(token)
      result.discardingCookies(DiscardingCookie(name = AuthTokenCookieKey))
    }
  }

  // TODO: login action
  def login = Action(parse.json) { implicit request =>
    loginForm.bind(request.body).fold(
      formErrors => BadRequest(Json.obj("err" -> formErrors.errorsAsJson)),
      loginData => {
        accountService.authenticate(loginData.email, loginData.password) map { user =>
          val token = UUID.randomUUID().toString
          Ok(Json.obj(
            "authToken" -> token,
            "userId" -> user.id
          )).withToken(token -> user.id.get)
        } getOrElse NotFound(Json.obj("err" -> "Account not found or Password invalid"))
      }
    )
  }

  // TODO: logout action
  def logout = Action { implicit request =>
    // do something
    Ok(views.html.index("hello"))
  }

  // TODO: logout action
  def ping = Action { implicit request =>
    // do something
    Ok(views.html.index("hello"))
  }


  // TODO: authenticate action
  def authenticate = Action { implicit request =>
    Ok(views.html.index("hello"))
  }

}
