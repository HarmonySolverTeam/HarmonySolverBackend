package pl.agh.harmonytools.solver.walking.evaluator

import pl.agh.harmonytools.algorithm.evaluator.{ConnectionEvaluator, HardRule, SoftRule}
import pl.agh.harmonytools.solver.walking.evaluator.rules.hard._
import pl.agh.harmonytools.solver.walking.evaluator.rules.soft._
import pl.agh.harmonytools.solver.walking.generator.WalkingBassNote

case class BassRulesChecker() extends ConnectionEvaluator[WalkingBassNote] {

  override protected val connectionSize: Int = 3

  override protected val softRules: List[SoftRule[WalkingBassNote]] = List(
    LastTwoTripletsRule,
    LastTripletVoiceLeadingRule,
    OneFourthOneRule,
    Repetition5Measures,
    ClosestVoiceLeadingRule,
    OctavesOn34BeatsRule,
    RepetitionAfterOneRule,
    DoubleJumpRule,
    TotalJumpRule,
    ClosestMoveRule,
    EnclosuresRule,
    UseScalesRule,
    RootStrongBeatsRule
  )

  override protected val hardRules: List[HardRule[WalkingBassNote]] = List(
    NoFourthOn2BeatRule,
    LeadingNotApproachedBy7Rule,
    MaxIntervalForSameChordRule,
    LeadingNoteClosestRule,
    SamePitchRepetitionRule,
    RootOfNextRule,
    StartingNoteRule,
    OnBeatRule,
    JumpRule,
    RootOnBarStartRule
  )
}
