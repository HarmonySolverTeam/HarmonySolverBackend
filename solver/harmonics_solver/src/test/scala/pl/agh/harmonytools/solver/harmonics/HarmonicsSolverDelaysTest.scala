package pl.agh.harmonytools.solver.harmonics

import org.scalatest.{Assertion, FunSuite, Matchers}
import pl.agh.harmonytools.exercise.harmonics.HarmonicsExercise
import pl.agh.harmonytools.exercise.harmonics.helpers.DelayHandler
import pl.agh.harmonytools.model.harmonicfunction.Delay
import pl.agh.harmonytools.model.measure.Measure
import pl.agh.harmonytools.model.util.ChordComponentManager

class HarmonicsSolverDelaysTest extends FunSuite with Matchers {
  private def getParserOutputWithHandledDelays(input: String): HarmonicsExercise = {
    val exercise = HarmonicsSolverTest.parseInput(input).get
    exercise.copy(
      measures = exercise.measures.map(m => Measure(m.meter, DelayHandler.handleDelays(m.contents)))
    )
  }

  private lazy val simpleDelayExercise = getParserOutputWithHandledDelays("""C
                                                           |3/4
                                                           |T{delay:4-3}
                                                           |""".stripMargin)

  test("Dividing function into two - delays") {
    simpleDelayExercise.measures.head.contents.size shouldBe 2
  }

  test("Transformation of first child function correctness - delays") {
    val first = simpleDelayExercise.measures.head.contents.head
    first.omit shouldBe Set(ChordComponentManager.chordComponentFromString("3"))
    first.extra shouldBe Set(ChordComponentManager.chordComponentFromString("4"))
    first.delay shouldBe Set(Delay("4", "3"))
  }

  test("Transformation of second child function correctness - delays") {
    val second = simpleDelayExercise.measures.head.contents(1)
    second.omit shouldBe Set()
    second.extra shouldBe Set()
    second.delay shouldBe Set()
  }

  test("Transformation with delay and fixed position") {
    val exercise = getParserOutputWithHandledDelays("""C
                                     |3/4
                                     |T{delay:4-3/position:3}
                                     |""".stripMargin)

    exercise.measures.head.contents.head.position shouldBe Some(
      ChordComponentManager.chordComponentFromString("4")
    )
    exercise.measures.head.contents(1).position shouldBe Some(
      ChordComponentManager.chordComponentFromString("3")
    )
  }

  test("Transformation with delay and fixed inversion") {
    val exercise = getParserOutputWithHandledDelays("""C
                                     |3/4
                                     |T{delay:4-3/inversion:3}
                                     |""".stripMargin)

    exercise.measures.head.contents.head.inversion shouldBe ChordComponentManager.chordComponentFromString(
      "4"
    )
    exercise.measures.head.contents(1).inversion shouldBe ChordComponentManager.chordComponentFromString("3")
  }

  test("More measures with delayed functions") {
    val exercise = getParserOutputWithHandledDelays("""C
                                     |3/4
                                     |T{delay:4-3} T{delay:4-3}
                                     |T{delay:4-3}
                                     |T{delay:4-3} T{delay:4-3}
                                     |""".stripMargin)
    exercise.measures.map(_.contents.length).sum shouldBe 10
  }

  test("Double delayed function transformation") {
    val exercise = getParserOutputWithHandledDelays("""C
                                     |3/4
                                     |T{delay:6-5,4-3}
                                     |""".stripMargin)
    val first    = exercise.measures.head.contents.head
    first.extra shouldBe Set(
      ChordComponentManager.chordComponentFromString("6"),
      ChordComponentManager.chordComponentFromString("4")
    )
    first.omit shouldBe Set(
      ChordComponentManager.chordComponentFromString("5"),
      ChordComponentManager.chordComponentFromString("3")
    )
  }

  test("Triple delayed function transformation") {
    val exercise = getParserOutputWithHandledDelays("""C
                                     |3/4
                                     |T{delay:6-5,4-3,2-1}
                                     |""".stripMargin)
    val first    = exercise.measures.head.contents.head
    first.extra shouldBe Set(
      ChordComponentManager.chordComponentFromString("6"),
      ChordComponentManager.chordComponentFromString("4"),
      ChordComponentManager.chordComponentFromString("2")
    )
    first.omit shouldBe Set(
      ChordComponentManager.chordComponentFromString("5"),
      ChordComponentManager.chordComponentFromString("3"),
      ChordComponentManager.chordComponentFromString("1")
    )
  }
}
