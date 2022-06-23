package pl.agh.harmonytools.solver.soprano.bayes

import org.scalatest.{FunSuite, Matchers}
import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.model.measure.{Measure, Meter}
import pl.agh.harmonytools.model.note.BaseNote.{A, B, C, D}
import pl.agh.harmonytools.model.note.NoteWithoutChordContext
import pl.agh.harmonytools.utils.TestUtils
import smile.License

class MarkovSopranoSolverTest extends FunSuite with Matchers with TestUtils {
  new License("SMILE LICENSE 6496c72d 3bb877f6 fb9bddc1 " + "THIS IS AN ACADEMIC LICENSE AND CAN BE USED " + "SOLELY FOR ACADEMIC RESEARCH AND TEACHING, " + "AS DEFINED IN THE BAYESFUSION ACADEMIC " + "SOFTWARE LICENSING AGREEMENT. " + "Serial #: 43an42u232fz4pgpvfc2vubye " + "Issued for: Jakub Sroka (jakubsroka3@gmail.com) " + "Academic institution: AGH the University of Science and Technology " + "Valid until: 2022-07-20 " + "Issued by BayesFusion activation server", Array[Byte](53, -13, -25, 86, -48, 63, 97, 86, -108, -13, 86, 101, 80, -63, -15, 7, -17, -33, -107, -77, -82, -81, -97, -121, -96, -72, -127, -121, 7, -95, 33, 39, -96, 74, -49, 101, 58, 55, 115, -94, 40, 0, -96, -72, 64, 112, 65, 69, -111, 26, -26, 55, -92, -68, -46, 124, 121, 99, -35, 48, 116, -60, 9, 119))

  import Keys._

  private val targosz_p59_ex1 = SopranoExercise(
    keyD,
    Meter(4, 4),
    List(
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(74, D, 0.5),
          NoteWithoutChordContext(69, A, 0.25),
          NoteWithoutChordContext(71, B, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(73, C, 0.5),
          NoteWithoutChordContext(74, D, 0.5)
        )
      )
    ),
    List()
  )

  test("Markov Soprano Solver test") {
    val solver = new MockMarkovSopranoSolver(targosz_p59_ex1)
    solver.solve().success shouldBe true
  }

  test("BayesNet Soprano Solver test") {
    val solver = new BayesNetSopranoSolver(targosz_p59_ex1)
    solver.solve().success shouldBe true
  }
}
