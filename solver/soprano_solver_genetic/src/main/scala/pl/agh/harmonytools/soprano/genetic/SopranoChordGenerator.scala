package pl.agh.harmonytools.soprano.genetic

import pl.agh.harmonytools.algorithm.generator.LayerGenerator
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.harmonicfunction.{BaseFunction, HarmonicFunction}
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.util.ChordComponentManager
import pl.agh.harmonytools.solver.harmonics.generator.{ChordGenerator, ChordGeneratorInput}
import pl.agh.harmonytools.solver.soprano.generator.{HarmonicFunctionGenerator, HarmonicFunctionGeneratorInput}
import pl.agh.harmonytools.utils.TestUtils

import scala.collection.immutable.HashMap
import scala.collection.mutable

case class SopranoChordGenerator(
  allowedHarmonicFunctions: List[HarmonicFunction],
  key: Key
) extends LayerGenerator[Chord, HarmonicFunctionGeneratorInput] {
  private val harmonicFunctionGenerator = HarmonicFunctionGenerator(allowedHarmonicFunctions, key)
  private val chordGenerator            = ChordGenerator(key)
  private val map = new mutable.HashMap[HarmonicFunctionGeneratorInput, List[Chord]]()

  def getMapLengths: (Int, Int) = {
    (map.keys.size, map.values.map(_.size).sum)
  }

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

object SopranoChordGenerator extends TestUtils {
  def apply(key: Key): SopranoChordGenerator = {
    import HarmonicFunctions._
    import ChordComponents._

    val baseFunctions: List[HarmonicFunction] = List(
      tonic, subdominant, dominant
    )

    val inversionFunctions: List[HarmonicFunction] = baseFunctions.map {
      f => f.getBasicChordComponents.map(cc => f.copy(inversion = cc))
    }.reduce(_ ++ _)

    val functionsWithExtra: List[HarmonicFunction] = List(
      dominant7,
      dominantRev7,
      subdominant6
    )

    val omitFunctions: List[HarmonicFunction] = List(
      dominant7.copy(omit = Set(fifth)),
      dominant7.copy(omit = Set(prime))
    )

    val sideFunctions: List[HarmonicFunction] = List(
      subdominantII,
      tonicIII,
      dominantIII,
      tonicVI,
      subdominantVI,
      dominantVII
    )

    val possibleFunctions = baseFunctions ++ inversionFunctions ++ functionsWithExtra ++ omitFunctions ++ sideFunctions
    SopranoChordGenerator(possibleFunctions, key)
  }
}
