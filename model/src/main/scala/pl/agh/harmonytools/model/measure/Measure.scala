package pl.agh.harmonytools.model.measure

import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction

case class Measure(harmonicFunctions: List[HarmonicFunction]) {
  def merge(other: Measure): Measure =
    Measure(harmonicFunctions ++ other.harmonicFunctions)

  def contentCount: Int = harmonicFunctions.length
}
