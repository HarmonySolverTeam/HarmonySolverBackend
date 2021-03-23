package pl.agh.harmonytools.bass

import pl.agh.harmonytools.model.harmonicfunction.Delay

case class BassDelay(first: BassSymbol, second: BassSymbol) {
  def mapToChordComponentDelay(): Delay =
    Delay(first.mapToChordComponentSymbol(), second.mapToChordComponentSymbol())
}
