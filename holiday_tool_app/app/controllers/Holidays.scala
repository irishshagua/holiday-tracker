package controllers

import play.api._
import play.api.mvc._

object Holidays extends Controller with Secured {

  def loadUserHolidayInfo() = withAuth { username => implicit request =>
    Ok(views.html.holidays("Holidays - User Info", username))
  }
}