package controllers

import play.api.libs.json.Json
import play.api.mvc._
import services.AccountService
import models.{AnormAccountRepository, Account}



trait Accounts extends Controller with Security {

  lazy val accountService = new AccountService(AnormAccountRepository)

  /** Example for token protected access */
  def myAccountInfo() = HasToken() { _ => currentId => implicit request =>
    accountService.findOneById(currentId) map { account =>
      Ok(Json.toJson(account))
    } getOrElse NotFound (Json.obj("err" -> "Account not found"))
  }

  def CanEditAccount[A](accountId: Long, p: BodyParser[A] = parse.anyContent)(f: Account => Request[A] => Result) =
    HasToken(p) { _ => currentId => request =>
      if(accountId == currentId) {
        accountService.findOneById(currentId) map { account =>
          f(account)(request)
        } getOrElse NotFound (Json.obj("err" -> "Account Not Found"))
      } else {
        Forbidden (Json.obj("err" -> "You don't have sufficient privileges"))
      }
    }

  def findOneById(id: Long) = CanEditAccount(id) { account => _ =>
    Ok(Json.toJson(account))
  }
}

object Accounts extends Accounts
