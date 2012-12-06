package utils.db

import anorm._
import anorm.SqlParser._

import play.api.db.DB
import play.api.Play.current

import model.User
import model.Locale

object DBUserAccessLayer {
  
  
  def returnUserByEmailAndPassword(email: String, password: String) = {
    DB.withConnection { implicit conn =>
      SQL(
        """
        SELECT 
    		  e.id, e.user_name, e.contact_email, l.locale_short_name 
        FROM 
    		  employee e, locale l
        WHERE
    		  e.locale = l.id
        AND
    		  contact_email = {email} and password = {password}
        """
      ).on(
        'email -> email,
        'password -> password
      ).as(User.parseSqlRow.singleOpt)
    }
  }
  
  def returnUsersByManager(userId: Int): Seq[User] = {
    DB.withConnection { implicit conn =>
    	SQL("""
    	    SELECT 
    			e.id, e.user_name, e.contact_email, l.locale_short_name 
    		FROM
    			employee e, locale l
    		WHERE
    			e.locale = l.id
    		AND
    			e.manager_id = {userId}
    	    """)
    	    .on("userId" -> userId)
    	    .as(User.parseSqlRow *)
    }
  }
  
  def returnUserById(userId: Int): User = {
    DB.withConnection { implicit conn =>
    	SQL("""
    	    SELECT 
    			e.id, e.user_name, e.contact_email, l.locale_short_name 
    		FROM
    			employee e, locale l
    		WHERE
    			e.locale = l.id
    		AND
    			e.id = {userId}
    	    """)
    	    .on("userId" -> userId)
    	    .as(User.parseSqlRow *)
    	    .head
    }
  }
  
  def returnUserRole(email: String): Int = {
    DB.withConnection {implicit conn =>
      SQL("""
          SELECT
    		  user_role
          FROM
    		  employee
          WHERE
    		  contact_email = {eml}
          """)
          .on("eml" -> email)
          .as(scalar[Int].single)
    }
  }
  
  def insertNewUser(userName: String, email: String, locale: Int, role: Int, mgrId: Int) {
    DB.withConnection {implicit conn =>
      	SQL("""
      	    INSERT INTO
      			employee (user_name, password, contact_email, locale, user_role, manager_id)
      		VALUES
      			({un}, 'password', {eml}, {lcl}, {rl}, {mgr})
      	    """)
      	    .on("un" -> userName,
      	        "eml" -> email,
      	        "lcl" -> locale,
      	        "rl" -> role,
      	        "mgr" -> mgrId)
      	    .executeInsert()
    }
  }
  
  def updateExistingUser(userId: Int, userName: String, contactEmail: String, locale: Int) {
    DB.withConnection {implicit conn =>
      	SQL("""
      	    UPDATE
      			employee
      		SET
      			user_name = {un},
      			contact_email = {eml},
      			locale = {lcl}
      		WHERE
      			id = {uid}
      	    """)
      	    .on("uid" -> userId,
      	        "un" -> userName,
      	        "eml" -> contactEmail,
      	        "lcl" -> locale)
      	    .executeUpdate()
    }
  }  
}