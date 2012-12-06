package model

import play.api.db._
import play.api.Play.current
 
import anorm._
import anorm.SqlParser._

import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

import utils.db.DBUserAccessLayer
import utils.db.DBBusinessProcess
import utils.Helper

import java.util.Date


case class User(id: Pk[Int], userName: String, contactEmail: String, localeShortName: String)

object User {
  
  // Used at anorm level to transform row to User object
  val parseSqlRow: RowParser[User] = {
    get[Pk[Int]]("id") ~
    get[String]("user_name") ~
    get[String]("contact_email")~
    get[String]("locale_short_name") map {
    	case id ~ userName ~ contactEmail ~ localeShortName => User(id, userName, contactEmail, localeShortName)
    }
  }
  
  def countRemainingUnusedHolidaysThisYear(email: String): Long = {
    DBBusinessProcess.countUserRemainingHolidays(email)
  }
  
  def nextHolidayForUser(email: String): Date = {
    val nextHolidays = DBBusinessProcess.showNextPublicAndPrivateHoliday(email)
    
    nextHolidays match {
      case (Some(date1), Some(date2)) => if (date1.before(date2)) date1 else date2
      case (None, Some(date)) => date
      case (Some(date), None) => date
      case (None, None) => Helper.UNDEFINED_FUTURE_DATE 
    }
  }
  
  def findAllUsersByManager(mgrId: Int): Seq[User] = {
    DBUserAccessLayer.returnUsersByManager(mgrId)
  }
  
  def findUsersById(userId: Int): User = {
    DBUserAccessLayer.returnUserById(userId)
  }
  
  
  def createNewUser(validValues: (String, String, Int)) = {
    val userName = validValues._1
    val email = validValues._2
    val locale = validValues._3
    
    DBUserAccessLayer.insertNewUser(userName, email, locale, 1, 1)
  }
  
  def updateUser(userId: Int, validValues: (String, String, Int)) = {   
    DBUserAccessLayer.updateExistingUser(userId, validValues._1, validValues._2, validValues._3)
  }
  
  def authenticate(email: String, password: String): Boolean = {
    DBUserAccessLayer.returnUserByEmailAndPassword(email, password).isDefined
  }
  
  def isAdmin(email: String): Boolean = {
    DBUserAccessLayer.returnUserRole(email) match {
      case 2 => true
      case _ => false
    }
  }
}