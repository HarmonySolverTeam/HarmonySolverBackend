package pl.agh.harmonytools.solver.harmonics.ecase objectuator

object ChordRules {
  abstract class Rule(name: String)
  case object ParallelOctaves extends Rule("ConcurrentOctaves")
  case object ParallelFifths extends Rule("ConcurrentFifths")
  case object CrossingVoices extends Rule("CrossingVoices")
  case object OneDirection extends Rule("OneDirection")
  case object ForbiddenJump extends Rule("ForbiddenJump")
  case object HiddenOctaves extends Rule("Hidden consecutive octaves")
  case object FalseRelation extends Rule("FalseRelation")
  case object SameFunctionCheckConnection extends Rule("SameFunctionCheckConnection")
  case object IllegalDoubledThird extends Rule("IllegalDoubledThird")
}
