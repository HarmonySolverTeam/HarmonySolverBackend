package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.harmonicfunction.Delay
import pl.agh.harmonytools.rest.dto.DelayDto
import pl.agh.harmonytools.utils.TestUtils

class DelayMapperTest extends MapperTest[Delay, DelayDto](DelayMapper()) with TestUtils {
  import ChordComponents._

  override protected val models: List[Delay] = List(
    Delay(seventh, eighth),
    Delay(fifthDim, fourth),
    Delay(fifthAltUp, sixth),
    Delay(sixthDim, fifthAltUp)
  )
  override protected val dtos: List[DelayDto] = List(
    DelayDto(seventh.chordComponentString, eighth.chordComponentString),
    DelayDto(fifthDim.chordComponentString, fourth.chordComponentString),
    DelayDto(fifthAltUp.chordComponentString, sixth.chordComponentString),
    DelayDto(sixthDim.chordComponentString, fifthAltUp.chordComponentString)
  )
}
