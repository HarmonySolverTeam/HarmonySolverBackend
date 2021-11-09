package pl.agh.harmonytools.soprano.genetic

import pl.agh.harmonytools.integrations.jenetics.Gene
import pl.agh.harmonytools.model.measure.MeasurePlace.MeasurePlace
import pl.agh.harmonytools.solver.soprano.generator.HarmonicFunctionGeneratorInput

import scala.util.Random

case class SopranoHarmonizationGene(
  chord: GeneticChord,
  measurePlace: MeasurePlace,
  isFirst: Boolean,
  isLast: Boolean,
  generator: SopranoChordGenerator,
  id: Int
) extends Gene[GeneticChord, SopranoHarmonizationGene](chord) {
  override def newInstance(): SopranoHarmonizationGene = {
    val input = HarmonicFunctionGeneratorInput(chord.content.sopranoNote.withoutChordContext, measurePlace, isFirst, isLast)
    val possibleChords = generator.generate(input)
    copy(chord = GeneticChord(possibleChords(Random.nextInt(possibleChords.size)), id))
  }

  override def newInstance(value: GeneticChord): SopranoHarmonizationGene = copy(chord = chord)
}
