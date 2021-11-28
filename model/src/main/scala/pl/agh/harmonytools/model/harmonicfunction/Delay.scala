package pl.agh.harmonytools.model.harmonicfunction

import pl.agh.harmonytools.model.chord.ChordComponent
import pl.agh.harmonytools.model.util.ChordComponentManager

case class Delay(first: ChordComponent, second: ChordComponent) {
  def toList: List[ChordComponent] = {
    List(first, second)
  }
}

object Delay {
  def apply(first: String, second: String): Delay =
    Delay(
      ChordComponentManager.chordComponentFromString(first),
      ChordComponentManager.chordComponentFromString(second)
    )

  def apply(pair: (String, String)): Delay =
    Delay(
      pair._1,
      pair._2
    )
}
