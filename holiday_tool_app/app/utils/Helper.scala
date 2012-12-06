package utils

import java.util.Calendar
import java.util.Date

object Helper {

  val UNDEFINED_FUTURE_DATE: Date = {
    val cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, 10000)
    cal.getTime()
  }
}