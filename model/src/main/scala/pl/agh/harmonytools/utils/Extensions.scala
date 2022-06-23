package pl.agh.harmonytools.utils

import java.time.LocalDateTime

object Extensions {
  implicit class ExtendedInt(val i: Int) extends AnyVal {
    def %%(m: Int): Int = { val x = i % m; if (x < 0) x + m else x }
    def isPowerOf2: Boolean = {
      i % 2 match {
        case 1 =>
          if (i == 1) true
          else false
        case 0 => (i / 2).isPowerOf2
      }
    }
  }

  implicit class ExtendedBoolean(val p: Boolean) extends AnyVal {
    def xor(q: Boolean): Boolean =
      (p || q) && !(p && q)
  }

  implicit class ExtendedLocalDateTime(val ldt: LocalDateTime) extends AnyVal {
    def dateString: String = {
      def to2Digits(number: Int): String = {
        if (number < 10)
          s"0$number"
        else
          number.toString
      }
      val day    = to2Digits(ldt.getDayOfMonth)
      val month  = to2Digits(ldt.getMonthValue)
      val year   = to2Digits(ldt.getYear)
      val hour   = to2Digits(ldt.getHour)
      val minute = to2Digits(ldt.getMinute)
      val second = to2Digits(ldt.getSecond)
      s"${day}_${month}_${year}_${hour}_${minute}_${second}"
    }
  }
}
