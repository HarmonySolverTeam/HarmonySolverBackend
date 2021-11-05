package pl.agh.harmonytools.integrations.jenetics

import io.jenetics._
import io.jenetics.engine.Engine.{Evaluator, GenotypeEvaluator}
import io.jenetics.engine.{Engine, EvolutionResult, Problem}

import java.time.Clock
import java.util.concurrent.Executor

import scala.collection.JavaConverters._

class GeneticEngineBuilder[T <: ItemWrapper[_], G <: JGene[_, G], C <: Comparable[C]](jeneticsEngine: Engine.Builder[G, C]) {

  type GE = GeneticEngineBuilder[T, G, C]

  def fitnessFunction(fitness: Genotype[G] => C): GE = mod(_.fitnessFunction((g: Genotype[G]) => fitness(g)))

  def genotypeFactory(factory: () => Genotype[G]): GE = mod(_.genotypeFactory(() => factory()))

  def offspringSelector(selector: Selector[G, C]): GE = mod(_.offspringSelector(selector))

  def survivorsSelector(selector: Selector[G, C]): GE = mod(_.survivorsSelector(selector))

  def selector(selector: Selector[G, C]): GE = mod(_.selector(selector))

  def alterers(alterers: Alterer[G, C]*): GE = mod(_.alterers(alterers.head, alterers.tail: _*))

  def phenotypeValidator(validator: Phenotype[G, C] => Boolean): GE =
    mod(_.phenotypeValidator((t: Phenotype[G, C]) => validator(t)))

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

  def build(): GeneticEngine[G, C] = new GeneticEngine(jeneticsEngine.build())

  private def mod(op: Engine.Builder[G, C] => Unit): GE = {
    op(jeneticsEngine)
    this
  }

}

class GeneticEngine[G <: JGene[_, G], C <: Comparable[C]](jeneticsEngine: Engine[G, C]) {
  def stream(): Stream[EvolutionResult[G, C]] = {
    jeneticsEngine.stream().iterator().asScala.toStream
  }
}

object GeneticEngine {
  def builder[T <: ItemWrapper[_], G <: JGene[_, G], C <: Comparable[C]](problem: GeneticProblem[T, G, C]): GeneticEngineBuilder[T, G, C] =
    new GeneticEngineBuilder[T, G, C](Engine.builder(problem))
}
