package pl.agh.harmonytools.finder

import pl.agh.harmonytools.model.chord.{Chord, ChordComponent}
import pl.agh.harmonytools.model.harmonicfunction.BaseFunction.BaseFunction
import pl.agh.harmonytools.model.harmonicfunction.{BaseFunction, HarmonicFunction}
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.key.Mode.Mode
import pl.agh.harmonytools.model.note.Note
import pl.agh.harmonytools.solver.harmonics.generator.{ChordGenerator, ChordGeneratorInput}
import pl.agh.harmonytools.utils.Extensions.ExtendedInt

abstract class Functions(val key: Key) {
  private val chordGenerator: ChordGenerator = ChordGenerator(key)

  protected val functions: List[HarmonicFunction]

  protected lazy val exampleChords: List[Chord] = {
    val inputs = functions.map(ChordGeneratorInput(_, allowDoubleThird = true))
    val ch     = inputs.map(chordGenerator.generate).reduce(_ ++ _)
//    println(ch.size)
    ch
  }

  def compare(
    nonLegalChord: Chord,
    pattern: Chord,
    previousBaseFunction: BaseFunction,
    previousKey: Option[Key],
    previousMode: Mode
  ): Int = {
    val notesDiff           = Math.abs(nonLegalChord.notes.map(_.pitch).sum - pattern.notes.map(_.pitch).sum)
    val positionsDiff       = nonLegalChord.notes.map(_.pitch).zip(pattern.notes.map(_.pitch)).count(x => x._1 != x._2)
    val positionsDiffPoints = if (positionsDiff <= 1) 0 else positionsDiff * 100
    val inversionPoints = {
      if (pattern.harmonicFunction.getBasicChordComponents.contains(pattern.harmonicFunction.inversion)) 0
      else 50
    }
    val prevBF = previousBaseFunction match {
      case BaseFunction.DOMINANT
          if pattern.harmonicFunction.baseFunction.isSubdominant && previousKey == pattern.harmonicFunction.key && previousMode.isMajor =>
        1000
      case _ => 0
    }
    notesDiff + positionsDiffPoints + prevBF + inversionPoints
  }

  def fitToKnown(
    nonLegalChord: Chord,
    previousBaseFunction: BaseFunction,
    previousKey: Option[Key],
    previousMode: Mode
  ): Chord = {
    val ch = exampleChords.minBy { pattern =>
      compare(nonLegalChord, pattern, previousBaseFunction, previousKey, previousMode)
    }
//    println(compare(nonLegalChord, ch, previousBaseFunction))
    ch
  }
}
