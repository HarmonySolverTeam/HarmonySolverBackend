package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.utils.TestUtils

class KeyMapperTest extends MapperTest[Key, String](KeyMapper) with TestUtils {
  import Keys._

  override protected val models: List[Key] = List(
    keyC,
    keyc,
    keyfsharp,
    keyBb
  )
  override protected val dtos: List[String] = List(
    "C",
    "c",
    "f#",
    "Bb"
  )
}
