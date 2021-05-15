package pl.agh.harmonytools.bass

import pl.agh.harmonytools.error.UnexpectedInternalError
import pl.agh.harmonytools.model.chord.ChordComponent
import pl.agh.harmonytools.model.harmonicfunction.{Delay, HarmonicFunction}
import pl.agh.harmonytools.model.harmonicfunction.builder.{HarmonicFunctionBasicBuilder, HarmonicFunctionBuilder}
import pl.agh.harmonytools.model.key.Mode.{MAJOR, MINOR}
import pl.agh.harmonytools.model.scale.ScaleDegree.{II, III, VI}
import pl.agh.harmonytools.model.util.ChordComponentManager

case class BassHarmonicFunctionBuilder() extends HarmonicFunctionBasicBuilder {

  def copy(): BassHarmonicFunctionBuilder = {
    val builder = BassHarmonicFunctionBuilder()
    builder.withDelay(delay)
    baseFunction match {
      case Some(value) => builder.withBaseFunction(value)
      case None        =>
    }
    builder.withOmit(omit)
    builder.withExtra(extra)
    builder.withIsDown(isDown)
    builder.withSystem(system)
    builder.withMode(mode)
    builder.withIsRelatedBackwards(false)
    builder.withInversion(getInversion)
    position match {
      case Some(value) => builder.withPosition(value)
      case None        =>
    }
    key match {
      case Some(value) => builder.withKey(value)
      case None        =>
    }
    builder
  }

  def removeInversionFromExtra(inversion: Int): Unit =
    withExtra(getExtra.filter(_.baseComponent != inversion))

  def handleDownChord(): Unit = {
    if (extra.exists(_.chordComponentString == "1>")) {
      withOmit(Set.empty)
      withMode(MINOR)
      withIsDown(true)
      var extra = Set.empty[ChordComponent]
      for (e <- getExtra) {
        if (e.baseComponent > 5)
          extra = extra + e.getIncreasedByHalfTone
      }
      withExtra(extra.map(e => ChordComponentManager.chordComponentWithIsDown(e)))
      position match {
        case Some(value) => withPosition(ChordComponentManager.chordComponentWithIsDown(value.getIncreasedByHalfTone))
        case None        =>
      }
      withInversion(ChordComponentManager.chordComponentWithIsDown(getInversion.getIncreasedByHalfTone))
      withDelay(
        getDelay.map(d =>
          Delay(
            ChordComponentManager.chordComponentWithIsDown(d.first.getIncreasedByHalfTone),
            ChordComponentManager.chordComponentWithIsDown(d.second.getIncreasedByHalfTone)
          )
        )
      )
    }
  }

  def fixExtraAfterModeChange(): Unit = {
    if (mode == MAJOR && extra.exists(_.chordComponentString == "3") && !List(II, III, VI).contains(getDegree))
      withExtra(extra.filterNot(_.chordComponentString == "3"))
    if (mode == MINOR && extra.exists(_.chordComponentString == "3>") && !List(II, III, VI).contains(getDegree))
      withExtra(extra.filterNot(_.chordComponentString == "3>"))
  }

  def addOmit3ForS2IfNecessary(): Unit = {
    if (
      getDegree == II && getMode == MINOR && getInversion.chordComponentString == "3" && getOmit.exists(
        _.chordComponentString == "3>"
      )
    )
      withOmit(getOmit + getCC("3>"))
  }

  def testThird: Boolean = {
    val test3 = getThird.chordComponentString == "3"
    if (isDown)
      test3 && (getThird.semitonesNumber == 3)
    else
      test3 && (getThird.semitonesNumber == 4)
  }

  private def handleThirdAlterationIn236Chords(): Unit = {
    if (List(II, III, VI).contains(getDegree) && (getMode == MAJOR || getDegree == II)) {
      if (omit.exists(_.chordComponentString == "3") && extra.exists(_.chordComponentString == "3<")) {
        omit = omit.filterNot(_.chordComponentString == "3") + getCC("3>")
        extra = extra.filterNot(_.chordComponentString == "3>") + getCC("3")
      }
    }
  }

  private def handleFifthAlterationIn236Chords(): Unit = {
    if (
      getMode == MINOR && getDegree == II
      && extra.exists(_.chordComponentString == "5<")
    ) {
      if (omit.exists(_.chordComponentString == "5"))
        omit = omit.filterNot(_.chordComponentString == "5")
      if (!omit.exists(_.chordComponentString == "5>"))
        omit = omit + getCC("5>")
      extra = extra.filterNot(_.chordComponentString == "5<") + getCC("5")
    }
  }

  def handle35AlterationsIn236Chords(): Unit = {
    handleThirdAlterationIn236Chords()
    handleFifthAlterationIn236Chords()
  }

  override def initializeHarmonicFunction(): HarmonicFunction = {
    HarmonicFunction(
      baseFunction.getOrElse(
        throw UnexpectedInternalError("Base function has to be defined when initializing HarmonicFunction")
      ),
      degree,
      position,
      inversion,
      delay,
      extra,
      omit,
      isDown,
      system,
      mode,
      key,
      isRelatedBackwards
    )
  }
}
