package pl.agh.harmonytools.integrations.jenetics

import io.jenetics._
import io.jenetics.engine.Engine.{Evaluator, GenotypeEvaluator}
import io.jenetics.engine.{Engine, EvolutionResult}

import java.time.Clock
import java.util.concurrent.Executor
import java.util.concurrent.atomic.AtomicInteger
import scala.collection.JavaConverters._

case class GeneticEngineBuilder[T <: ItemWrapper[_], G <: JGene[_, G], C <: Fitness[C]](
  private[jenetics] val jeneticsEngine: Engine.Builder[G, C],
  private val recombinators: Seq[Recombinator[G, C]] = Seq(),
  private val repairOperators: Seq[RepairOperator[G, C]] = Seq(),
  private val otherMutators: Seq[Mutator[G, C]] = Seq()
) {

  type GE = GeneticEngineBuilder[T, G, C]

  def fitnessFunction(fitness: Genotype[G] => C): GE = mod(_.fitnessFunction((g: Genotype[G]) => fitness(g)))

  def genotypeFactory(factory: () => Genotype[G]): GE = mod(_.genotypeFactory(() => factory()))

  def offspringSelector(selector: Selector[G, C]): GE = mod(_.offspringSelector(selector))

  def survivorsSelector(selector: Selector[G, C]): GE = mod(_.survivorsSelector(selector))

  def selector(selector: Selector[G, C]): GE = mod(_.selector(selector))

  def repairOperators(repairOperators: RepairOperator[G, C]*): GE = copy(repairOperators = repairOperators)

  def recombinators(recombinators: Recombinator[G, C]*): GE = copy(recombinators = recombinators)

  def mutators(mutators: Mutator[G, C]*): GE = copy(otherMutators = mutators)

  def phenotypeValidator(validator: Phenotype[G, C] => Boolean): GE =
    mod(_.phenotypeValidator((p: Phenotype[G, C]) => validator(p)))

  def genotypeValidator(validator: Genotype[G] => Boolean): GE =
    mod(_.genotypeValidator((g: Genotype[G]) => validator(g)))

  def optimize(optimize: Optimize): GE = mod(_.optimize(optimize))

  def maximizing(): GE = mod(_.maximizing())

  def minimizing(): GE = mod(_.minimizing())

  def offspringFraction(fraction: Double): GE = mod(_.offspringFraction(fraction))

  def survivorsFraction(fraction: Double): GE = mod(_.survivorsFraction(fraction))

  def offspringSize(size: Int): GE = mod(_.offspringSize(size))

  def survivorsSize(size: Int): GE = mod(_.survivorsSize(size))

  def populationSize(size: Int): GE = mod(_.populationSize(size))

  def maximalPhenotypeAge(age: Int): GE = mod(_.maximalPhenotypeAge(age))

  def executor(executor: Executor): GE = mod(_.executor(executor))

  def clock(clock: Clock): GE = mod(_.clock(clock))

  def evaluator(evaluator: Evaluator[G, C]): GE = mod(_.evaluator(evaluator))

  def genotypeEvaluator(evaluator: GenotypeEvaluator[G, C]): GE = mod(_.evaluator(evaluator))

  def individualCreationRetries(retries: Int): GE = mod(_.individualCreationRetries(retries))

  def mapping(mapper: EvolutionResult[G, C] => EvolutionResult[G, C]): GE =
    mod(_.mapping((res: EvolutionResult[G, C]) => mapper(res)))

  def getAlterers: Seq[Alterer[G, C]] = otherMutators ++ recombinators ++ repairOperators

  def build(): GeneticEngine[G, C] = {
    val alterers = getAlterers
    new GeneticEngine(jeneticsEngine.alterers(alterers.head, alterers.tail: _*).build())
  }

  private def mod(op: Engine.Builder[G, C] => Engine.Builder[G, C]): GE =
    copy(jeneticsEngine = op(jeneticsEngine))

}



class GeneticEngine[G <: JGene[_, G], C <: Fitness[C]](val jeneticsEngine: Engine[G, C]) {
  def stream(maxIterations: Int): Stream[EvolutionResult[G, C]] = {
    val loader      = new AtomicInteger()
    val fivePercent = maxIterations / 20
    val startTime   = System.nanoTime()
    jeneticsEngine
      .stream()
      .peek { stat =>
        if (loader.incrementAndGet() % fivePercent == 0) {
          val currentTime  = System.nanoTime()
          val mean         = (currentTime - startTime) / loader.get() / 1e9d
          val timeToTheEnd = (maxIterations - loader.get()) * mean
          println(s"${loader.get()}/${maxIterations} | ${5 * (loader
            .get() / fivePercent)}% | $mean s/it | $timeToTheEnd s remaining | current rating ${stat.getBestFitness.toDouble}")
        }
      }
      .iterator()
      .asScala
      .toStream
      .take(maxIterations)
  }
}

object GeneticEngine {
  def builder[T <: ItemWrapper[_], G <: JGene[_, G], C <: Fitness[C]](
    problem: GeneticProblem[T, G, C]
  ): GeneticEngineBuilder[T, G, C] =
    new GeneticEngineBuilder[T, G, C](Engine.builder(problem))
}
