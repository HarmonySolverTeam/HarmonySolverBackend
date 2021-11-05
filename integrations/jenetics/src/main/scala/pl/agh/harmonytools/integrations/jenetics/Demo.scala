package pl.agh.harmonytools.integrations.jenetics

import io.jenetics._
import pl.agh.harmonytools.integrations.jenetics.KnapsackProblem.MAX_W

import scala.collection.convert.ImplicitConversions.`iterable AsScalaIterable`
import scala.util.Random

object Demo extends App {

  def run(): Unit = {
    val evolver = GeneticEngine.builder(KnapsackProblemClassic)
      .populationSize(100)
      .alterers(
        new SinglePointCrossover(0.4),
        new UniformCrossover(0.3, 0.2)
      )
      .selector(
        new TournamentSelector[BitGene, Integer]()
      )
      .maximizing()
      .build()

    val best = evolver.stream().drop(1000).head.getBestPhenotype

    println(best)
    println(best.getGeneration)
    println(Knapsack(KnapsackProblem.optimalSolution.toSet, 165).profit())
  }

  run()

}

case class Item(profit: Int, weight: Int)

case class Knapsack(items: Set[Item], maxCapacity: Int) extends ItemWrapper[Item](items.toSeq) {
  def profit(): Int = {
    items.map(_.profit).sum
  }

  def weight(): Int = items.map(_.weight).sum
}

case class ItemGene(item: Item) extends Gene[Item, ItemGene](item) {
  override def newInstance(): ItemGene = {
    val idx = Math.abs(Random.nextInt(KnapsackProblem.items.size))
    ItemGene(KnapsackProblem.items(idx))
  }

  override def newInstance(value: Item): ItemGene = ItemGene(item)
}

case class KnapsackChromosome(knapsack: Knapsack) extends Chromosome[Item, Knapsack, ItemGene](knapsack) {
  override def newInstanceByGenes(genes: Seq[ItemGene]): Chromosome[Item, Knapsack, ItemGene] = {
    KnapsackChromosome(Knapsack(genes.map(_.getAllele).toSet, knapsack.maxCapacity))
  }

  override def itemGeneMapper: Item => ItemGene = ItemGene

  override def getGene(index: Int): ItemGene = ItemGene(knapsack.items.toList(index))

  override def newInstance(): Chromosome[Item, Knapsack, ItemGene] = KnapsackChromosome.newInstance()
}

object KnapsackChromosome {
  def newInstance(): Chromosome[Item, Knapsack, ItemGene] = {
    val random = for (item <- KnapsackProblem.items) yield (item, Random.nextBoolean())
    val items = random.collect {
      case (item, true) => item
    }
    KnapsackChromosome(Knapsack(items.toSet, KnapsackProblem.MAX_W))
  }
}

object KnapsackProblemClassic extends GeneticProblem[Knapsack, BitGene, Integer] {
  override def computeFitness(input: Knapsack): Integer = if (input.weight() > MAX_W) -1 else input.profit()

  override def createChromosomes(): Seq[JChromosome[BitGene]] = Seq(BitChromosome.of(KnapsackProblem.items.size))

  override def decodeGenotype(genotype: Genotype[BitGene]): Knapsack = {
    val items = (genotype.getChromosome.toIterable zip KnapsackProblem.items) collect { case (BitGene.ONE, item) => item}
    Knapsack(items.toSet, KnapsackProblem.MAX_W)
  }
}


object KnapsackProblem extends GeneticProblem[Knapsack, ItemGene, Integer] {

  val items = Seq(
    Item(92, 23),
    Item(57, 31),
    Item(49, 29),
    Item(68, 44),
    Item(60, 53),
    Item(43, 38),
    Item(67, 63),
    Item(84, 85),
    Item(87, 89),
    Item(72, 82)
  )

  val optimalSolution = List(true, true, true, true, false, true, false, false, false, false) zip items collect {case (true, item) => item}

  val MAX_W = 165

  override def computeFitness(input: Knapsack): Integer = if (input.weight() > MAX_W) -1 else input.profit()

  override def createChromosomes(): Seq[Chromosome[Item, Knapsack, ItemGene]] = Seq(KnapsackChromosome.newInstance())

  override def decodeGenotype(genotype: Genotype[ItemGene]): Knapsack = genotype.getChromosome.asInstanceOf[KnapsackChromosome].knapsack
}