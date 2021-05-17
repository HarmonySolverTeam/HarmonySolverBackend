package pl.agh.harmonytools.bass

import org.scalatest.{FunSuite, Matchers}
import pl.agh.harmonytools.bass
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.key.Mode.{MAJOR, MINOR}
import pl.agh.harmonytools.model.measure.{Measure, Meter}
import pl.agh.harmonytools.model.note.BaseNote._
import pl.agh.harmonytools.model.scale.ScaleDegree.V
import pl.agh.harmonytools.utils.TestUtils

class BassTranslatorEndToEndTest extends FunSuite with Matchers with TestUtils {

  import ChordComponents._

  test("Base major triad") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      bass.FiguredBassExercise(Key("C"), Meter(4, 4), Measure(Meter(4, 4), List(FiguredBassElement(NoteBuilder(48, C, 1)))))
    )
    ex.measures.head.contents.head.inversion.chordComponentString shouldBe "1"
    ex.measures.head.contents.head.countChordComponents shouldBe 3
    ex.measures.head.contents.head.extra shouldBe Set.empty
    ex.measures.head.contents.head.omit shouldBe Set.empty
  }

  test("1st major triad inversion") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      bass.FiguredBassExercise(
        Key("C"),
        Meter(4, 4),
        Measure(Meter(4, 4), List(FiguredBassElement(NoteBuilder(52, E, 1), symbols = List(BassSymbol(6)))))
      )
    )
    ex.measures.head.contents.head.inversion.chordComponentString shouldBe "3"
    ex.measures.head.contents.head.countChordComponents shouldBe 3
    ex.measures.head.contents.head.extra shouldBe Set.empty
    ex.measures.head.contents.head.omit shouldBe Set.empty
  }

  test("2nd major triad inversion") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      bass.FiguredBassExercise(
        Key("C"),
        Meter(4, 4),
        Measure(
          Meter(4, 4),
          List(FiguredBassElement(NoteBuilder(55, G, 1), symbols = List(BassSymbol(6), BassSymbol(4))))
        )
      )
    )
    ex.measures.head.contents.head.inversion.chordComponentString shouldBe "5"
    ex.measures.head.contents.head.countChordComponents shouldBe 3
    ex.measures.head.contents.head.extra shouldBe Set.empty
    ex.measures.head.contents.head.omit shouldBe Set.empty
  }

  test("Base minor triad") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      bass.FiguredBassExercise(Key("c"), Meter(4, 4), Measure(Meter(4, 4), List(FiguredBassElement(NoteBuilder(48, C, 1)))))
    )
    ex.measures.head.contents.head.inversion.chordComponentString shouldBe "1"
    ex.measures.head.contents.head.getThird.chordComponentString shouldBe "3>"
    ex.measures.head.contents.head.countChordComponents shouldBe 3
    ex.measures.head.contents.head.extra shouldBe Set.empty
    ex.measures.head.contents.head.omit shouldBe Set.empty
  }

  test("1st minor triad inversion") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      bass.FiguredBassExercise(
        Key("c"),
        Meter(4, 4),
        Measure(Meter(4, 4), List(FiguredBassElement(NoteBuilder(51, E, 1), symbols = List(BassSymbol(6)))))
      )
    )
    ex.measures.head.contents.head.inversion.chordComponentString shouldBe "3>"
    ex.measures.head.contents.head.countChordComponents shouldBe 3
    ex.measures.head.contents.head.extra shouldBe Set.empty
    ex.measures.head.contents.head.omit shouldBe Set.empty
  }

  test("2nd minor triad inversion") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      bass.FiguredBassExercise(
        Key("c"),
        Meter(4, 4),
        Measure(
          Meter(4, 4),
          List(FiguredBassElement(NoteBuilder(55, G, 1), symbols = List(BassSymbol(6), BassSymbol(4))))
        )
      )
    )
    ex.measures.head.contents.head.inversion.chordComponentString shouldBe "5"
    ex.measures.head.contents.head.countChordComponents shouldBe 3
    ex.measures.head.contents.head.extra shouldBe Set.empty
    ex.measures.head.contents.head.omit shouldBe Set.empty
  }

  test("Picardian triad") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      bass.FiguredBassExercise(
        Key("c"),
        Meter(4, 4),
        Measure(
          Meter(4, 4),
          List(FiguredBassElement(NoteBuilder(48, C, 1), symbols = List(bass.BassSymbol(3, AlterationType.NATURAL))))
        )
      )
    )
    ex.measures.head.contents.head.inversion.chordComponentString shouldBe "1"
    ex.measures.head.contents.head.getThird.chordComponentString shouldBe "3"
    ex.measures.head.contents.head.countChordComponents shouldBe 3
    ex.measures.head.contents.head.extra shouldBe Set.empty
    ex.measures.head.contents.head.omit shouldBe Set.empty
  }

  test("Base D7") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      bass.FiguredBassExercise(
        Key("C"),
        Meter(4, 4),
        Measure(Meter(4, 4), List(FiguredBassElement(NoteBuilder(55, G, 1), symbols = List(BassSymbol(7)))))
      )
    )
    ex.measures.head.contents.head.inversion.chordComponentString shouldBe "1"
    ex.measures.head.contents.head.countChordComponents shouldBe 4
    ex.measures.head.contents.head.extra.map(_.chordComponentString) shouldBe Set("7")
    ex.measures.head.contents.head.omit.map(_.chordComponentString) shouldBe Set.empty
  }

  test("1st inversion D7") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      bass.FiguredBassExercise(
        Key("C"),
        Meter(4, 4),
        Measure(
          Meter(4, 4),
          List(FiguredBassElement(NoteBuilder(59, B, 1), symbols = List(BassSymbol(6), BassSymbol(5))))
        )
      )
    )
    ex.measures.head.contents.head.inversion.chordComponentString shouldBe "3"
    ex.measures.head.contents.head.countChordComponents shouldBe 4
    ex.measures.head.contents.head.extra.map(_.chordComponentString) shouldBe Set("7")
    ex.measures.head.contents.head.omit.map(_.chordComponentString) shouldBe Set.empty
  }

  test("2nd inversion D7") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      bass.FiguredBassExercise(
        Key("C"),
        Meter(4, 4),
        Measure(
          Meter(4, 4),
          List(FiguredBassElement(NoteBuilder(50, D, 1), symbols = List(BassSymbol(4), BassSymbol(3))))
        )
      )
    )
    ex.measures.head.contents.head.inversion.chordComponentString shouldBe "5"
    ex.measures.head.contents.head.countChordComponents shouldBe 4
    ex.measures.head.contents.head.extra.map(_.chordComponentString) shouldBe Set("7")
    ex.measures.head.contents.head.omit.map(_.chordComponentString) shouldBe Set.empty
  }

  test("3rd inversion D7") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      bass.FiguredBassExercise(
        Key("C"),
        Meter(4, 4),
        Measure(Meter(4, 4), List(FiguredBassElement(NoteBuilder(53, F, 1), symbols = List(BassSymbol(2)))))
      )
    )
    ex.measures.head.contents.head.inversion.chordComponentString shouldBe "7"
    ex.measures.head.contents.head.countChordComponents shouldBe 4
    ex.measures.head.contents.head.extra.map(_.chordComponentString) shouldBe Set("7")
    ex.measures.head.contents.head.omit.map(_.chordComponentString) shouldBe Set.empty
  }

  test("Base D7 in minor key") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      bass.FiguredBassExercise(
        Key("c"),
        Meter(4, 4),
        Measure(
          Meter(4, 4),
          List(
            FiguredBassElement(
              NoteBuilder(55, G, 1),
              symbols = List(BassSymbol(7), bass.BassSymbol(3, AlterationType.NATURAL))
            )
          )
        )
      )
    )
    ex.measures.head.contents.head.inversion.chordComponentString shouldBe "1"
    ex.measures.head.contents.head.getThird.chordComponentString shouldBe "3"
    ex.measures.head.contents.head.countChordComponents shouldBe 4
    ex.measures.head.contents.head.extra.map(_.chordComponentString) shouldBe Set("7")
    ex.measures.head.contents.head.omit.map(_.chordComponentString) shouldBe Set.empty
  }

  test("1st inversion D7 in minor key") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      bass.FiguredBassExercise(
        Key("c"),
        Meter(4, 4),
        Measure(
          Meter(4, 4),
          List(FiguredBassElement(NoteBuilder(59, B, 1), symbols = List(BassSymbol(6), BassSymbol(5))))
        )
      )
    )
    ex.measures.head.contents.head.inversion.chordComponentString shouldBe "3"
    ex.measures.head.contents.head.getThird.chordComponentString shouldBe "3"
    ex.measures.head.contents.head.countChordComponents shouldBe 4
    ex.measures.head.contents.head.extra.map(_.chordComponentString) shouldBe Set("7")
    ex.measures.head.contents.head.omit.map(_.chordComponentString) shouldBe Set.empty
  }

  test("2nd inversion D7 in minor key") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      bass.FiguredBassExercise(
        Key("c"),
        Meter(4, 4),
        Measure(
          Meter(4, 4),
          List(
            FiguredBassElement(
              NoteBuilder(50, D, 1),
              symbols = List(bass.BassSymbol(6, AlterationType.NATURAL), BassSymbol(4), BassSymbol(3))
            )
          )
        )
      )
    )
    ex.measures.head.contents.head.inversion.chordComponentString shouldBe "5"
    ex.measures.head.contents.head.getThird.chordComponentString shouldBe "3"
    ex.measures.head.contents.head.countChordComponents shouldBe 4
    ex.measures.head.contents.head.extra.map(_.chordComponentString) shouldBe Set("7")
    ex.measures.head.contents.head.omit.map(_.chordComponentString) shouldBe Set.empty
  }

  test("3rd inversion D7 in minor key") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      bass.FiguredBassExercise(
        Key("c"),
        Meter(4, 4),
        Measure(
          Meter(4, 4),
          List(
            FiguredBassElement(
              NoteBuilder(53, F, 1),
              symbols = List(bass.BassSymbol(4, AlterationType.NATURAL), BassSymbol(2))
            )
          )
        )
      )
    )
    ex.measures.head.contents.head.inversion.chordComponentString shouldBe "7"
    ex.measures.head.contents.head.countChordComponents shouldBe 4
    ex.measures.head.contents.head.extra.map(_.chordComponentString) shouldBe Set("7")
    ex.measures.head.contents.head.omit.map(_.chordComponentString) shouldBe Set.empty
  }

  test("Base minor with 7") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      bass.FiguredBassExercise(
        Key("c"),
        Meter(4, 4),
        Measure(Meter(4, 4), List(FiguredBassElement(NoteBuilder(55, G, 1), symbols = List(BassSymbol(7)))))
      )
    )
    ex.measures.head.contents.head.inversion.chordComponentString shouldBe "1"
    ex.measures.head.contents.head.getThird.chordComponentString shouldBe "3>"
    ex.measures.head.contents.head.countChordComponents shouldBe 4
    ex.measures.head.contents.head.extra.map(_.chordComponentString) shouldBe Set("7")
    ex.measures.head.contents.head.omit.map(_.chordComponentString) shouldBe Set.empty
  }

  test("1st inversion minor with 7") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      bass.FiguredBassExercise(
        Key("c"),
        Meter(4, 4),
        Measure(
          Meter(4, 4),
          List(FiguredBassElement(NoteBuilder(58, B, 1), symbols = List(BassSymbol(6), BassSymbol(5))))
        )
      )
    )
    ex.measures.head.contents.head.inversion.chordComponentString shouldBe "3>"
    ex.measures.head.contents.head.getThird.chordComponentString shouldBe "3>"
    ex.measures.head.contents.head.countChordComponents shouldBe 4
    ex.measures.head.contents.head.extra.map(_.chordComponentString) shouldBe Set("7")
    ex.measures.head.contents.head.omit.map(_.chordComponentString) shouldBe Set.empty
  }

  test("2nd inversion minor with 7") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      bass.FiguredBassExercise(
        Key("c"),
        Meter(4, 4),
        Measure(
          Meter(4, 4),
          List(FiguredBassElement(NoteBuilder(50, D, 1), symbols = List(BassSymbol(4), BassSymbol(3))))
        )
      )
    )
    ex.measures.head.contents.head.inversion.chordComponentString shouldBe "5"
    ex.measures.head.contents.head.getThird.chordComponentString shouldBe "3>"
    ex.measures.head.contents.head.countChordComponents shouldBe 4
    ex.measures.head.contents.head.extra.map(_.chordComponentString) shouldBe Set("7")
    ex.measures.head.contents.head.omit.map(_.chordComponentString) shouldBe Set.empty
  }

  test("3rd inversion minor with 7") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      bass.FiguredBassExercise(
        Key("c"),
        Meter(4, 4),
        Measure(Meter(4, 4), List(FiguredBassElement(NoteBuilder(53, F, 1), symbols = List(BassSymbol(2)))))
      )
    )
    ex.measures.head.contents.head.inversion.chordComponentString shouldBe "7"
    ex.measures.head.contents.head.getThird.chordComponentString shouldBe "3>"
    ex.measures.head.contents.head.countChordComponents shouldBe 4
    ex.measures.head.contents.head.extra.map(_.chordComponentString) shouldBe Set("7")
    ex.measures.head.contents.head.omit.map(_.chordComponentString) shouldBe Set.empty
  }

  test("4-3 delay") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      bass.FiguredBassExercise(
        Key("C"),
        Meter(4, 4),
        Measure(
          Meter(4, 4),
          List(FiguredBassElement(NoteBuilder(48, C, 1), delays = List(BassDelay(BassSymbol(4), BassSymbol(3)))))
        )
      )
    )
    ex.measures.head.contents.head.delay.map(d =>
      d.first.chordComponentString + "-" + d.second.chordComponentString
    ) shouldBe Set("4-3")
  }

  test("3-3> delay") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      bass.FiguredBassExercise(
        Key("C"),
        Meter(4, 4),
        Measure(
          Meter(4, 4),
          List(
            FiguredBassElement(
              NoteBuilder(48, C, 1),
              delays = List(BassDelay(BassSymbol(3), bass.BassSymbol(3, AlterationType.FLAT)))
            )
          )
        )
      )
    )
    ex.measures.head.contents.length shouldBe 2
    ex.measures.head.contents.head.mode shouldBe MAJOR
    ex.measures.head.contents.last.mode shouldBe MINOR
    ex.measures.head.contents.head.delay shouldBe Set.empty
    ex.measures.head.contents.last.delay shouldBe Set.empty
  }

  test("6-5, 4-3 delay") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      bass.FiguredBassExercise(
        Key("C"),
        Meter(4, 4),
        Measure(
          Meter(4, 4),
          List(
            FiguredBassElement(
              NoteBuilder(48, C, 1),
              delays = List(BassDelay(BassSymbol(6), BassSymbol(5)), BassDelay(BassSymbol(4), BassSymbol(3)))
            )
          )
        )
      )
    )
    ex.measures.head.contents.length shouldBe 1
    ex.measures.head.contents.head.delay.map(d =>
      d.first.chordComponentString + "-" + d.second.chordComponentString
    ) shouldBe Set("6-5", "4-3")
  }

  test("altered VI") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      bass.FiguredBassExercise(
        Key("e"),
        Meter(4, 4),
        Measure(
          Meter(4, 4),
          List(
            FiguredBassElement(
              NoteBuilder(49, C, 1)
            )
          )
        )
      )
    )

    ex.measures.head.contents.length shouldBe 1
    val hf = ex.measures.head.contents.head
    hf.inversion.chordComponentString shouldBe "1<"
    hf.extra.map(_.chordComponentString) shouldBe Set("1<")
    hf.omit.map(_.chordComponentString) shouldBe Set("1")
  }

  test("2 4# 10 symbols -> D9 on 7") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      bass.FiguredBassExercise(
        Key("f#"),
        Meter(4, 4),
        Measure(
          Meter(4, 4),
          List(
            FiguredBassElement(
              NoteBuilder(47, B, 1),
              symbols = List(
                BassSymbol(2),
                bass.BassSymbol(4, AlterationType.SHARP),
                BassSymbol(10)
              )
            )
          )
        )
      )
    )
    // b c# e# d
    ex.measures.head.contents.length shouldBe 1
    val hf = ex.measures.head.contents.head
    hf.degree shouldBe V
    hf.inversion shouldBe seventh
    hf.extra shouldBe Set(seventh, ninthDim)
    hf.omit shouldBe Set(fifth)
  }

  test("Dmaj on 5 Dmin on 5 delay") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      bass.FiguredBassExercise(
        Key("G"),
        Meter(4, 4),
        Measure(
          Meter(4, 4),
          List(
            FiguredBassElement(
              NoteBuilder(57, A, 1),
              symbols = List(
                BassSymbol(6),
                BassSymbol(4)
              ),
              delays = List(
                BassDelay(BassSymbol(6), bass.BassSymbol(6, AlterationType.FLAT))
              )
            )
          )
        )
      )
    )
    ex.measures.head.contents.length shouldBe 2
    ex.measures.head.contents.head.mode shouldBe MAJOR
    ex.measures.head.contents.last.mode shouldBe MINOR
  }

  test("SII (dim chord) in minor key - I inversion") {
    val ex = BassTranslator.createExerciseFromFiguredBass(
      bass.FiguredBassExercise(
        Key("a"),
        Meter(4, 4),
        Measure(
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
    )
    ex.measures.head.contents.length shouldBe 1
    val hf = ex.measures.head.contents.head
    hf.inversion shouldBe thirdDim
    hf.omit shouldBe Set()
    hf.extra shouldBe Set()
    hf.countChordComponents shouldBe 3
    hf.getFifth shouldBe fifthDim
  }
}
