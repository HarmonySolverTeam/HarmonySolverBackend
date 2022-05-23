package pl.agh.harmonytools.solver.soprano.test

import io.jenetics.util.RandomRegistry
import pl.agh.harmonytools.error.HarmonySolverError
import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.integrations.jenetics.JChromosome
import pl.agh.harmonytools.solver.soprano.SopranoSolver
//import pl.agh.harmonytools.solver.soprano.bayes.BayesNetSopranoSolver
import pl.agh.harmonytools.solver.soprano.genetic.{GeneticChord, SopranoChordGenerator, SopranoGeneticSolution, SopranoHarmonizationChromosome, SopranoHarmonizationGene, SopranoHarmonizationProblemAbstract}

class HybridSopranoHarmonizationProblem(exercise: SopranoExercise) extends SopranoHarmonizationProblemAbstract(exercise) {
  private val generator                 = SopranoChordGenerator(exercise.key)

  override def createChromosomes(): Seq[JChromosome[SopranoHarmonizationGene]] = {
    ???
//    val chords = if (RandomRegistry.getRandom.nextDouble() < 0.05) new BayesNetSopranoSolver(exercise).solve().chords else {
//      SopranoSolver.prepareSopranoGeneratorInputs(exercise).map(generator.generate).map { chords =>
//        if (chords.isEmpty)
//          throw new HarmonySolverError("Provided note which cannot be harmonized with given set of harmonic functions") {
//            override val source: String = "Soprano Solver"
//          }
//        chords(RandomRegistry.getRandom.nextInt(chords.size))
//      }
//    }
//    Seq(
//      SopranoHarmonizationChromosome(
//        SopranoGeneticSolution(chords.zipWithIndex.map { case (chord, idx) => GeneticChord(chord, idx) }, exercise),
//        generator
//      )
//    )
  }
}
