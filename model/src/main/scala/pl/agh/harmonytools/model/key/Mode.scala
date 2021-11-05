package pl.agh.harmonytools.model.key

object Mode {
  sealed trait Mode

  case object MAJOR extends Mode
  case object MINOR extends Mode

  val values: List[Mode] = List(MAJOR, MINOR)
}
