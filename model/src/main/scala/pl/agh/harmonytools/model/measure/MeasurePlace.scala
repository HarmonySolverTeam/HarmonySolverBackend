package pl.agh.harmonytools.model.measure

import pl.agh.harmonytools.utils.Extensions._

import scala.annotation.tailrec

object MeasurePlace extends Enumeration {
  type MeasurePlace = Value
  val UPBEAT, DOWNBEAT, BEGINNING = Value

  def getMeasurePlace(
    meter: Meter,
    measureCount: Double,
    isRecursive: Boolean = false
  ): MeasurePlace = {
    val count = measureCount * meter.denominator
    //if not integer - return UPBEAT
    if (count.toInt != count)
      return UPBEAT
    if (count == 0)
      return if (isRecursive) DOWNBEAT else BEGINNING

    val nominator = meter.nominator

    if (nominator.isPowerOf2) {
      for (i <- Range(2, nominator, 2)) {
        if (count == i)
          return DOWNBEAT
      }
      return UPBEAT
    }

    if (nominator %% 3 == 0) {
      for (i <- Range(3, nominator, 3))
        if (count == i)
          return DOWNBEAT
      return UPBEAT
    }

    val (part1, part2) =
      if (nominator %% 2 != 0) ((nominator + 1) / 2, (nominator - 1) / 2) else (nominator / 2, nominator / 2)

    if (count < part1)
      getMeasurePlace(Meter(part1, meter.denominator), count / meter.denominator, isRecursive = true)
    else
      getMeasurePlace(Meter(part2, meter.denominator), (count - part1) / meter.denominator, isRecursive = true)
  }
}
