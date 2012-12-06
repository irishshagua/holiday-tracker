package utils.db

import anorm._
import anorm.SqlParser._

import play.api.db.DB
import play.api.Play.current

import model.User
import model.Locale

import java.util.Date

/*
 * Store DB Business Process SQL
 */
object DBBusinessProcess {
  
  def countUserRemainingHolidays(email: String): Long = {
    DB.withConnection { implicit conn =>
    	SQL("""
    	    SELECT
    			(holiday_allocation - 
    				(select count(*) from user_holiday where user_id = 
    					(select id from employee where contact_email = {eml})
    	            )
    	        ) 
    			as num_hols_remianing
    		FROM
    			employee
    		WHERE
    			contact_email = {eml}
    	    """)
    	    .on("eml" -> email)
    	    .as(scalar[Long].single)
    }
  }
  
  def showNextPublicAndPrivateHoliday(email: String): (Option[Date], Option[Date]) = {
    val nextUserHoliday: Option[Date] = DB.withConnection { implicit conn =>
    	SQL("""
    	    SELECT 
    			day_of_holiday 
    		FROM 
    			user_holiday 
    		WHERE 
    			user_id = (SELECT
    						id
    					   FROM
    						employee
    					   WHERE
    						contact_email = {eml})
    	    AND
    			approved = 1
    		AND
    			day_of_holiday > now()
    		ORDER BY 
    			day_of_holiday
    		ASC LIMIT 1
    	    """)
    	    .on("eml" -> email)
    	    .as(scalar[Date].singleOpt)
    }
    
    val nextPublicHoliday: Option[Date] = DB.withConnection { implicit conn =>
    	SQL("""
    	    SELECT 
    			day_of_holiday 
    		FROM 
    			public_holiday 
    		WHERE 
    			locale = (SELECT
    						locale
    					   FROM
    						employee
    					   WHERE
    						contact_email = {eml})
    		AND
    			day_of_holiday > now()
    		ORDER BY 
    			day_of_holiday
    		ASC LIMIT 1
    	    """)
    	    .on("eml" -> email)
    	    .as(scalar[Date].singleOpt)
    }
    
    (nextUserHoliday, nextPublicHoliday)
  }
}