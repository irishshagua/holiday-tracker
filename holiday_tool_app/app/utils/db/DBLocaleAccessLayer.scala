package utils.db

import anorm._
import anorm.SqlParser._

import play.api.db.DB
import play.api.Play.current

import model.User
import model.Locale

object DBLocaleAccessLayer {
  
  def returnAllLocales: Seq[Locale] = {
    DB.withConnection { implicit conn =>
    	SQL("""
    	    SELECT 
    			* 
    		FROM
    			locale
    	    """)
    	    .as(Locale.parseSqlRow *)
    }
  }
  
}