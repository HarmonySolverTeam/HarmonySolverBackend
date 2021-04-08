package pl.agh.harmonytools.bass

import org.scalatest.{FunSuite, Matchers}
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.key.Mode.{MAJOR, MINOR}
import pl.agh.harmonytools.model.measure.Meter
import pl.agh.harmonytools.model.note.BaseNote.{A, B, C, D, E, F, G}
import pl.agh.harmonytools.model.scale.ScaleDegree.V
import pl.agh.harmonytools.utils.TestUtils

class BassTranslatorEndToEndTest extends FunSuite with Matchers with TestUtils {

  import ChordComponents._

  test("Base major triad") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      FiguredBassExercise(Key("C"), Meter(4, 4), List(FiguredBassElement(NoteBuilder(48, C, 1))))
    )
    ex.measures.head.harmonicFunctions.head.revolution.chordComponentString shouldBe "1"
    ex.measures.head.harmonicFunctions.head.countChordComponents shouldBe 3
    ex.measures.head.harmonicFunctions.head.extra shouldBe Set.empty
    ex.measures.head.harmonicFunctions.head.omit shouldBe Set.empty
  }

  test("1st major triad inversion") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      FiguredBassExercise(
        Key("C"),
        Meter(4, 4),
        List(FiguredBassElement(NoteBuilder(52, E, 1), symbols = List(BassSymbol(6))))
      )
    )
    ex.measures.head.harmonicFunctions.head.revolution.chordComponentString shouldBe "3"
    ex.measures.head.harmonicFunctions.head.countChordComponents shouldBe 3
    ex.measures.head.harmonicFunctions.head.extra shouldBe Set.empty
    ex.measures.head.harmonicFunctions.head.omit shouldBe Set.empty
  }

  test("2nd major triad inversion") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      FiguredBassExercise(
        Key("C"),
        Meter(4, 4),
        List(FiguredBassElement(NoteBuilder(55, G, 1), symbols = List(BassSymbol(6), BassSymbol(4))))
      )
    )
    ex.measures.head.harmonicFunctions.head.revolution.chordComponentString shouldBe "5"
    ex.measures.head.harmonicFunctions.head.countChordComponents shouldBe 3
    ex.measures.head.harmonicFunctions.head.extra shouldBe Set.empty
    ex.measures.head.harmonicFunctions.head.omit shouldBe Set.empty
  }

  test("Base minor triad") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      FiguredBassExercise(Key("c"), Meter(4, 4), List(FiguredBassElement(NoteBuilder(48, C, 1))))
    )
    ex.measures.head.harmonicFunctions.head.revolution.chordComponentString shouldBe "1"
    ex.measures.head.harmonicFunctions.head.getThird.chordComponentString shouldBe "3>"
    ex.measures.head.harmonicFunctions.head.countChordComponents shouldBe 3
    ex.measures.head.harmonicFunctions.head.extra shouldBe Set.empty
    ex.measures.head.harmonicFunctions.head.omit shouldBe Set.empty
  }

  test("1st minor triad inversion") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      FiguredBassExercise(
        Key("c"),
        Meter(4, 4),
        List(FiguredBassElement(NoteBuilder(51, E, 1), symbols = List(BassSymbol(6))))
      )
    )
    ex.measures.head.harmonicFunctions.head.revolution.chordComponentString shouldBe "3>"
    ex.measures.head.harmonicFunctions.head.countChordComponents shouldBe 3
    ex.measures.head.harmonicFunctions.head.extra shouldBe Set.empty
    ex.measures.head.harmonicFunctions.head.omit shouldBe Set.empty
  }

  test("2nd minor triad inversion") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      FiguredBassExercise(
        Key("c"),
        Meter(4, 4),
        List(FiguredBassElement(NoteBuilder(55, G, 1), symbols = List(BassSymbol(6), BassSymbol(4))))
      )
    )
    ex.measures.head.harmonicFunctions.head.revolution.chordComponentString shouldBe "5"
    ex.measures.head.harmonicFunctions.head.countChordComponents shouldBe 3
    ex.measures.head.harmonicFunctions.head.extra shouldBe Set.empty
    ex.measures.head.harmonicFunctions.head.omit shouldBe Set.empty
  }

  test("Picardian triad") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      FiguredBassExercise(
        Key("c"),
        Meter(4, 4),
        List(FiguredBassElement(NoteBuilder(48, C, 1), symbols = List(BassSymbol(3, Some(AlterationType.NATURAL)))))
      )
    )
    ex.measures.head.harmonicFunctions.head.revolution.chordComponentString shouldBe "1"
    ex.measures.head.harmonicFunctions.head.getThird.chordComponentString shouldBe "3"
    ex.measures.head.harmonicFunctions.head.countChordComponents shouldBe 3
    ex.measures.head.harmonicFunctions.head.extra shouldBe Set.empty
    ex.measures.head.harmonicFunctions.head.omit shouldBe Set.empty
  }

  test("Base D7") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      FiguredBassExercise(
        Key("C"),
        Meter(4, 4),
        List(FiguredBassElement(NoteBuilder(55, G, 1), symbols = List(BassSymbol(7))))
      )
    )
    ex.measures.head.harmonicFunctions.head.revolution.chordComponentString shouldBe "1"
    ex.measures.head.harmonicFunctions.head.countChordComponents shouldBe 4
    ex.measures.head.harmonicFunctions.head.extra.map(_.chordComponentString) shouldBe Set("7")
    ex.measures.head.harmonicFunctions.head.omit.map(_.chordComponentString) shouldBe Set.empty
  }

  test("1st inversion D7") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      FiguredBassExercise(
        Key("C"),
        Meter(4, 4),
        List(FiguredBassElement(NoteBuilder(59, B, 1), symbols = List(BassSymbol(6), BassSymbol(5))))
      )
    )
    ex.measures.head.harmonicFunctions.head.revolution.chordComponentString shouldBe "3"
    ex.measures.head.harmonicFunctions.head.countChordComponents shouldBe 4
    ex.measures.head.harmonicFunctions.head.extra.map(_.chordComponentString) shouldBe Set("7")
    ex.measures.head.harmonicFunctions.head.omit.map(_.chordComponentString) shouldBe Set.empty
  }

  test("2nd inversion D7") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      FiguredBassExercise(
        Key("C"),
        Meter(4, 4),
        List(FiguredBassElement(NoteBuilder(50, D, 1), symbols = List(BassSymbol(4), BassSymbol(3))))
      )
    )
    ex.measures.head.harmonicFunctions.head.revolution.chordComponentString shouldBe "5"
    ex.measures.head.harmonicFunctions.head.countChordComponents shouldBe 4
    ex.measures.head.harmonicFunctions.head.extra.map(_.chordComponentString) shouldBe Set("7")
    ex.measures.head.harmonicFunctions.head.omit.map(_.chordComponentString) shouldBe Set.empty
  }

  test("3rd inversion D7") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      FiguredBassExercise(
        Key("C"),
        Meter(4, 4),
        List(FiguredBassElement(NoteBuilder(53, F, 1), symbols = List(BassSymbol(2))))
      )
    )
    ex.measures.head.harmonicFunctions.head.revolution.chordComponentString shouldBe "7"
    ex.measures.head.harmonicFunctions.head.countChordComponents shouldBe 4
    ex.measures.head.harmonicFunctions.head.extra.map(_.chordComponentString) shouldBe Set("7")
    ex.measures.head.harmonicFunctions.head.omit.map(_.chordComponentString) shouldBe Set.empty
  }

  test("Base D7 in minor key") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      FiguredBassExercise(
        Key("c"),
        Meter(4, 4),
        List(
          FiguredBassElement(
            NoteBuilder(55, G, 1),
            symbols = List(BassSymbol(7), BassSymbol(3, Some(AlterationType.NATURAL)))
          )
        )
      )
    )
    ex.measures.head.harmonicFunctions.head.revolution.chordComponentString shouldBe "1"
    ex.measures.head.harmonicFunctions.head.getThird.chordComponentString shouldBe "3"
    ex.measures.head.harmonicFunctions.head.countChordComponents shouldBe 4
    ex.measures.head.harmonicFunctions.head.extra.map(_.chordComponentString) shouldBe Set("7")
    ex.measures.head.harmonicFunctions.head.omit.map(_.chordComponentString) shouldBe Set.empty
  }

  test("1st inversion D7 in minor key") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      FiguredBassExercise(
        Key("c"),
        Meter(4, 4),
        List(FiguredBassElement(NoteBuilder(59, B, 1), symbols = List(BassSymbol(6), BassSymbol(5))))
      )
    )
    ex.measures.head.harmonicFunctions.head.revolution.chordComponentString shouldBe "3"
    ex.measures.head.harmonicFunctions.head.getThird.chordComponentString shouldBe "3"
    ex.measures.head.harmonicFunctions.head.countChordComponents shouldBe 4
    ex.measures.head.harmonicFunctions.head.extra.map(_.chordComponentString) shouldBe Set("7")
    ex.measures.head.harmonicFunctions.head.omit.map(_.chordComponentString) shouldBe Set.empty
  }

  test("2nd inversion D7 in minor key") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      FiguredBassExercise(
        Key("c"),
        Meter(4, 4),
        List(
          FiguredBassElement(
            NoteBuilder(50, D, 1),
            symbols = List(BassSymbol(6, Some(AlterationType.NATURAL)), BassSymbol(4), BassSymbol(3))
          )
        )
      )
    )
    ex.measures.head.harmonicFunctions.head.revolution.chordComponentString shouldBe "5"
    ex.measures.head.harmonicFunctions.head.getThird.chordComponentString shouldBe "3"
    ex.measures.head.harmonicFunctions.head.countChordComponents shouldBe 4
    ex.measures.head.harmonicFunctions.head.extra.map(_.chordComponentString) shouldBe Set("7")
    ex.measures.head.harmonicFunctions.head.omit.map(_.chordComponentString) shouldBe Set.empty
  }

  test("3rd inversion D7 in minor key") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      FiguredBassExercise(
        Key("c"),
        Meter(4, 4),
        List(
          FiguredBassElement(
            NoteBuilder(53, F, 1),
            symbols = List(BassSymbol(4, Some(AlterationType.NATURAL)), BassSymbol(2))
          )
        )
      )
    )
    ex.measures.head.harmonicFunctions.head.revolution.chordComponentString shouldBe "7"
    ex.measures.head.harmonicFunctions.head.countChordComponents shouldBe 4
    ex.measures.head.harmonicFunctions.head.extra.map(_.chordComponentString) shouldBe Set("7")
    ex.measures.head.harmonicFunctions.head.omit.map(_.chordComponentString) shouldBe Set.empty
  }

  test("Base minor with 7") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      FiguredBassExercise(
        Key("c"),
        Meter(4, 4),
        List(FiguredBassElement(NoteBuilder(55, G, 1), symbols = List(BassSymbol(7))))
      )
    )
    ex.measures.head.harmonicFunctions.head.revolution.chordComponentString shouldBe "1"
    ex.measures.head.harmonicFunctions.head.getThird.chordComponentString shouldBe "3>"
    ex.measures.head.harmonicFunctions.head.countChordComponents shouldBe 4
    ex.measures.head.harmonicFunctions.head.extra.map(_.chordComponentString) shouldBe Set("7")
    ex.measures.head.harmonicFunctions.head.omit.map(_.chordComponentString) shouldBe Set.empty
  }

  test("1st inversion minor with 7") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      FiguredBassExercise(
        Key("c"),
        Meter(4, 4),
        List(FiguredBassElement(NoteBuilder(58, B, 1), symbols = List(BassSymbol(6), BassSymbol(5))))
      )
    )
    ex.measures.head.harmonicFunctions.head.revolution.chordComponentString shouldBe "3>"
    ex.measures.head.harmonicFunctions.head.getThird.chordComponentString shouldBe "3>"
    ex.measures.head.harmonicFunctions.head.countChordComponents shouldBe 4
    ex.measures.head.harmonicFunctions.head.extra.map(_.chordComponentString) shouldBe Set("7")
    ex.measures.head.harmonicFunctions.head.omit.map(_.chordComponentString) shouldBe Set.empty
  }

  test("2nd inversion minor with 7") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      FiguredBassExercise(
        Key("c"),
        Meter(4, 4),
        List(FiguredBassElement(NoteBuilder(50, D, 1), symbols = List(BassSymbol(4), BassSymbol(3))))
      )
    )
    ex.measures.head.harmonicFunctions.head.revolution.chordComponentString shouldBe "5"
    ex.measures.head.harmonicFunctions.head.getThird.chordComponentString shouldBe "3>"
    ex.measures.head.harmonicFunctions.head.countChordComponents shouldBe 4
    ex.measures.head.harmonicFunctions.head.extra.map(_.chordComponentString) shouldBe Set("7")
    ex.measures.head.harmonicFunctions.head.omit.map(_.chordComponentString) shouldBe Set.empty
  }

  test("3rd inversion minor with 7") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      FiguredBassExercise(
        Key("c"),
        Meter(4, 4),
        List(FiguredBassElement(NoteBuilder(53, F, 1), symbols = List(BassSymbol(2))))
      )
    )
    ex.measures.head.harmonicFunctions.head.revolution.chordComponentString shouldBe "7"
    ex.measures.head.harmonicFunctions.head.getThird.chordComponentString shouldBe "3>"
    ex.measures.head.harmonicFunctions.head.countChordComponents shouldBe 4
    ex.measures.head.harmonicFunctions.head.extra.map(_.chordComponentString) shouldBe Set("7")
    ex.measures.head.harmonicFunctions.head.omit.map(_.chordComponentString) shouldBe Set.empty
  }

  test("4-3 delay") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      FiguredBassExercise(
        Key("C"),
        Meter(4, 4),
        List(FiguredBassElement(NoteBuilder(48, C, 1), delays = List(BassDelay(BassSymbol(4), BassSymbol(3)))))
      )
    )
    ex.measures.head.harmonicFunctions.head.delay.map(d =>
      d.first.chordComponentString + "-" + d.second.chordComponentString
    ) shouldBe Set("4-3")
  }

  test("3-3> delay") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      FiguredBassExercise(
        Key("C"),
        Meter(4, 4),
        List(
          FiguredBassElement(
            NoteBuilder(48, C, 1),
            delays = List(BassDelay(BassSymbol(3), BassSymbol(3, Some(AlterationType.FLAT))))
          )
        )
      )
    )
    ex.measures.head.harmonicFunctions.length shouldBe 2
    ex.measures.head.harmonicFunctions.head.mode shouldBe MAJOR
    ex.measures.head.harmonicFunctions.last.mode shouldBe MINOR
    ex.measures.head.harmonicFunctions.head.delay shouldBe Set.empty
    ex.measures.head.harmonicFunctions.last.delay shouldBe Set.empty
  }

  test("6-5, 4-3 delay") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      FiguredBassExercise(
        Key("C"),
        Meter(4, 4),
        List(
          FiguredBassElement(
            NoteBuilder(48, C, 1),
            delays = List(BassDelay(BassSymbol(6), BassSymbol(5)), BassDelay(BassSymbol(4), BassSymbol(3)))
          )
        )
      )
    )
    ex.measures.head.harmonicFunctions.length shouldBe 1
    ex.measures.head.harmonicFunctions.head.delay.map(d => d.first.chordComponentString + "-" + d.second.chordComponentString) shouldBe Set("6-5", "4-3")
  }

  test("altered VI") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      FiguredBassExercise(
        Key("e"),
        Meter(4, 4),
        List(
          FiguredBassElement(
            NoteBuilder(49, C, 1)
          )
        )
      )
    )

    ex.measures.head.harmonicFunctions.length shouldBe 1
    val hf = ex.measures.head.harmonicFunctions.head
    hf.revolution.chordComponentString shouldBe "1<"
    hf.extra.map(_.chordComponentString) shouldBe Set("1<")
    hf.omit.map(_.chordComponentString) shouldBe Set("1")
  }

  test("2 4# 10 symbols -> D9 on 7") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      FiguredBassExercise(
        Key("f#"),
        Meter(4, 4),
        List(
          FiguredBassElement(
            NoteBuilder(47, B, 1),
            symbols = List(
              BassSymbol(2),
              BassSymbol(4, Some(AlterationType.SHARP)),
              BassSymbol(10)
            )
          )
        )
      )
    )
    // b c# e# d
    ex.measures.head.harmonicFunctions.length shouldBe 1
    val hf = ex.measures.head.harmonicFunctions.head
    hf.degree shouldBe V
    hf.revolution shouldBe seventh
    hf.extra shouldBe Set(seventh, ninthDim)
    hf.omit shouldBe Set(fifth)
  }

  test("Dmaj on 5 Dmin on 5 delay") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      FiguredBassExercise(
        Key("G"),
        Meter(4, 4),
        List(
          FiguredBassElement(
            NoteBuilder(57, A, 1),
            symbols = List(
              BassSymbol(6),
              BassSymbol(4)
            ),
            delays = List(
              BassDelay(BassSymbol(6), BassSymbol(6, Some(AlterationType.FLAT)))
            )
          )
        )
      )
    )
    ex.measures.head.harmonicFunctions.length shouldBe 2
    ex.measures.head.harmonicFunctions.head.mode shouldBe MAJOR
    ex.measures.head.harmonicFunctions.last.mode shouldBe MINOR
  }

  test("SII (dim chord) in minor key - I inversion") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      FiguredBassExercise(
        Key("a"),
        Meter(4, 4),
        List(
          FiguredBassElement(
            NoteBuilder(50, D, 1),
            symbols = List(
              BassSymbol(6)
            )
          )
        )
      )
    )
    ex.measures.head.harmonicFunctions.length shouldBe 1
    val hf = ex.measures.head.harmonicFunctions.head
    hf.revolution shouldBe thirdDim
    hf.omit shouldBe Set()
    hf.extra shouldBe Set()
    hf.countChordComponents shouldBe 3
    hf.getFifth shouldBe fifthDim
  }
}
