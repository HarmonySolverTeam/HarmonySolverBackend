package pl.agh.harmonytools.soprano.genetic

import pl.agh.harmonytools.algorithm.generator.LayerGenerator
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.solver.harmonics.generator.{ChordGenerator, ChordGeneratorInput}
import pl.agh.harmonytools.solver.soprano.generator.{HarmonicFunctionGenerator, HarmonicFunctionGeneratorInput}

case class SopranoChordGenerator(
  allowedHarmonicFunctions: List[HarmonicFunction],
  key: Key
) extends LayerGenerator[Chord, HarmonicFunctionGeneratorInput] {
  private val harmonicFunctionGenerator = HarmonicFunctionGenerator(allowedHarmonicFunctions, key)
  private val chordGenerator            = ChordGenerator(key)

  override def generate(input: HarmonicFunctionGeneratorInput): List[Chord] = {
    val harmonicFunctions = harmonicFunctionGenerator.generate(input)
    val chordGeneratorInputs = harmonicFunctions match {
      case Nil => List()
      case head :: Nil => List(head.toChordGeneratorInput(true))
      case head :: tail => head.toChordGeneratorInput(true) +: tail.map(_.toChordGeneratorInput())
    }
    chordGeneratorInputs.map(chordGenerator.generate).reduceOption(_ ++ _).getOrElse(List())
  }
}
