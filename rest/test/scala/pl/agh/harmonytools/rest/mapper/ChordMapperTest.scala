package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.chord.{Chord, ChordSystem}
import pl.agh.harmonytools.model.harmonicfunction.{Delay, FunctionNames, HarmonicFunction}
import pl.agh.harmonytools.model.note.{BaseNote, Note}
import pl.agh.harmonytools.rest.dto.{ChordDto, DelayDto, HarmonicFunctionDto, NoteDto}
import pl.agh.harmonytools.utils.TestUtils

class ChordMapperTest extends MapperTest[Chord, ChordDto](ChordMapper) with TestUtils {
  import ChordComponents._
  import Keys._

  override protected val models: List[Chord] = List(
    Chord(
      Note(70, BaseNote.B, seventh),
      Note(68, BaseNote.G, fifthAltUp),
      Note(65, BaseNote.F, fourth),
      Note(60, BaseNote.C, prime),
      HarmonicFunction(
        baseFunction = FunctionNames.DOMINANT,
        extra = Set(seventh, fifthAltUp),
        omit = Set(fifth),
        delay = Set(Delay(fourth, third)),
        position = Some(seventh),
        system = ChordSystem.CLOSE,
        key = Some(keyf)
      ),
      duration = 0.5
    )
  )
  override protected val dtos: List[ChordDto] = List(
    ChordDto(
      NoteDto(70, 6, Some(seventh.chordComponentString), Some(0.0)),
      NoteDto(68, 4, Some(fifthAltUp.chordComponentString), Some(0.0)),
      NoteDto(65, 3, Some(fourth.chordComponentString), Some(0.0)),
      NoteDto(60, 0, Some(prime.chordComponentString), Some(0.0)),
      HarmonicFunctionDto(
        functionName = HarmonicFunctionDto.FunctionName.D,
        extra = Some(List(seventh.chordComponentString, fifthAltUp.chordComponentString)),
        omit = Some(List(fifth.chordComponentString)),
        delays = Some(List(DelayDto(fourth.chordComponentString, third.chordComponentString))),
        revolution = Some(prime.chordComponentString),
        mode = Some(HarmonicFunctionDto.Mode.Major),
        isRelatedBackwards = Some(false),
        isDown = Some(false),
        degree = Some(HarmonicFunctionDto.Degree.V),
        position = Some(seventh.chordComponentString),
        system = Some(HarmonicFunctionDto.System.Close),
        key = Some(keyf.toString)
      ),
      0.5
    )
  )
}
