package pl.agh.harmonytools.solver.soprano.evaluator

import pl.agh.harmonytools.algorithm.graph.node.NodeContent
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.TONIC
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.measure.MeasurePlace
import pl.agh.harmonytools.model.measure.MeasurePlace.MeasurePlace
import pl.agh.harmonytools.model.note.BaseNote.C
import pl.agh.harmonytools.model.note.{Note, NoteWithoutChordContext}

case class HarmonicFunctionWithSopranoInfo(
  harmonicFunction: HarmonicFunction,
  measurePlace: MeasurePlace = MeasurePlace.DOWNBEAT,
  sopranoNote: NoteWithoutChordContext
) extends NodeContent {
  override def isRelatedTo(other: NodeContent): Boolean = {
    other match {
      case otherHf: HarmonicFunctionWithSopranoInfo => otherHf.harmonicFunction.hasSameFunctionInKey(harmonicFunction)
      case _                                        => false
    }
  }
}

object HarmonicFunctionWithSopranoInfo {
  def empty: HarmonicFunctionWithSopranoInfo =
    HarmonicFunctionWithSopranoInfo(HarmonicFunction(TONIC), sopranoNote = NoteWithoutChordContext(0, C))
}
