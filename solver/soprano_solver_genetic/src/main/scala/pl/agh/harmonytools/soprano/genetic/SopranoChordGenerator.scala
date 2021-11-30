package pl.agh.harmonytools.soprano.genetic

import pl.agh.harmonytools.algorithm.generator.LayerGenerator
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.solver.harmonics.generator.{ChordGenerator, ChordGeneratorInput}
import pl.agh.harmonytools.solver.soprano.generator.{HarmonicFunctionGenerator, HarmonicFunctionGeneratorInput}

import scala.collection.immutable.HashMap
import scala.collection.mutable

case class SopranoChordGenerator(
  allowedHarmonicFunctions: List[HarmonicFunction],
  key: Key
) extends LayerGenerator[Chord, HarmonicFunctionGeneratorInput] {
  private val harmonicFunctionGenerator = HarmonicFunctionGenerator(allowedHarmonicFunctions, key)
  private val chordGenerator            = ChordGenerator(key)
  private val map = new mutable.HashMap[HarmonicFunctionGeneratorInput, List[Chord]]()

  override def generate(input: HarmonicFunctionGeneratorInput): List[Chord] = {
    map.get(input) match {
      case Some(list) => list
      case None =>
        val harmonicFunctions = harmonicFunctionGenerator.generate(input)
        val chordGeneratorInputs = harmonicFunctions match {
          case Nil => List()
          case head :: Nil => List(head.toChordGeneratorInput(true))
          case head :: tail => head.toChordGeneratorInput(true) +: tail.map(_.toChordGeneratorInput())
        }
        val list = chordGeneratorInputs.map(chordGenerator.generate).reduceOption(_ ++ _).getOrElse(List())
        map.put(input, list)
        list
    }
  }
}
