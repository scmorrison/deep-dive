package controllers

import play.api.mvc._

trait Admin extends Controller with Security {

  /** Returns the admin page */
  def admin() = HasToken() { _ => user => implicit request =>
    Ok(views.html.admin("Go Deep Dive Admin!"))
  }

  def dashboard() = HasToken() { _ => user => implicit request =>
    Ok(views.html.admin("Go to deep dive"))
  }
}

object Admin extends Admin
