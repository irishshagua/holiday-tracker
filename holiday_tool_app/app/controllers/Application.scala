package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

import model.User

object Application extends Controller with Secured {
  
  def index = Action { implicit request =>
    Redirect(routes.Application.landingPage)
  }
  
  def landingPage = withAuth {username => implicit request =>
    Ok(views.html.landing_page("Holidays - Landing Page", username))
  }
}