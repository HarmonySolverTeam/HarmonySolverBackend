package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.bass
import pl.agh.harmonytools.bass._
import pl.agh.harmonytools.model.measure.{Measure, Meter}
import pl.agh.harmonytools.model.note.BaseNote
import pl.agh.harmonytools.rest.dto.{BassElementDto, BassExerciseDto, BassSymbolDto, NoteDto}
import pl.agh.harmonytools.utils.TestUtils

class BassExerciseMapperTest
  extends MapperTest[FiguredBassExercise, BassExerciseDto](BassExerciseMapper)
  with TestUtils {
  override protected val models: List[FiguredBassExercise] = List(
    bass.FiguredBassExercise(
      Keys.keyc,
      Meter(4, 4),
      Measure(
        Meter(4, 4),
        List(
          FiguredBassElement(
            NoteBuilder(55, BaseNote.G, 0.0, Some("1")),
            List(BassSymbol(7)),
            List(BassDelay(BassSymbol(4), bass.BassSymbol(3, AlterationType.NATURAL)))
          ),
          FiguredBassElement(
            NoteBuilder(60, BaseNote.C, 0.0, Some("1"))
          )
        )
      )
    )
  )
  override protected val dtos: List[BassExerciseDto] = List(
    BassExerciseDto(
      "c",
      List(4, 4),
      List(
        BassElementDto(
          NoteDto(55, 4, Some("1"), Some(0.0)),
          Some(List(BassSymbolDto(Some(7), None))),
          Some(List(List("4", "3h")))
        ),
        BassElementDto(
          NoteDto(60, 0, Some("1"), Some(0.0)),
          Some(List()),
          Some(List())
        )
      ),
      evaluateWithProlog = Some(false)
    )
  )
}
