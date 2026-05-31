package harmonytools.solver.walking.evaluator

import harmonytools.solver.walking.evaluator.rules.hard.{JumpRule, LeadingNotApproachedBy7Rule, LeadingNoteClosestRule, MaxIntervalForSameChordRule, NoFourthOn2BeatRule, OnBeatRule, RootOfNextRule, RootOnBarStartRule, SamePitchRepetitionRule, StartingNoteRule}
import harmonytools.solver.walking.evaluator.rules.soft.{ClosestMoveRule, ClosestVoiceLeadingRule, DoubleJumpRule, EnclosuresRule, LastTripletVoiceLeadingRule, LastTwoTripletsRule, OctavesOn34BeatsRule, OneFourthOneRule, Repetition5Measures, RepetitionAfterOneRule, RootStrongBeatsRule, TotalJumpRule, UseScalesRule}
import harmonytools.solver.walking.generator.WalkingBassNote
import harmonytools.algorithm.evaluator.{ConnectionEvaluator, HardRule, SoftRule}

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
