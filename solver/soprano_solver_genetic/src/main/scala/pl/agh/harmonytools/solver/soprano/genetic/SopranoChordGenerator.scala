package pl.agh.harmonytools.solver.soprano.genetic

import pl.agh.harmonytools.algorithm.generator.LayerGenerator
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Mode.MINOR
import pl.agh.harmonytools.model.key.{Key, Mode}
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

  def getMapSize: (Int, Int) = {
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
  import ChordComponents._
  import HarmonicFunctions._

  def apply(key: Key): SopranoChordGenerator = {
    key.mode match {
      case Mode.MAJOR => initializeMajor(key)
      case Mode.MINOR => initializeMinor(key)
    }
  }

  private def initializeMajor(key: Key): SopranoChordGenerator = {
    assert(key.mode.isMajor)

    val baseFunctions: List[HarmonicFunction] = List(
      tonic, subdominant, dominant, subdominant.copy(mode = Mode.MINOR)
    )

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

    val inversionFunctions: List[HarmonicFunction] = (baseFunctions ++ sideFunctions).map {
      f => f.getBasicChordComponents.map(cc => f.copy(inversion = cc))
    }.reduce(_ ++ _)


    val baseNotes = BaseNote.values.takeRight(BaseNote.values.size - BaseNote.values.indexOf(key.baseNote)) ++ BaseNote.values.take(BaseNote.values.indexOf(key.baseNote))
    val keys = MajorScale.pitches.zip(baseNotes).map(x => Key(x._2, 60 + (x._1 + key.tonicPitch) %% 12)).drop(1)

    val modulations: List[HarmonicFunction] = keys.map(key => dominant.copy(key = Some(key), extra = Set(seventh), omit = Set(fifth)))// ++
//      keys.map(key => dominant.copy(key = Some(key), extra = Set(seventh), omit = Set(prime), inversion = fifth)) ++
//      keys.map(key => dominant.copy(key = Some(key), extra = Set(seventh), omit = Set(prime), inversion = third))

    // funkcje harmoniczne jakich uzywamy do harmonizacji

    val possibleFunctions = baseFunctions ++ inversionFunctions ++ functionsWithExtra ++ omitFunctions ++ sideFunctions ++ modulations
    SopranoChordGenerator(possibleFunctions, key)
  }

  private def initializeMinor(key: Key): SopranoChordGenerator = {
    assert(key.mode.isMinor)

    val baseFunctions: List[HarmonicFunction] = List(
      tonic.copy(mode = Mode.MINOR), subdominant.copy(mode = Mode.MINOR), subdominant.copy(mode = Mode.MAJOR), dominant
    )

    val inversionFunctions: List[HarmonicFunction] = baseFunctions.map {
      f => f.getBasicChordComponents.map(cc => f.copy(inversion = cc))
    }.reduce(_ ++ _)

    val functionsWithExtra: List[HarmonicFunction] = List(
      dominant7,
      dominantRev7,
      subdominant6.copy(mode = Mode.MINOR)
    )

    val omitFunctions: List[HarmonicFunction] = List(
      tonic.copy(omit = Set(fifth), mode = Mode.MINOR),
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
    ).map(_.copy(mode = Mode.MINOR))

    val apparentDominants = List(
      tonic.copy(mode = Mode.MINOR, extra = Set(seventhMajor))
    )

    val baseNotes = BaseNote.values.takeRight(BaseNote.values.size - BaseNote.values.indexOf(key.baseNote)) ++ BaseNote.values.take(BaseNote.values.indexOf(key.baseNote))
    val keys = MinorScale.pitches.zip(baseNotes).map(x => Key(x._2, 60 + (x._1 + key.tonicPitch) %% 12)).drop(1)

    val modulations: List[HarmonicFunction] = keys.map(key => dominant.copy(key = Some(key), extra = Set(seventh), omit = Set(fifth)))

    // funkcje harmoniczne jakich uzywamy do harmonizacji

    val possibleFunctions = baseFunctions ++ inversionFunctions ++ functionsWithExtra ++ omitFunctions ++ sideFunctions ++ modulations ++ apparentDominants
    SopranoChordGenerator(possibleFunctions, key)
  }
}
