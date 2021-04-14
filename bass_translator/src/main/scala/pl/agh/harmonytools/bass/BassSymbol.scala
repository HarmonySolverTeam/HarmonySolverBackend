package pl.agh.harmonytools.bass

import pl.agh.harmonytools.bass.AlterationType.{ChordComponentType, ELEVATED, LOWERED}
import pl.agh.harmonytools.error.UnexpectedInternalError
import pl.agh.harmonytools.model.chord.ChordComponent
import pl.agh.harmonytools.model.util.ChordComponentManager

case class BassSymbol(var component: Int = 3, alteration: AlterationType.FiguredBassType = AlterationType.EMPTY) {
  def mapToChordComponentSymbol(): ChordComponent = {
    val alt = alteration match {
      case AlterationType.SHARP => Some(AlterationType.ELEVATED.value)
      case AlterationType.FLAT  => Some(AlterationType.LOWERED.value)
      case AlterationType.EMPTY => None
      case unknown              => throw UnexpectedInternalError(s"Unknown alteration type: $unknown")
    }
    ChordComponentManager.chordComponentFromString(component.toString + alt.getOrElse(""))
  }

  override def toString: String =
    component.toString + alteration.value
}
