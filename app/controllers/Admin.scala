package controllers

import play.api.mvc._

trait Admin extends Controller with Security {

  /** Returns the admin page */
  def admin() = HasToken() { _ => user => implicit request =>
    Ok(views.html.index())
  }

  def dashboard() = HasToken() { _ => user => implicit request =>
    Ok(views.html.index())
  }
}

object Admin extends Admin
