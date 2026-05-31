package harmonytools.solver.walking.generator

import harmonytools.algorithm.graph.node.NodeContent
import harmonytools.model.note.JazzNote
import harmonytools.model.util.ChordComponentManager

case class WalkingBassNote(note: JazzNote, input: BassGeneratorInput) extends NodeContent {
  override def isRelatedTo(other: NodeContent): Boolean =
    other match {
      case WalkingBassNote(otherNote, _) => otherNote.pitch == note.pitch
      case _                             => false
    }
}

object WalkingBassNote {

  val empty: WalkingBassNote = WalkingBassNote(
    JazzNote(0, ChordComponentManager.getRoot()),
    BassGeneratorInput(
      List.empty,
      List.empty,
      List.empty,
      false,
      false,
      0.0,
      "",
      false
    )
  )
}
