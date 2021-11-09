package pl.agh.harmonytools.soprano.genetic

import pl.agh.harmonytools.algorithm.graph.node.{EmptyContent, NodeContent}
import pl.agh.harmonytools.model.chord.Chord

case class GeneticChord(content: Chord, id: Int) extends NodeContent {
  override def isRelatedTo(other: NodeContent): Boolean = other match {
    case GeneticChord(chord, _) => content isRelatedTo chord
    case _ => false
  }
}
