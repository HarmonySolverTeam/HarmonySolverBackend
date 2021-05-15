package pl.agh.harmonytools.solver.soprano.generator

import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.note.BaseNote.{A, B, BaseNoteType, C, D, E, F, G}
import pl.agh.harmonytools.utils.Extensions._

class HarmonicFunctionMap {
  private def initializeForPitch(pitch: Int, baseNotes: List[BaseNoteType]): Unit = {
    for (baseNote <- baseNotes)
      map = map + ((pitch, baseNote) -> Set.empty)
  }
  private var map: Map[(Int, BaseNoteType), Set[HarmonicFunction]] = Map.empty

  initializeForPitch(60, List(B, C, D))
  initializeForPitch(61, List(B, C, D))
  initializeForPitch(62, List(C, D, E))
  initializeForPitch(63, List(D, E, F))
  initializeForPitch(64, List(D, E, F))
  initializeForPitch(65, List(E, F, G))
  initializeForPitch(66, List(E, F, G))
  initializeForPitch(67, List(F, G, A))
  initializeForPitch(68, List(G, A))
  initializeForPitch(69, List(G, A, B))
  initializeForPitch(70, List(A, B, C))
  initializeForPitch(71, List(A, B, C))

  def getValues(pitch: Int, baseNoteType: BaseNoteType): Set[HarmonicFunction] = {
    val p = pitch %% 12 + 60
    map(p, baseNoteType)
  }

  def pushToValues(pitch: Int, baseNoteType: BaseNoteType, harmonicFunction: HarmonicFunction): Unit = {
    if (pitch >= 60 && pitch < 72 && !map(pitch, baseNoteType).contains(harmonicFunction))
      map = map.updated((pitch, baseNoteType), map(pitch, baseNoteType) + harmonicFunction)
  }
}
