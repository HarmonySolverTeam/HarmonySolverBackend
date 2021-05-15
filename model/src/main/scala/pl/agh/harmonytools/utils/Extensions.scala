package pl.agh.harmonytools.utils

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
}
