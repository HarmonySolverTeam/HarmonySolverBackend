package pl.agh.harmonytools.harmonics.solver.utils

import pl.agh.harmonytools.harmonics.solver.generator.ChordGenerator
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.note.Note

object PreChecker {
  def run(
    harmonicFunctions: List[HarmonicFunction],
    chordGenerator: ChordGenerator,
    bassLine: Option[List[Note]] = None,
    sopranoLine: Option[List[Note]] = None
  ): Unit = {
    if (sopranoLine.isEmpty) {

    }
  }

}
