package pl.agh.harmonytools.utils

object Extensions
{
  implicit class ExtendedInt (val i: Int) extends AnyVal {
    def %% (m: Int): Int = {val x = i % m; if (x < 0) x + m else x}
  }

  implicit class ExtendedBoolean (val p: Boolean) extends AnyVal {
    def xor (q: Boolean): Boolean = {
      (p || q) && !(p && q)
    }
  }
}
