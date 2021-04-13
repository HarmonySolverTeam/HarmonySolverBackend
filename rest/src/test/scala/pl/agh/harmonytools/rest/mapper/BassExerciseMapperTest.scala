package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.bass.{
  AlterationType,
  BassDelay,
  BassSymbol,
  FiguredBassElement,
  FiguredBassExercise,
  NoteBuilder
}
import pl.agh.harmonytools.model.measure.Meter
import pl.agh.harmonytools.model.note.BaseNote
import pl.agh.harmonytools.rest.dto.{BassDelayDto, BassElementDto, BassExerciseDto, NoteDto}
import pl.agh.harmonytools.utils.TestUtils

class BassExerciseMapperTest
  extends MapperTest[FiguredBassExercise, BassExerciseDto](BassExerciseMapper)
  with TestUtils {
  override protected val models: List[FiguredBassExercise] = List(
    FiguredBassExercise(
      Keys.keyc,
      Meter(4, 4),
      List(
        FiguredBassElement(
          NoteBuilder(55, BaseNote.G),
          List(BassSymbol(7)),
          List(BassDelay(BassSymbol(4), BassSymbol(3, AlterationType.NATURAL)))
        ),
        FiguredBassElement(
          NoteBuilder(60, BaseNote.C)
        )
      )
    )
  )
  override protected val dtos: List[BassExerciseDto] = List(
    BassExerciseDto(
      "c",
      "4/4",
      List(
        BassElementDto(
          NoteDto(55, NoteDto.BaseNote.G, Some("1"), Some(0.0)),
          Some(List("7")),
          Some(List(BassDelayDto("4", "3h")))
        ),
        BassElementDto(
          NoteDto(60, NoteDto.BaseNote.C, Some("1"), Some(0.0)),
          Some(List()),
          Some(List())
        )
      )
    )
  )
}
