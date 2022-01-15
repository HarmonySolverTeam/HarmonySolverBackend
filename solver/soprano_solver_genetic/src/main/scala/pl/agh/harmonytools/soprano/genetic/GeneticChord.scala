package pl.agh.harmonytools.soprano.genetic

import pl.agh.harmonytools.algorithm.graph.node.NodeContent
import pl.agh.harmonytools.model.chord.Chord

import scala.math.abs

case class GeneticChord(content: Chord, id: Int) extends NodeContent {
  override def isRelatedTo(other: NodeContent): Boolean =
    other match {
      case GeneticChord(chord, _) => content isRelatedTo chord
      case _                      => false
    }

  def computeMetric(other: Chord): Double = {
    val notesDiff = content.notes.tail.zip(other.notes.tail).map(x => abs(x._1.pitch - x._2.pitch)).sum.toDouble
    val functionsDiff = if (content.harmonicFunction.baseFunction != other.harmonicFunction.baseFunction) 10 else 0
    val degreeDiff = if (functionsDiff > 0 && content.harmonicFunction.degree != other.harmonicFunction.degree) 10 else 0
    val sidePoints = if (!List(1, 4, 5).contains(other.harmonicFunction.degree.root)) 5 else 0
    notesDiff + functionsDiff + degreeDiff - sidePoints
  }
}
