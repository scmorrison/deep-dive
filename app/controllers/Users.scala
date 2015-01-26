package controllers

import play.api.libs.json.Json
import play.api.mvc._
import services.UserService
import models.{AnormUserRepository, User}



trait Users extends Controller with Security {

  lazy val userService = new UserService(AnormUserRepository)

  /** Example for token protected access */
  def myAccountInfo() = HasToken() { _ => currentId => implicit request =>
    userService.findOneById(currentId) map { user =>
      Ok(Json.toJson(user))
    } getOrElse NotFound (Json.obj("err" -> "User not found"))
  }

  def CanEditUser[A](userId: Long, p: BodyParser[A] = parse.anyContent)(f: User => Request[A] => Result) =
    HasToken(p) { _ => currentId => request =>
      if(userId == currentId) {
        userService.findOneById(currentId) map { user =>
          f(user)(request)
        } getOrElse NotFound (Json.obj("err" -> "User Not Found"))
      } else {
        Forbidden (Json.obj("err" -> "You don't have sufficient privileges"))
      }
    }

  def findOneById(id: Long) = CanEditUser(id) { user => _ =>
    Ok(Json.toJson(user))
  }
}

object Users extends Users
