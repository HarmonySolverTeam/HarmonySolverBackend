package pl.agh.harmonytools.model.chord

import pl.agh.harmonytools.error.{HarmonySolverError, RequirementChecker, UnexpectedInternalError}
import pl.agh.harmonytools.model.util.ChordComponentManager

import scala.collection.immutable.HashMap

case class ChordComponent(
  chordComponentString: String,
  baseComponent: Int,
  semitonesNumber: Int,
  isDown: Boolean
) {
  override def toString: String = chordComponentString

  lazy val toXmlString: String =
    chordComponentString.replace("<", "&lt;").replace(">", "&gt;")

  lazy val alteration: String = chordComponentString.reverse.takeWhile(!_.isDigit).reverse

  def getDecreasedByHalfTone: ChordComponent = {
    alteration match {
      case ""   => ChordComponentManager.chordComponentFromString(baseComponent.toString + ">")
      case "<<" => ChordComponentManager.chordComponentFromString(baseComponent.toString + "<")
      case "<"  => ChordComponentManager.chordComponentFromString(baseComponent.toString)
      case ">"  => ChordComponentManager.chordComponentFromString(baseComponent.toString + ">>")
      case s    => throw UnexpectedInternalError(s"Unexpected alteration symbol $s")
    }
  }

  def getIncreasedByHalfTone: ChordComponent = {
    alteration match {
      case ""   => ChordComponentManager.chordComponentFromString(baseComponent.toString + "<")
      case ">>" => ChordComponentManager.chordComponentFromString(baseComponent.toString + ">")
      case ">"  => ChordComponentManager.chordComponentFromString(baseComponent.toString)
      case "<"  => ChordComponentManager.chordComponentFromString(baseComponent.toString + "<<")
      case s    => throw UnexpectedInternalError(s"Unexpected alteration symbol $s")
    }
  }
}

object ChordComponent {

  final val baseComponentPitch: HashMap[Int, Int] = HashMap(
    1  -> 0,
    2  -> 2,
    3  -> 4,
    4  -> 5,
    5  -> 7,
    6  -> 9,
    7  -> 10,
    8  -> 12,
    9  -> 14,
    10 -> 16,
    11 -> 17,
    12 -> 19,
    13 -> 21
  )

  def apply(chordComponentString: String, isDown: Boolean = false): ChordComponent = {
    RequirementChecker.isRequired(
      chordComponentString.matches("\\d+(<+|>+)?"),
      ChordComponentParseError(
        s"Illegal form of chord component: $chordComponentString. Should be like digits appended with < or > signs."
      )
    )

    val baseComponent = {
      if (chordComponentString.head.isDigit) Integer.parseInt(chordComponentString.takeWhile(_.isDigit))
      else Integer.parseInt(chordComponentString.reverse.takeWhile(_.isDigit).reverse)
    }
    var semitonesNumber = baseComponentPitch.getOrElse(
      baseComponent,
      throw UnexpectedInternalError("Illegal baseComponent: " + baseComponent)
    )
    semitonesNumber += chordComponentString.count(_ == '<')
    semitonesNumber -= chordComponentString.count(_ == '>')
    if (isDown) semitonesNumber -= 1
    new ChordComponent(
      chordComponentString,
      baseComponent,
      semitonesNumber,
      isDown
    )
  }
}

case class ChordComponentParseError(msg: String) extends HarmonySolverError(msg) {
  override val source: String = "Error during creating chord component"
}
