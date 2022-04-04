package pl.agh.harmonytools.soprano.genetic

import pl.agh.harmonytools.algorithm.generator.LayerGenerator
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.note.BaseNote
import pl.agh.harmonytools.model.scale.{MajorScale, MinorScale}
import pl.agh.harmonytools.solver.harmonics.generator.ChordGenerator
import pl.agh.harmonytools.solver.soprano.generator.{HarmonicFunctionGenerator, HarmonicFunctionGeneratorInput}
import pl.agh.harmonytools.utils.Extensions.ExtendedInt
import pl.agh.harmonytools.utils.TestUtils

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
    import ChordComponents._
    import HarmonicFunctions._

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
      tonic.copy(omit = Set(fifth)),
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

    val scale = if (key.mode.isMajor) MajorScale else MinorScale
    val baseNotes = BaseNote.values.takeRight(BaseNote.values.size - BaseNote.values.indexOf(key.baseNote)) ++ BaseNote.values.take(BaseNote.values.indexOf(key.baseNote))
    val keys = scale.pitches.zip(baseNotes).map(x => Key(x._2, 60 + (x._1 + key.tonicPitch) %% 12)).drop(1)

    val modulations: List[HarmonicFunction] = keys.map(key => dominant.copy(key = Some(key)))

//    val possibleFunctions = baseFunctions ++ inversionFunctions ++ functionsWithExtra ++ omitFunctions ++ sideFunctions ++ modulations
    SopranoChordGenerator(baseFunctions/* ++ sideFunctions ++ modulations*/, key)
  }
}
