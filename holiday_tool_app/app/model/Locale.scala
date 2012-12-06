package model

import play.api.db._
import play.api.Play.current
 
import anorm._
import anorm.SqlParser._

import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

import utils.db.DBLocaleAccessLayer

case class Locale(id: Pk[Int], localeShortName: String, localeName: String)

object Locale {
  
  // Used at anorm level to transform row to User object
  val parseSqlRow: RowParser[Locale] = {
    get[Pk[Int]]("id") ~
    get[String]("locale_short_name") ~
    get[String]("locale_name") map {
    	case id ~ localeShortName ~ localeName => Locale(id, localeShortName, localeName)
    }
  }
  
  
  def findAllLocales: Seq[Locale] = {
    DBLocaleAccessLayer.returnAllLocales
  }
  
}