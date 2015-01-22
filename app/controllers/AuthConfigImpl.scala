package controllers

import jp.t2v.lab.play2.auth.{AuthConfig, AuthElement}
import models.{Account, Role}
import models.Role.{Administrator, NormalUser}
import play.api._
import play.api.mvc._
import play.api.mvc.Results._
import scala.concurrent.{ExecutionContext, Future}
import scala.reflect._


trait AuthConfigImpl extends AuthConfig {

  // a type that is used to identify a user
  type Id = Long

  // a type that represents a user
  type User = Account

  // a type that is defined by every action for authorization
  type Authority = Role

  // a classtag is used to retrieve an id from the cache api
  val idTag: ClassTag[Id] = classTag[Id]

  // session timeout in seconds
  val sessionTimeoutInSeconds: Int = 3600

  // a function that returns a user from an id
  def resolveUser(id: Id)(implicit ctx: ExecutionContext) = Future.successful(null)

  // where to redirect after successful login
  def loginSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext): Future[Result] =
    // TODO stub
    Future.successful(Redirect("/"))

  // where to redirect after logging out
  def logoutSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext): Future[Result] =
    Future.successful(Redirect("/"))

  def authenticationFailed(request: RequestHeader)(implicit context: ExecutionContext): Future[Result] =
    Future.successful(Redirect("/"))

  // redirect on auth failure (usually due to bad password)
  def authorizationFailed(request: RequestHeader)(implicit ctx: ExecutionContext): Future[Result] =
    Future.successful(Forbidden("no permission"))

  // a function that determines what authority a user has
  def authorize(user: User, authority: Authority)(implicit ctx: ExecutionContext): Future[Boolean] = Future.successful {
    (user.role, authority) match {
      case (Administrator, _)       => true
      case (NormalUser, NormalUser) => true
      case _                        => false
    }
  }

  override lazy val cookieSecureOption: Boolean = play.api.Play.isProd(play.api.Play.current)

  override lazy val isTransientCookie: Boolean = false
}
