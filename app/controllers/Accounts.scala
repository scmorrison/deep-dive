package controllers

import play.api.libs.json.Json
import play.api.mvc.Controller
import services.AccountService
import models.AnormAccountRepository



trait Accounts extends Controller with Security {

  lazy val accountService = new AccountService(AnormAccountRepository)

  /** Example for token protected access */
  def myAccountInfo() = HasToken() { _ => currentId => implicit request =>
    accountService.findOneById(currentId) map { account =>
      Ok(Json.toJson(account))
    } getOrElse NotFound (Json.obj("err" -> "Account not found"))
  }
}

object Accounts extends Accounts
