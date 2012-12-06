package controllers

import play.api._
import play.api.mvc._

object Error extends Controller {
  
  def index = Action {
    Redirect(routes.Error.displayErrorInformation(404))
  }

  def displayErrorInformation(errorCode: Int) = Action {
    NotFound(views.html.error(errorCode))
  }
}