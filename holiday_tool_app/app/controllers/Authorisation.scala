package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

import model.User

object Authorisation extends Controller with Secured {

  val loginForm = Form(
    tuple(
      "email" -> text,
      "password" -> text
    ) verifying ("Invalid email or password", result => result match {
      case (email, password) => User.authenticate(email, password)
    })
  )
  
  def login = Action {
    Ok(views.html.index(loginForm))
  }

  def logout = Action {
    Redirect(routes.Authorisation.login).withNewSession
  }
  
  
  def check(username: String, password: String) = {
    (username == "admin" && password == "1234")  
  }
  
  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.error(403)),
      user => Redirect(routes.Application.landingPage).withSession(Security.username -> user._1)
    )
  }
}

/*
 * 
 */
trait Secured {
  def username(request: RequestHeader) = request.session.get(Security.username)

  def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Authorisation.login)

  def withAuth(f: => String => Request[AnyContent] => Result) = {
    Security.Authenticated(username, onUnauthorized) { user =>
      Action(request => f(user)(request))
    }
  }
}