package controllers

import java.util.UUID
import play.api.libs.json._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.cache._
import services.AccountService
import models.{AnormAccountRepository, Account}

/**
 * Security actions that should be used by all controllers that need to protect their actions.
 * Can be composed to fine-tune access control.
 */
trait Security { self: Controller =>

  implicit val app: play.api.Application = play.api.Play.current

  val AuthTokenHeader = "X-XSRF-TOKEN"
  val AuthTokenCookieKey = "XSRF-TOKEN"
  val AuthTokenUrlKey = "auth"

  /** Checks that a token is either in the header or in the query string */
  def HasToken[A](p: BodyParser[A] = parse.anyContent)(f: String => Long => Request[A] => Result): Action[A] =
    Action(p) { implicit request =>
      val maybeToken = request.headers.get(AuthTokenHeader).orElse(request.getQueryString(AuthTokenUrlKey))
      maybeToken flatMap { token =>
        Cache.getAs[Long](token) map { accountId =>
          f(token)(accountId)(request)
        }
      } getOrElse Unauthorized(Json.obj("err" -> "No Token"))
    }

}

/** General Application actions, mainly session management */
trait Application extends Controller with Security {

  lazy val CacheExpiration =
    app.configuration.getInt("cache.expiration").getOrElse(60 /*seconds*/ * 2 /* minutes */)

  lazy val accountService = new AccountService(AnormAccountRepository)

  /** Returns the index page */
  def index = Action {
    Ok(views.html.index("Go Deep Dive!"))
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

  /** Check credentials, generate token and serve it back as auth token in a Cookie */
  def login = Action(parse.json) { implicit request =>
    loginForm.bind(request.body).fold( // Bind JSON body to form values
      formErrors => BadRequest(Json.obj("err" -> formErrors.errorsAsJson)),
      loginData => {
        accountService.authenticate(loginData.email, loginData.password) map { account =>
          val token = java.util.UUID.randomUUID().toString
          Ok(Json.obj(
            "authToken" -> token,
            "accountId" -> account.id.get
          )).withToken(token -> account.id.get)
        } getOrElse NotFound(Json.obj("err" -> "Account Not Found or Password Invalid"))
      }
    )
  }

  /** Invalidate the token in the Cache and discard the cookie */
  def logout = Action { implicit request =>
    request.headers.get(AuthTokenHeader) map { token =>
      Ok(Json.obj(
        "message" -> "ok"
      )).discardingToken(token)
    } getOrElse BadRequest(Json.obj("err" -> "No Token"))
  }

  /**
   * Returns the current account's ID if a valid token is transmitted.
   * Also sets the cookie (useful in some edge cases).
   * This action can be used by the route service.
   */
  def ping() = HasToken() { token => accountId => implicit request =>
    accountService.findOneById (accountId) map { account =>
      Ok(Json.obj("accountId" -> accountId)).withToken(token -> accountId)
    } getOrElse NotFound (Json.obj("err" -> "Account Not Found"))
  }

}

object Application extends Application
