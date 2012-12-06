package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

import model.User

object Administration extends Controller with Secured {
  
  val userForm = Form(
      tuple(
          "userName" -> nonEmptyText,
          "email" -> email,
          "locale" -> number
      ) verifying ("User Already Exists", result => result match {
      		case (userName, email, locale) => true // TODO: Implement Properly
      })
  )
  
  val userEditForm = Form(
      tuple(
          "userName" -> nonEmptyText,
          "email" -> email,
          "locale" -> number
      ) verifying ("Invalid Selection", result => result match {
      		case (userName, email, locale) => true // TODO: Implement Properly
      })
  )
     
  def adminServices = withAuth { username => implicit request =>
    println(username)
    Ok(views.html.admin("Holidays - Administration", username))
  }
  
  
  def administerUsers = withAuth { username => implicit request =>
    Ok(views.html.admin_user("Holidays - User Administration", username, userForm))
  }
  
  def createNewUser = withAuth { username => implicit request =>
  	userForm.bindFromRequest.fold(
  			error => BadRequest(views.html.error(400)),
  			validValues => {
  				User.createNewUser(validValues)
  				Redirect(routes.Administration.administerUsers)
  			}
  		)
  }
  
  def updateUser(userId: Int) = withAuth { username => implicit request =>
  	userForm.bindFromRequest.fold(
  			error => BadRequest(views.html.error(400)),
  			validValues => {
  				User.updateUser(userId, validValues)
  				Redirect(routes.Administration.administerUsers)
  			}
  		)
  }
  
  def administerSpecificUser(userId: Int) = withAuth { username => implicit request =>
    Ok(views.html.admin_user_edit("Holidays - User["+userId+"] Administration", username, User.findUsersById(userId), userEditForm))
  }
  
  def administerCalendars = withAuth { username => implicit request =>
    Ok(views.html.admin_calendar("Holidays - Calendar Administration"))
  }
  
  def administerSpecificCalendar(locale: Int) = withAuth { username => implicit request =>
    Ok(views.html.admin_calendar("Holidays - Calendar["+locale+"] Administration"))
  }
}