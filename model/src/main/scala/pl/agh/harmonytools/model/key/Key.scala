package pl.agh.harmonytools.model.key

import pl.agh.harmonytools.error.{HarmonySolverError, UnexpectedInternalError}
import pl.agh.harmonytools.model.key.Mode.{MAJOR, Mode}
import pl.agh.harmonytools.model.note.BaseNote

import scala.collection.immutable.HashMap

case class Key(
  mode: Mode,
  tonicPitch: Integer,
  baseNote: BaseNote.BaseNote
) {
  override def toString: String = {
    val keyLower = Key
      .pitchKeyStr(tonicPitch)
      .find(_.head.toUpper == baseNote.name)
      .getOrElse(throw UnexpectedInternalError("Internal error in key to string"))
    if (mode == MAJOR)
      keyLower.head.toUpper + keyLower.tail
    else
      keyLower
  }
}

object Key {

  private def baseNoteFromKeySignature(keySignature: String): BaseNote.BaseNote =
    BaseNote.values
      .find(b => b.toString == keySignature.toUpperCase.head.toString)
      .getOrElse(throw KeyParseError("Unsupported key signature: " + keySignature))

  def apply(keySignature: String): Key = {
    Key(
      if (keySignature.head.isLower) Mode.MINOR else Mode.MAJOR,
      keyStrPitch.getOrElse(
        keySignature.toLowerCase,
        throw KeyParseError("Illegal keySignature: " + keySignature)
      ),
      baseNoteFromKeySignature(keySignature)
    )
  }

  def inferKeySignature(baseNote: BaseNote.BaseNote, tonicPitch: Integer): String = {
    val possibleKeySignatures = pitchKeyStr
      .getOrElse(tonicPitch, throw UnexpectedInternalError("Illegal tonicPitch: " + tonicPitch))
      .filter(keyCandidate => baseNoteFromKeySignature(keyCandidate).equals(baseNote))
      .toList
    possibleKeySignatures match {
      case Nil       => throw UnexpectedInternalError("Cannot infer a key")
      case el :: Nil => el
      case _         => throw UnexpectedInternalError("Cannot infer a key - too many possible keys for given arguments")
    }
  }

  def apply(baseNote: BaseNote.BaseNote, tonicPitch: Integer): Key = {
    Key(
      Mode.MAJOR,
      tonicPitch,
      baseNote
    )
  }

  private final val keyStrPitch: HashMap[String, Integer] = HashMap(
    "c"   -> 60,
    "b#"  -> 60,
    "dbb" -> 60,
    "c#"  -> 61,
    "db"  -> 61,
    "b##" -> 61,
    "d"   -> 62,
    "c##" -> 62,
    "ebb" -> 62,
    "d#"  -> 63,
    "eb"  -> 63,
    "fbb" -> 63,
    "e"   -> 64,
    "d##" -> 64,
    "fb"  -> 64,
    "f"   -> 65,
    "e#"  -> 65,
    "gbb" -> 65,
    "f#"  -> 66,
    "gb"  -> 66,
    "e##" -> 66,
    "g"   -> 67,
    "f##" -> 67,
    "abb" -> 67,
    "g#"  -> 68,
    "ab"  -> 68,
    "a"   -> 69,
    "g##" -> 69,
    "bbb" -> 69,
    "a#"  -> 70,
    "bb"  -> 70,
    "cbb" -> 70,
    "b"   -> 71,
    "cb"  -> 71,
    "a##" -> 71
  )

  lazy val possibleMajorKeys: List[Key] = {
    val keysString = List("C", "C#", "Db", "D", "Eb", "E", "F", "F#", "Gb", "G", "Ab", "A", "Bb", "B", "Cb")
    keysString.map(Key(_))
  }

  lazy val possibleMinorKeys: List[Key] = {
    val keysString = List("c", "c#", "d", "d#", "eb", "e", "f", "f#", "g", "g#", "ab", "a", "a#", "bb", "b")
    keysString.map(Key(_))
  }

  private final val pitchKeyStr: HashMap[Integer, Set[String]] = {
    val builder = HashMap.newBuilder[Integer, Set[String]]
    builder ++= keyStrPitch.groupBy(_._2).mapValues(_.keySet)
    builder.result()
  }
}

case class KeyParseError(msg: String, det: Option[String] = None) extends HarmonySolverError(msg, det) {
  override val source: String = "Key is provided in incorrect format"
}
