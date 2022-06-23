package pl.agh.harmonytools.model.key

import pl.agh.harmonytools.model.scale.{MajorScale, MinorScale, Scale, ScaleCompanion}

object Mode {
  sealed trait Mode {
    def isMajor: Boolean
    def isMinor: Boolean
    def scale: ScaleCompanion
  }

  case object MAJOR extends Mode {
    override def isMajor: Boolean = true

    override def isMinor: Boolean = false

    override def scale: ScaleCompanion = MajorScale
  }
  case object MINOR extends Mode {
    override def isMajor: Boolean = false

    override def isMinor: Boolean = true

    override def scale: ScaleCompanion = MinorScale
  }

  val values: List[Mode] = List(MAJOR, MINOR)
}
