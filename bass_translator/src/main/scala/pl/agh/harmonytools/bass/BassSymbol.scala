package pl.agh.harmonytools.bass

import pl.agh.harmonytools.bass.AlterationType.{ChordComponentType, ELEVATED, LOWERED}
import pl.agh.harmonytools.model.chord.ChordComponent
import pl.agh.harmonytools.model.util.ChordComponentManager

case class BassSymbol(var component: Int = 3, alteration: Option[AlterationType.FiguredBassType] = None) {
  def mapToChordComponentSymbol(): ChordComponent = {
    val alt = alteration flatMap[String] {
      case AlterationType.SHARP => Some(AlterationType.ELEVATED.value)
      case AlterationType.FLAT => Some(AlterationType.LOWERED.value)
      case _ => None
    }
    ChordComponentManager.chordComponentFromString(component.toString + alt.getOrElse(""))
  }
}
