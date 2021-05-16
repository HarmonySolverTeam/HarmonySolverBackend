package pl.agh.harmonytools.solver

import org.scalatest.{FunSuite, Matchers}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.exercise.Exercise
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.DOMINANT
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.measure.{Measure, Meter}
import pl.agh.harmonytools.utils.TestUtils

class ExerciseSolutionTest extends FunSuite with Matchers with TestUtils {
  import Keys._
  import HarmonicFunctions._
  test("Exact divide for hfs 1") {
    val exercise         = ExerciseImpl(keyC, Meter(3, 4), List(Measure(List(tonic, dominant, dominant))))
    val exerciseSolution = HarmonicsSolution(exercise, 0, List(Chord.empty, Chord.empty, Chord.empty))
    exerciseSolution.setDurations()
    exerciseSolution.chords.map(_.duration) shouldBe List(0.25, 0.25, 0.25)
  }

  test("Exact divide for hfs 2") {
    val exercise         = ExerciseImpl(keyC, Meter(4, 4), List(Measure(List(tonic, subdominant, dominant))))
    val exerciseSolution = HarmonicsSolution(exercise, 0, List(Chord.empty, Chord.empty, Chord.empty))
    exerciseSolution.setDurations()
    exerciseSolution.chords.map(_.duration) shouldBe List(0.5, 0.25, 0.25)
  }

  test("Exact divide for hfs 3") {
    val exercise = ExerciseImpl(
      keyC,
      Meter(4, 4),
      List(Measure(List(tonic, subdominant, subdominant, dominant, dominant, tonic, tonic, tonic, dominant)))
    )
    val exerciseSolution = HarmonicsSolution(
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
    exerciseSolution.chords.map(_.duration) shouldBe List(0.25, 0.125, 0.125, 0.125, 0.125, 0.0625, 0.03125, 0.03125,
      0.125)
  }

  test("Exact divide for hfs 4") {
    val hfDeflection = HarmonicFunction(DOMINANT, key = Some(keyG))
    val exercise = ExerciseImpl(
      keyC,
      Meter(4, 4),
      List(Measure(List(hfDeflection, dominant, tonic, subdominant)))
    )
    val exerciseSolution = HarmonicsSolution(
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
    exerciseSolution.chords.map(_.duration) shouldBe List(0.25, 0.25, 0.25, 0.25)
  }

}

case class ExerciseImpl(key: Key, meter: Meter, measures: List[Measure[HarmonicFunction]]) extends Exercise(key, meter, measures)
