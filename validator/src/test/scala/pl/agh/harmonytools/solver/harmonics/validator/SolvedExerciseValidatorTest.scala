package pl.agh.harmonytools.solver.harmonics.validator

import org.scalatest.{FunSuite, Matchers}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.note.BaseNote.{A, C, E, F, G}
import pl.agh.harmonytools.model.note.Note
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard.ParallelFifthsRule
import pl.agh.harmonytools.utils.TestUtils

class SolvedExerciseValidatorTest extends FunSuite with Matchers with TestUtils {
  import ChordComponents._
  import HarmonicFunctions._
  private val ch1  = Chord(Note(72, C, prime), Note(67, G, fifth), Note(64, E, third), Note(48, C, prime), tonic)
  private val ch2  = Chord(Note(76, E, third), Note(67, G, fifth), Note(60, C, prime), Note(60, C, prime), tonic)
  private val ch3  = Chord(Note(81, A, third), Note(72, C, fifth), Note(60, C, fifth), Note(53, F, prime), subdominant)

  test("Validator test - not broken parallel fifths rule") {
    SolvedExerciseValidator.checkCorrectness(List(ch1, ch2)) shouldBe List()
  }

  test("Validator test - broken parallel fifths rule") {
    SolvedExerciseValidator.checkCorrectness(List(ch1, ch3)) shouldBe List((1, List(ParallelFifthsRule())))
  }
}
