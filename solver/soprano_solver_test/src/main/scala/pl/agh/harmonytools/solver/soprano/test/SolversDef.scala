package pl.agh.harmonytools.solver.soprano.test

import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.solver.soprano.SopranoSolver
import pl.agh.harmonytools.solver.soprano.genetic.{SopranoChordGenerator, SopranoGeneticSolver, SopranoHarmonizationProblem}

import scala.collection.mutable.ListBuffer

object SolversDef {
  def solveByRuleBasedSystem(name: String, exercise: SopranoExercise): Double = {
    val solution = SopranoSolver(exercise.copy(possibleFunctionsList = SopranoChordGenerator(exercise.key).allowedHarmonicFunctions)).solve()
    val path = s"solver/soprano_solver_test/src/main/resources/rule_based_solutions/$name"
    solution.save(path)
    solution.rating
  }

  def solveByGeneticAlgorithm(name: String, exercise: SopranoExercise): (List[Double], List[Double]) = {
    val ratings = ListBuffer[Double]()
    val mins = ListBuffer[Double]()
    for (_ <- 1 to 10) {
      val solution = new SopranoGeneticSolver(exercise, 2000, 2000, 0.2, 0.5, 0.3, new SopranoHarmonizationProblem(exercise)).solve()
      val path = s"solver/soprano_solver_test/src/main/resources/genetic_solutions/$name"
      solution.save(path)
      ratings.append(solution.rating)
      mins.append(solution.minEpoch.getOrElse(sys.error("Unknown min epoch")))
    }
    (ratings.toList, mins.toList)
  }

  def solveByHybridAlgorithm(name: String, exercise: SopranoExercise): (List[Double], List[Double]) = {
    val ratings = ListBuffer[Double]()
    val mins = ListBuffer[Double]()
    for (_ <- 1 to 10) {
      val solution = new SopranoGeneticSolver(exercise, 1000, 1500, 0.2, 0.5, 0.3, new HybridSopranoHarmonizationProblem(exercise)).solve()
      val path = s"solver/soprano_solver_test/src/main/resources/hybrid_solutions/$name"
      solution.save(path)
      ratings.append(solution.rating)
      mins.append(solution.minEpoch.getOrElse(sys.error("Unknown min epoch")))
    }
    (ratings.toList, mins.toList)
  }


  def solveByAll(name: String, exercise: SopranoExercise): Map[String, Any] = {
    val result = solveByHybridAlgorithm(name, exercise)
    Map(
//      "rule_based_system" -> solveByRuleBasedSystem(name, exercise),
//      "genetic_algorithm" -> solveByGeneticAlgorithm(name, exercise),
//      "bayes_network" -> solveByBayesNetwork(name, exercise)
        "hybrid_algorithm" -> result._1,
        "hybrid_algorithm-min_epoch_number" -> result._2
    )
  }
}
