package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.chord.ChordSystem
import pl.agh.harmonytools.model.harmonicfunction.{Delay, FunctionNames, HarmonicFunction}
import pl.agh.harmonytools.model.scale.ScaleDegree
import pl.agh.harmonytools.rest.dto.{DelayDto, HarmonicFunctionDto}
import pl.agh.harmonytools.utils.TestUtils

class HarmonicFunctionMapperTest
  extends MapperTest[HarmonicFunction, HarmonicFunctionDto](HarmonicFunctionMapper)
  with TestUtils {
  import Keys._
  import ChordComponents._

  override protected val models: List[HarmonicFunction] = List(
    HarmonicFunction(
      baseFunction = FunctionNames.DOMINANT,
      extra = Set(seventhD, fifthAltUpD),
      omit = Set(fifthD),
      delay = Set(Delay(fourthD, thirdD)),
      position = Some(seventhD),
      system = ChordSystem.CLOSE,
      key = Some(keyf),
      isRelatedBackwards = true,
      isDown = true,
      degree = Some(ScaleDegree.VII)
    )
  )
  override protected val dtos: List[HarmonicFunctionDto] = List(
    HarmonicFunctionDto(
      functionName = HarmonicFunctionDto.FunctionName.D,
      extra = Some(List(seventh.chordComponentString, fifthAltUp.chordComponentString)),
      omit = Some(List(fifth.chordComponentString)),
      delays = Some(List(DelayDto(fourth.chordComponentString, third.chordComponentString))),
      inversion = Some(prime.chordComponentString),
      mode = Some(HarmonicFunctionDto.Mode.Major),
      isRelatedBackwards = Some(true),
      isDown = Some(true),
      degree = Some(HarmonicFunctionDto.Degree.VII),
      position = Some(seventh.chordComponentString),
      system = Some(HarmonicFunctionDto.System.Close),
      key = Some(keyf.toString)
    )
  )
}
