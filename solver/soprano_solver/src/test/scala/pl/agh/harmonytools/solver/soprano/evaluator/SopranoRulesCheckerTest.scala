package pl.agh.harmonytools.solver.soprano.evaluator

import org.scalatest.{FunSuite, Matchers}
import pl.agh.harmonytools.algorithm.evaluator.Connection
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.DOMINANT
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.measure.MeasurePlace
import pl.agh.harmonytools.model.note.BaseNote.{A, B, C, D, E, F, G}
import pl.agh.harmonytools.model.note.{Note, NoteWithoutChordContext}
import pl.agh.harmonytools.solver.harmonics.evaluator.AdaptiveRulesChecker
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.ChordRules
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard.ParallelOctavesRule
import pl.agh.harmonytools.solver.soprano.evaluator.rules.hard.{
  ForbiddenDSConnectionRule,
  SecondaryDominantConnectionRule
}
import pl.agh.harmonytools.solver.soprano.evaluator.rules.soft.{
  ChangeFunctionAtMeasureBeginningRule,
  ChangeFunctionConnectionRule,
  ChangeFunctionOnDownBeatRule,
  DominantRelationRule,
  FourthChordsRule,
  JumpRule,
  SecondRelationRule
}
import pl.agh.harmonytools.utils.TestUtils

class SopranoRulesCheckerTest extends FunSuite with Matchers with TestUtils {
  import Keys._
  import HarmonicFunctions._
  import ChordComponents._

  private val rulesChecker = SopranoRulesChecker(keyC)

  test("D -> S connection test") {
    val connection = Connection(
      HarmonicFunctionWithSopranoInfo(subdominant, sopranoNote = anyNoteWithoutChordContext),
      HarmonicFunctionWithSopranoInfo(dominant, sopranoNote = anyNoteWithoutChordContext)
    )
    val allowedConnection = Connection(
      HarmonicFunctionWithSopranoInfo(subdominant, sopranoNote = anyNoteWithoutChordContext),
      HarmonicFunctionWithSopranoInfo(
        HarmonicFunction(DOMINANT, key = Some(keyF)),
        sopranoNote = anyNoteWithoutChordContext
      )
    )
    val rule = ForbiddenDSConnectionRule()
    rule.isBroken(connection) shouldBe true
    rule.isNotBroken(allowedConnection) shouldBe true
  }

  test("Function must change at measure beginning test") {
    val connection = Connection(
      HarmonicFunctionWithSopranoInfo(subdominant, MeasurePlace.BEGINNING, sopranoNote = anyNoteWithoutChordContext),
      HarmonicFunctionWithSopranoInfo(subdominant, MeasurePlace.DOWNBEAT, sopranoNote = anyNoteWithoutChordContext)
    )
    val rule = ChangeFunctionAtMeasureBeginningRule()
    rule.isBroken(connection) shouldBe true
  }

  test("Dominant relation test") {
    val connection1 = Connection(
      HarmonicFunctionWithSopranoInfo(dominant, sopranoNote = anyNoteWithoutChordContext),
      HarmonicFunctionWithSopranoInfo(subdominantII, sopranoNote = anyNoteWithoutChordContext)
    )
    val connection2 = Connection(
      HarmonicFunctionWithSopranoInfo(tonic, sopranoNote = anyNoteWithoutChordContext),
      HarmonicFunctionWithSopranoInfo(subdominantII, sopranoNote = anyNoteWithoutChordContext)
    )
    val rule = DominantRelationRule
    rule.isNotBroken(connection1)
    rule.isBroken(connection2)
  }

