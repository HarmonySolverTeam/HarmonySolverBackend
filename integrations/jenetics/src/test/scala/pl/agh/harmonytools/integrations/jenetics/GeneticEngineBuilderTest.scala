package pl.agh.harmonytools.integrations.jenetics

import io.jenetics.engine.Engine
import io.jenetics.engine.Engine.Evaluator
import io.jenetics.ext.WeaselMutator
import io.jenetics.util.ISeq
import io.jenetics.{Alterer, BoltzmannSelector, Genotype, Mutator, Optimize, Phenotype, RouletteWheelSelector, SwapMutator, TournamentSelector, util}
import org.scalatest.{Assertion, FunSuite, Matchers}

import java.time.Clock
import java.util.concurrent.Executor

class GeneticEngineBuilderTest extends FunSuite with Matchers {
  private val builder            = GeneticEngine.builder(new TestGeneticProblem)
  private val genotype           = Genotype.create(Seq(new TestChromosome(new TestItemWrapper(List(1, 2, 3)))))
  private val boltzmannSelector  = new BoltzmannSelector[TestGene, Integer]
  private val tournamentSelector = new TournamentSelector[TestGene, Integer]
  private val rouletteSelector   = new RouletteWheelSelector[TestGene, Integer]
  private val alterers = List(
    new Mutator[TestGene, Integer],
    new WeaselMutator[TestGene, Integer],
    new SwapMutator[TestGene, Integer]
  )
  private val population = 111

  test("population") {
    builder.populationSize(population)
    assertBuild(_.getPopulationSize, population)
  }

  test("fitness function") {
    val fitness =
      (genotype: Genotype[TestGene]) => new Integer(genotype.getChromosome.asInstanceOf[TestChromosome].getGene(0).item)
    builder.fitnessFunction(fitness)
    assertBuild(_.getFitnessFunction.apply(genotype), fitness(genotype))
  }

  test("genotype factory") {
    val genotypeFactory = () => genotype
    builder.genotypeFactory(genotypeFactory)
    assertBuild(_.getGenotypeFactory.newInstance(), genotype)
  }

  test("offspring selector") {
    builder.offspringSelector(boltzmannSelector)
    assertBuild(_.getOffspringSelector, boltzmannSelector)
  }

  test("survisors selector") {
    builder.survivorsSelector(tournamentSelector)
    assertBuild(_.getSurvivorsSelector, tournamentSelector)
  }

  test("selector") {
    builder.selector(rouletteSelector)
    assertBuild(_.getOffspringSelector, rouletteSelector)
    assertBuild(_.getSurvivorsSelector, rouletteSelector)
  }

  test("alterers") {
    builder.alterers(alterers: _*)
    assertBuild(_.getAlterers, Alterer.of(alterers: _*))
  }

  test("optimize") {
    for (optimize <- List(Optimize.MINIMUM, Optimize.MAXIMUM)) {
      builder.optimize(optimize)
      assertBuild(_.getOptimize, optimize)
    }
  }

  test("maximizing") {
    builder.maximizing()
    assertBuild(_.getOptimize, Optimize.MAXIMUM)
  }

  test("minimizing") {
    builder.minimizing()
    assertBuild(_.getOptimize, Optimize.MINIMUM)
  }

  test("offspring fraction") {
    builder.offspringFraction(0.1)
    assertBuild(_.getOffspringFraction, 0.1)
  }

  test("surivivors fraction") {
    builder.survivorsFraction(0.1)
    assertBuild(_.getOffspringFraction, 0.9)
  }

  test("offspring size") {
    val offspring = 3.0
    builder.offspringSize(offspring.toInt)
    assertBuild(_.getOffspringFraction, offspring / population)
  }

  test("survivors size") {
    val offspring = 3.0
    builder.survivorsSize(offspring.toInt)
    assertBuild(_.getOffspringFraction, 1.0 - (offspring / population))
  }

  test("maximal phenotype age") {
    builder.maximalPhenotypeAge(10)
    assertBuild(_.getMaximalPhenotypeAge, 10)
  }

  test("executor") {
    val executor = new Executor {
      override def execute(command: Runnable): Unit = ???
    }

    builder.executor(executor)
    assertBuild(_.getExecutor, executor)
  }

  test("clock") {
    val clock = Clock.systemUTC()
    builder.clock(clock)
    assertBuild(_.getClock, clock)
  }

  test("evaluator") {
    builder.individualCreationRetries(11)
    assertBuild(_.getIndividualCreationRetries, 11)
  }

  def assertBuild[A](f: Engine.Builder[TestGene, Integer] => A, result: A): Assertion =
    f(builder.jeneticsEngine) shouldBe result
}
