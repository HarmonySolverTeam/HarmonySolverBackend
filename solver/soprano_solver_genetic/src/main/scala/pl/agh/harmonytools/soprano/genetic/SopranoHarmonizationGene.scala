package pl.agh.harmonytools.soprano.genetic

import io.jenetics.util.RandomRegistry
import pl.agh.harmonytools.integrations.jenetics.Gene
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.measure.MeasurePlace.MeasurePlace
import pl.agh.harmonytools.solver.soprano.generator.HarmonicFunctionGeneratorInput

case class SopranoHarmonizationGene(
  chord: GeneticChord,
  measurePlace: MeasurePlace,
  isFirst: Boolean,
  isLast: Boolean,
  generator: SopranoChordGenerator,
  id: Int
) extends Gene[GeneticChord, SopranoHarmonizationGene](chord) {
  override def newInstance(): SopranoHarmonizationGene =
    newInstance(generateSubstitutions(), RandomRegistry.getRandom)

  override def newInstance(newChord: GeneticChord): SopranoHarmonizationGene = copy(chord = newChord)

  def toGeneratorInput: HarmonicFunctionGeneratorInput =
    HarmonicFunctionGeneratorInput(chord.content.sopranoNote.withoutChordContext, measurePlace, isFirst, isLast)

  def newInstance(chords: List[Chord], random: java.util.Random): SopranoHarmonizationGene = {
    assert(chords.nonEmpty)
    val chord = chords(random.nextInt(chords.length))
    newInstance(GeneticChord(chord, id))
  }

  def generateSubstitutions(filter: Chord => Boolean = _ => true): List[Chord] = generator.generate(toGeneratorInput).filter(filter)
}
