package pl.agh.harmonytools.solver.soprano.generator

import pl.agh.harmonytools.algorithm.generator.LayerGenerator
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.TONIC
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.scale.ScaleDegree.I
import pl.agh.harmonytools.solver.harmonics.generator.ChordGenerator
import pl.agh.harmonytools.solver.soprano.evaluator.HarmonicFunctionWithSopranoInfo
import pl.agh.harmonytools.utils.Extensions._

case class HarmonicFunctionGenerator(
  allowedHarmonicFunctions: List[HarmonicFunction],
  key: Key
) extends LayerGenerator[HarmonicFunctionWithSopranoInfo, HarmonicFunctionGeneratorInput] {
  private val chordGenerator = ChordGenerator(key)
  private[generator] val map = new HarmonicFunctionMap

  for (hf <- allowedHarmonicFunctions) {
    val possibleNotesToHarmonize = chordGenerator.generatePossibleSopranoNotesFor(hf)
    for (note <- possibleNotesToHarmonize) {
      map.pushToValues(note.pitch %% 12 + 60, note.baseNote, hf.copy(position = Some(note.chordComponent)))
    }
  }

  override def generate(input: HarmonicFunctionGeneratorInput): List[HarmonicFunctionWithSopranoInfo] = {
    val resultList: Set[HarmonicFunction] =
      if (input.isFirst || input.isLast) {
        map.getValues(input.sopranoNote.pitch, input.sopranoNote.baseNote)
          .filter( hf =>
            hf.baseFunction == TONIC &&
              hf.degree == I &&
              hf.key.isEmpty &&
              hf.revolution == hf.getPrime
        )
      } else {
        map.getValues(input.sopranoNote.pitch, input.sopranoNote.baseNote)
      }
    resultList.map(HarmonicFunctionWithSopranoInfo(_, input.measurePlace, input.sopranoNote)).toList
  }
}
