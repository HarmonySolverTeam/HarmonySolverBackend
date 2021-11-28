package pl.agh.harmonytools.soprano.genetic

import io.jenetics
import pl.agh.harmonytools.error.HarmonySolverError
import pl.agh.harmonytools.integrations.jenetics.Chromosome
import pl.agh.harmonytools.model.measure.MeasurePlace
import pl.agh.harmonytools.solver.soprano.SopranoSolver

import scala.util.Random

case class SopranoHarmonizationChromosome(solution: SopranoGeneticSolution, generator: SopranoChordGenerator)
  extends Chromosome[GeneticChord, SopranoGeneticSolution, SopranoHarmonizationGene](solution) {
  override def newInstanceByGenes(
    genes: Seq[SopranoHarmonizationGene]
  ): Chromosome[GeneticChord, SopranoGeneticSolution, SopranoHarmonizationGene] =
    copy(solution = SopranoGeneticSolution(genes.map(_.chord).toList, solution.exercise))

  override def itemGeneMapper: GeneticChord => SopranoHarmonizationGene = { genChord =>
    val chord         = solution.chords.find(_.id == genChord.id).getOrElse(sys.error("Unknown Chord in Genetic Engine"))
    val totalDuration = solution.chords.takeWhile(_.id < genChord.id).map(_.content.duration)
    SopranoHarmonizationGene(
      chord,
      MeasurePlace.getMeasurePlace(solution.meter, totalDuration.sum),
      genChord.id == 0,
      genChord.id == solution.chords.size - 1,
      generator,
      genChord.id
    )
  }

  override def getGene(index: Int): SopranoHarmonizationGene = {
    val totalDuration = solution.chords.takeWhile(_.id < index).map(_.content.duration)
    SopranoHarmonizationGene(
      solution.chords(index),
      MeasurePlace.getMeasurePlace(solution.meter, totalDuration.sum),
      index == 0,
      index == solution.chords.size - 1,
      generator,
      index
    )
  }

  override def newInstance(): jenetics.Chromosome[SopranoHarmonizationGene] = {
    val chords = SopranoSolver.prepareSopranoGeneratorInputs(solution.exercise).map(generator.generate).map { chords =>
      if (chords.isEmpty)
        throw new HarmonySolverError("Provided note which cannot be harmonized with given set of harmonic functions") {
          override val source: String = "Soprano Solver"
        }
      chords(Random.nextInt(chords.size))
    }
    SopranoHarmonizationChromosome(
      SopranoGeneticSolution(
        chords.zipWithIndex.map { case (chord, idx) => GeneticChord(chord, idx) },
        solution.exercise
      ),
      generator
    )
  }
}
