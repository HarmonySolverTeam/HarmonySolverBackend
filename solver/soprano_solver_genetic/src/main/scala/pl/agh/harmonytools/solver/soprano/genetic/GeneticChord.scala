package pl.agh.harmonytools.solver.soprano.genetic

import pl.agh.harmonytools.algorithm.graph.node.NodeContent
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.harmonicfunction.BaseFunction.{DOMINANT, SUBDOMINANT, TONIC}

import scala.math.abs

case class GeneticChord(content: Chord, id: Int) extends NodeContent {
  override def isRelatedTo(other: NodeContent): Boolean =
    other match {
      case GeneticChord(chord, _) => content isRelatedTo chord
      case _                      => false
    }

  private def computeFunctionsDiff(other: Chord): Double = {
    (content.harmonicFunction.baseFunction, other.harmonicFunction.baseFunction) match {
      case (TONIC, DOMINANT) => 3
      case (SUBDOMINANT, TONIC) => 2
      case (x, y) if x == y =>
        if (content.harmonicFunction.degree != other.harmonicFunction.degree) 2
        else 1
      case _ => 0
    }
  }
// metryka opisująca odległość akordów

  def computeMetric(other: Chord): Double = {
    if (other.harmonicFunction.isModulation) return 0
    val notesDiff     = content.notes.tail.zip(other.notes.tail).map(x => abs(x._1.pitch - x._2.pitch)).sum * 3.0
    val functionsDiff = 3 * computeFunctionsDiff(other)
    val sidePoints = if (!List(1, 4, 5).contains(other.harmonicFunction.degree.root) || other.harmonicFunction.key != content.harmonicFunction.key)
      5 else 0
    val extraPoints = if (content.harmonicFunction.extra.size < other.harmonicFunction.extra.size) 3 else 0
    notesDiff + functionsDiff - sidePoints - extraPoints
  }
}
