package pl.agh.harmonytools.model.scale

import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.utils.Extensions.ExtendedInt

trait Scale {
  val key: Key
}

trait ScaleCompanion {
  val pitches: List[Int]

  final def getDegree(pitch: Int, key: Key): ScaleDegree.Degree = {
    val base = key.tonicPitch.toInt %% 12
    for ((scalePitch, idx) <- pitches.zipWithIndex) {
      if (pitch %% 12 == (base + scalePitch) %% 12) {
        return ScaleDegree.fromValue(idx + 1)
      }
    }
    throw new InternalError("Unknown ScaleDegree - pitch from other key.")
  }
}

