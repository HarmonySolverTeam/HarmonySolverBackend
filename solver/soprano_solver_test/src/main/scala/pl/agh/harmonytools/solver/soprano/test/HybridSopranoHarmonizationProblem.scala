package pl.agh.harmonytools.solver.soprano.test

import io.jenetics.util.RandomRegistry
import pl.agh.harmonytools.error.HarmonySolverError
import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.integrations.jenetics.JChromosome
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.SopranoSolution
import pl.agh.harmonytools.solver.soprano.SopranoSolver
import pl.agh.harmonytools.solver.soprano.genetic.{GeneticChord, SopranoChordGenerator, SopranoGeneticSolution, SopranoHarmonizationChromosome, SopranoHarmonizationGene, SopranoHarmonizationProblemAbstract}
import spray.json._

class HybridSopranoHarmonizationProblem(exercise: SopranoExercise, exerciseName: String) extends SopranoHarmonizationProblemAbstract(exercise) {
  private val generator                 = SopranoChordGenerator(exercise.key)

  private def getChordsFromSolution: List[Chord] = {
    val solutionName = exerciseName match {
      case "targosz_p59_ex1" => "24_05_2022_00_11_34_3970.json"
      case "targosz_p59_ex2" => "24_05_2022_00_39_31_3369.json"
      case "targosz_p59_ex4" => "24_05_2022_00_52_47_2834.json"
      case "targosz_p60_ex6" => "24_05_2022_00_19_46_1956.json"
      case "targosz_p60_ex7" => "24_05_2022_00_04_59_811.json"
      case "targosz_p60_ex10" => "24_05_2022_01_01_06_4846.json"
      case "targosz_p60_ex11" => "24_05_2022_00_25_30_5505.json"
      case "targosz_p60_ex12" => "23_05_2022_23_59_34_1820.json"
      case "targosz_p238_ex106" => "23_05_2022_23_45_27_5624.json"
      case "targosz_p238_ex109" => "24_05_2022_00_34_18_12544.json"
    }

    val path = s"solver/soprano_solver_test/src/main/resources/bayes_solutions/$exerciseName/$solutionName"
    val source = scala.io.Source.fromFile(path)
    val solutionJSON = try source.mkString finally source.close()
    val solution = solutionJSON.parseJson
    implicit val reader = new JsonReader[SopranoSolution] {
      override def read(json: JsValue): SopranoSolution = {
        json match {
          case JsObject(fields) =>
            fields("chords") match {
              case JsArray(elements) => SopranoSolution(exercise, -1, elements.map {
                case JsObject(fields) =>
                  Chord.empty
                case _ => ???
              }.toList)
              case _ => ???
            }
          case _ => ???
        }
      }
    }
    solution.convertTo[SopranoSolution].chords
  }

  override def createChromosomes(): Seq[JChromosome[SopranoHarmonizationGene]] = {
    val chords = if (RandomRegistry.getRandom.nextDouble() < 0.005) getChordsFromSolution else {
      SopranoSolver.prepareSopranoGeneratorInputs(exercise).map(generator.generate).map { chords =>
        if (chords.isEmpty)
          throw new HarmonySolverError("Provided note which cannot be harmonized with given set of harmonic functions") {
            override val source: String = "Soprano Solver"
          }
        chords(RandomRegistry.getRandom.nextInt(chords.size))
      }
    }
    Seq(
      SopranoHarmonizationChromosome(
        SopranoGeneticSolution(chords.zipWithIndex.map { case (chord, idx) => GeneticChord(chord, idx) }, exercise),
        generator
      )
    )
  }
}

object HybridSopranoHarmonizationProblem extends App {
  new HybridSopranoHarmonizationProblem(ExercisesDef.targosz_p59_ex2, "targosz_p59_ex2").getChordsFromSolution
}