  test("TSD cadence test") {
    val connectionSD = Connection(
      HarmonicFunctionWithSopranoInfo(dominant, sopranoNote = anyNoteWithoutChordContext),
      HarmonicFunctionWithSopranoInfo(subdominant, sopranoNote = anyNoteWithoutChordContext)
    )
    val connectionDT = Connection(
      HarmonicFunctionWithSopranoInfo(tonic, sopranoNote = anyNoteWithoutChordContext),
      HarmonicFunctionWithSopranoInfo(dominant, sopranoNote = anyNoteWithoutChordContext)
    )
    val connectionTS = Connection(
      HarmonicFunctionWithSopranoInfo(subdominant, sopranoNote = anyNoteWithoutChordContext),
      HarmonicFunctionWithSopranoInfo(tonic, sopranoNote = anyNoteWithoutChordContext)
    )
    val connectionTD = Connection(
      HarmonicFunctionWithSopranoInfo(dominant, sopranoNote = anyNoteWithoutChordContext),
      HarmonicFunctionWithSopranoInfo(tonic, sopranoNote = anyNoteWithoutChordContext)
    )
    val connectionST = Connection(
      HarmonicFunctionWithSopranoInfo(tonic, sopranoNote = anyNoteWithoutChordContext),
      HarmonicFunctionWithSopranoInfo(subdominant, sopranoNote = anyNoteWithoutChordContext)
    )
    val connectionDS = Connection(
      HarmonicFunctionWithSopranoInfo(subdominant, sopranoNote = anyNoteWithoutChordContext),
      HarmonicFunctionWithSopranoInfo(dominant, sopranoNote = anyNoteWithoutChordContext)
    )
    val rule = ChangeFunctionConnectionRule()
    rule.isNotBroken(connectionSD) shouldBe true
    rule.isNotBroken(connectionDT) shouldBe true
    rule.isNotBroken(connectionTS) shouldBe true
    rule.isBroken(connectionTD) shouldBe true
    rule.isBroken(connectionST) shouldBe true
    rule.isBroken(connectionDS) shouldBe true
  }

  test("Soprano jump rule test") {
    val connectionWithJumpWithNotChange = Connection(
      HarmonicFunctionWithSopranoInfo(dominant, MeasurePlace.BEGINNING, sopranoNote = NoteWithoutChordContext(71, B)),
      HarmonicFunctionWithSopranoInfo(dominant, MeasurePlace.UPBEAT, sopranoNote = NoteWithoutChordContext(74, D))
    )
    val connectionWithJumpWithChange = Connection(
      HarmonicFunctionWithSopranoInfo(
        subdominant,
        MeasurePlace.BEGINNING,
        sopranoNote = NoteWithoutChordContext(69, A)
      ),
      HarmonicFunctionWithSopranoInfo(tonic, MeasurePlace.UPBEAT, sopranoNote = NoteWithoutChordContext(72, C))
    )
    val connectionWithNoJumpWithNotChange = Connection(
      HarmonicFunctionWithSopranoInfo(tonic, MeasurePlace.BEGINNING, sopranoNote = NoteWithoutChordContext(72, C)),
      HarmonicFunctionWithSopranoInfo(subdominant, MeasurePlace.UPBEAT, sopranoNote = NoteWithoutChordContext(72, C))
    )
    val connectionWithNoJumpWithChange = Connection(
      HarmonicFunctionWithSopranoInfo(
        subdominant,
        MeasurePlace.BEGINNING,
        sopranoNote = NoteWithoutChordContext(72, C)
      ),
      HarmonicFunctionWithSopranoInfo(subdominant, MeasurePlace.UPBEAT, sopranoNote = NoteWithoutChordContext(72, C))
    )
    val rule = JumpRule()
    rule.isNotBroken(connectionWithJumpWithNotChange) shouldBe true
    rule.isBroken(connectionWithJumpWithChange) shouldBe true
    rule.isNotBroken(connectionWithNoJumpWithNotChange) shouldBe true
    rule.isBroken(connectionWithNoJumpWithChange) shouldBe true
  }

  test("Second relation test") {
    val connection1 = Connection(
      HarmonicFunctionWithSopranoInfo(tonicVI, sopranoNote = anyNoteWithoutChordContext),
      HarmonicFunctionWithSopranoInfo(dominant, sopranoNote = anyNoteWithoutChordContext)
    )
    val connection2 = Connection(
      HarmonicFunctionWithSopranoInfo(tonic, sopranoNote = anyNoteWithoutChordContext),
      HarmonicFunctionWithSopranoInfo(subdominant, sopranoNote = anyNoteWithoutChordContext)
    )
    val rule = SecondRelationRule
    rule.isNotBroken(connection1)
    rule.isBroken(connection2)
  }

