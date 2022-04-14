package pl.agh.harmonytools.finder

import pl.agh.harmonytools.model.harmonicfunction.BaseFunction.{DOMINANT, SUBDOMINANT, TONIC}
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.key.Mode.MINOR
import pl.agh.harmonytools.model.note.BaseNote
import pl.agh.harmonytools.model.scale.MinorScale
import pl.agh.harmonytools.model.scale.ScaleDegree.{II, III, VI, VII}
import pl.agh.harmonytools.utils.Extensions.ExtendedInt
import pl.agh.harmonytools.utils.TestUtils

class MinorFunctions(key: Key) extends Functions(key) with TestUtils {
  import ChordComponents._
  override protected val functions: List[HarmonicFunction] = {
    val baseFunctions = List(
      HarmonicFunction(TONIC),
      HarmonicFunction(TONIC, mode = MINOR),
      HarmonicFunction(SUBDOMINANT),
      HarmonicFunction(DOMINANT),
      HarmonicFunction(SUBDOMINANT, mode = MINOR),
      HarmonicFunction(DOMINANT, mode = MINOR)
    )

    val sideFunctions = List(
      HarmonicFunction(TONIC, degree = Some(VI), mode = MINOR),
      HarmonicFunction(SUBDOMINANT, degree = Some(VI), mode = MINOR),
      HarmonicFunction(DOMINANT, degree = Some(III), mode = MINOR),
      HarmonicFunction(TONIC, degree = Some(III), mode = MINOR),
      HarmonicFunction(SUBDOMINANT, degree = Some(II), mode = MINOR),
      HarmonicFunction(DOMINANT, degree = Some(VII), mode = MINOR)
    )

    val baseNotes = BaseNote.values.takeRight(
      BaseNote.values.size - BaseNote.values.indexOf(key.baseNote)
    ) ++ BaseNote.values.take(BaseNote.values.indexOf(key.baseNote))
    val keys = MinorScale.pitches.zip(baseNotes).map(x => Key(x._2, 60 + (x._1 + key.tonicPitch) %% 12)).drop(1)
    val modulations: List[HarmonicFunction] =
      keys.map(key => HarmonicFunction(DOMINANT, key = Some(key), extra = Set(seventh)))
    val sm = HarmonicFunction(SUBDOMINANT, mode = MINOR, key = Some(keys(2)))
    val s  = HarmonicFunction(SUBDOMINANT, key = Some(keys(2)))
    val subdominantToSubdominant: List[HarmonicFunction] = List(
      sm,
      sm.copy(inversion = thirdDim),
      sm.copy(inversion = fifth),
      s,
      s.copy(inversion = third),
      s.copy(inversion = fifth)
    )

    val quadruples = List(
      HarmonicFunction(SUBDOMINANT, extra = Set(sixth)),
      HarmonicFunction(DOMINANT, extra = Set(seventh)),
      HarmonicFunction(SUBDOMINANT, mode = MINOR, extra = Set(sixth)),
      HarmonicFunction(DOMINANT, mode = MINOR, extra = Set(seventh))
    ) ++ sideFunctions.filter(_.degree != II).map(_.copy(extra = Set(seventhMajor))) ++ modulations ++ sideFunctions
      .filter(_.degree == II)
      .map(_.copy(extra = Set(seventh)))

    val ninthDominants = List(
      HarmonicFunction(DOMINANT, extra = Set(seventh, ninthDim), omit = Set(fifth)),
      HarmonicFunction(DOMINANT, extra = Set(seventh, ninth), omit = Set(fifth)),
      HarmonicFunction(DOMINANT, extra = Set(seventh, ninthDim), omit = Set(prime), inversion = Some(fifth)),
      HarmonicFunction(DOMINANT, extra = Set(seventh, ninth), omit = Set(prime), inversion = Some(fifth))
    )

    val functions = baseFunctions ++ sideFunctions ++ quadruples

    val inversions =
      functions.map(f => f.copy(inversion = f.getThird)) ++ functions.map(f => f.copy(inversion = f.getFifth)) ++
        quadruples.map(f => f.copy(inversion = seventh)) ++ ninthDominants.map(f => f.copy(inversion = third))

    val omits =
      functions.map(f => f.copy(omit = Set(f.getFifth))) ++ inversions.map(f => f.copy(omit = Set(f.getPrime))) ++
        inversions.filter(f => f.inversion != f.getFifth).map(f => f.copy(omit = Set(f.getFifth)))

    functions ++ inversions ++ omits ++ subdominantToSubdominant ++ ninthDominants
  }
}
