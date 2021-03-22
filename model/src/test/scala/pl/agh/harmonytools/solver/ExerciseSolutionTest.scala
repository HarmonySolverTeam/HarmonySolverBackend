package pl.agh.harmonytools.solver

import org.scalatest.{FunSuite, Matchers}
import pl.agh.harmonytools.harmonics.exercise.{Measure, Meter}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.exercise.Exercise
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.DOMINANT
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.utils.TestUtils

class ExerciseSolutionTest extends FunSuite with Matchers with TestUtils {
  import Keys._
  import HarmonicFunctions._
  test("Exact divide for hfs 1") {
    val exercise         = ExerciseImpl(keyC, Meter(3, 4), List(Measure(List(tonic, dominant, dominant))))
    val exerciseSolution = ExerciseSolution(exercise, 0, List(Chord.empty, Chord.empty, Chord.empty))
    exerciseSolution.setDurations()
    exerciseSolution.chords.map(_.duration.get.asDouble) shouldBe List(1.0 / 4, 1.0 / 4, 1.0 / 4)
  }

  test("Exact divide for hfs 2") {
    val exercise         = ExerciseImpl(keyC, Meter(4, 4), List(Measure(List(tonic, subdominant, dominant))))
    val exerciseSolution = ExerciseSolution(exercise, 0, List(Chord.empty, Chord.empty, Chord.empty))
    exerciseSolution.setDurations()
    exerciseSolution.chords.map(_.duration.get.asDouble) shouldBe List(2.0 / 4, 1.0 / 4, 1.0 / 4)
  }

  test("Exact divide for hfs 3") {
    val exercise = ExerciseImpl(
      keyC,
      Meter(4, 4),
      List(Measure(List(tonic, subdominant, subdominant, dominant, dominant, tonic, tonic, tonic, dominant)))
    )
    val exerciseSolution = ExerciseSolution(
      exercise,
      0,
      List(
        Chord.empty,
        Chord.empty,
        Chord.empty,
        Chord.empty,
        Chord.empty,
        Chord.empty,
        Chord.empty,
        Chord.empty,
        Chord.empty
      )
    )
    exerciseSolution.setDurations()
    exerciseSolution.chords.map(_.duration.get.asDouble) shouldBe List(1.0 / 4, 1.0 / 8, 1.0 / 8, 1.0 / 8, 1.0 / 8, 1.0 / 16, 1.0 / 32, 1.0 / 32, 1.0 / 8)
  }

  test("Exact divide for hfs 4") {
    val hfDeflection = HarmonicFunction(DOMINANT, key = Some(keyG))
    val exercise = ExerciseImpl(
      keyC,
      Meter(4, 4),
      List(Measure(List(hfDeflection, dominant, tonic, subdominant)))
    )
    val exerciseSolution = ExerciseSolution(
      exercise,
      0,
      List(
        Chord.empty,
        Chord.empty,
        Chord.empty,
        Chord.empty
      )
    )
    exerciseSolution.setDurations()
    exerciseSolution.chords.map(_.duration.get.asDouble) shouldBe List(1.0 / 4, 1.0 / 4, 1.0 / 4, 1.0 / 4)
  }

}

case class ExerciseImpl(key: Key, meter: Meter, measures: List[Measure]) extends Exercise(key, meter, measures)