  test("Change function on downbeat rule test") {
    val connection1 = Connection(
      HarmonicFunctionWithSopranoInfo(tonic, MeasurePlace.UPBEAT, sopranoNote = anyNoteWithoutChordContext),
      HarmonicFunctionWithSopranoInfo(dominant, sopranoNote = anyNoteWithoutChordContext)
    )
    val connection2 = Connection(
      HarmonicFunctionWithSopranoInfo(tonic, MeasurePlace.DOWNBEAT, sopranoNote = anyNoteWithoutChordContext),
      HarmonicFunctionWithSopranoInfo(subdominant, sopranoNote = anyNoteWithoutChordContext)
    )
    val rule = ChangeFunctionOnDownBeatRule()
    rule.isNotBroken(connection2)
    rule.isBroken(connection1)
  }

  test("Secondary dominant connection rule test") {
    val hf = HarmonicFunction(DOMINANT, key = Some(keyF))
    val connection1 = Connection(
      HarmonicFunctionWithSopranoInfo(subdominant, sopranoNote = anyNoteWithoutChordContext),
      HarmonicFunctionWithSopranoInfo(hf, sopranoNote = anyNoteWithoutChordContext)
    )
    val connection2 = Connection(
      HarmonicFunctionWithSopranoInfo(dominant, sopranoNote = anyNoteWithoutChordContext),
      HarmonicFunctionWithSopranoInfo(hf, sopranoNote = anyNoteWithoutChordContext)
    )
    val rule = SecondaryDominantConnectionRule(keyC)
    rule.isNotBroken(connection1) shouldBe true
    rule.isBroken(connection2) shouldBe true
  }

  test("Fourth chords rule test") {
    val connection1 = Connection(
      HarmonicFunctionWithSopranoInfo(dominant7, sopranoNote = anyNoteWithoutChordContext),
      HarmonicFunctionWithSopranoInfo(dominant, sopranoNote = anyNoteWithoutChordContext)
    )
    val connection2 = Connection(
      HarmonicFunctionWithSopranoInfo(dominant, sopranoNote = anyNoteWithoutChordContext),
      HarmonicFunctionWithSopranoInfo(dominant7, sopranoNote = anyNoteWithoutChordContext)
    )
    val rule = FourthChordsRule()
    rule.isNotBroken(connection1)
    rule.isBroken(connection2)
  }

  private var punishmentRatios          = ChordRules.getValues.map(_ -> 1.0).toMap
  private var adaptiveChordRulesChecker = AdaptiveRulesChecker(punishmentRatios)

  test("Adaptive chord rules checker init with all hard rules test") {
    adaptiveChordRulesChecker.getHardRules.length shouldBe 11
  }

  test("Adaptive chord rules checker init with some soft rules test") {
    val ch1        = Chord(Note(72, C, prime), Note(67, G, fifth), Note(64, E, third), Note(48, C, prime), tonic)
    val ch2        = Chord(Note(77, F, prime), Note(69, A, third), Note(60, C, fifth), Note(53, F, prime), subdominant)
    val connection = Connection(ch2, ch1)
    val rule1Opt   = adaptiveChordRulesChecker.getHardRules.find(_.isInstanceOf[ParallelOctavesRule])
    rule1Opt.isDefined shouldBe true
    val rule1       = rule1Opt.get
    val firstResult = rule1.evaluate(connection)
    punishmentRatios = punishmentRatios.updated(ChordRules.ParallelFifths, 0.5)
    punishmentRatios = punishmentRatios.updated(ChordRules.ParallelOctaves, 0.5)
    adaptiveChordRulesChecker = AdaptiveRulesChecker(punishmentRatios)
    val rule2Opt = adaptiveChordRulesChecker.getSoftRules.find(_.isInstanceOf[ParallelOctavesRule])
    rule2Opt.isDefined shouldBe true
    val rule2        = rule2Opt.get
    val secondResult = rule2.evaluate(connection)
    adaptiveChordRulesChecker.getHardRules.length shouldBe 9
    secondResult shouldBe firstResult * 0.5
  }
}
