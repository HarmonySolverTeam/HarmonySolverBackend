package pl.agh.harmonytools.bass

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
      case None =>
    }
    builder.withOmit(omit)
    builder.withExtra(extra)
    builder.withIsDown(isDown)
    builder.withSystem(system)
    builder.withMode(mode)
    builder.withIsRelatedBackwards(false)
    builder.withRevolution(getRevolution)
    position match {
      case Some(value) => builder.withPosition(value)
      case None =>
    }
    key match {
      case Some(value) => builder.withKey(value)
      case None =>
    }
    builder
  }

  def removeRevolutionFromExtra(revolution: Int): Unit = {
    withExtra(getExtra.filter(_.baseComponent != revolution))
  }

  def handleDownChord(): Unit = {
    if (extra.exists(_.chordComponentString == "1>")) {
      withOmit(List.empty)
      withMode(MINOR)
      withIsDown(true)
      var extra = List.empty[ChordComponent]
      for (e <- getExtra) {
        if (e.baseComponent > 5) {
          extra = extra :+ e.getIncreasedByHalfTone
        }
      }
      withExtra(extra)
      position match {
        case Some(value) => withPosition(value.getIncreasedByHalfTone)
        case None =>
      }
      withRevolution(getRevolution.getIncreasedByHalfTone)
      withDelay(getDelay.map(d => Delay(d.first.getIncreasedByHalfTone, d.second.getIncreasedByHalfTone)))
    }
  }

  def fixExtraAfterModeChange(): Unit = {
    if (mode == MAJOR && extra.exists(_.chordComponentString == "3") && !List(II, III, VI).contains(getDegree)) {
      withExtra(extra.filterNot(_.chordComponentString == "3"))
    }
    if (mode == MINOR && extra.exists(_.chordComponentString == "3>") && !List(II, III, VI).contains(getDegree)) {
      withExtra(extra.filterNot(_.chordComponentString == "3>"))
    }
  }

  def addOmit3ForS2IfNecessary(): Unit = {
    if (getDegree == II && getMode == MINOR && getRevolution.chordComponentString == "3" && getOmit.exists(_.chordComponentString == "3>")) {
      withOmit(getOmit :+ getCC("3>"))
    }
  }

  def testThird: Boolean = {
    val test3 = getThird.chordComponentString == "3"
    if (isDown) {
      test3 && (getThird.semitonesNumber == 3)
    } else {
      test3 && (getThird.semitonesNumber == 4)
    }
  }

  private def handleThirdAlterationIn236Chords(): Unit = {
    if (List(II, III, VI).contains(getDegree) && (getMode == MAJOR || getDegree == II)) {
      if (omit.exists(_.chordComponentString == "3") && extra.exists(_.chordComponentString == "3<")) {
        omit = omit.filterNot(_.chordComponentString == "3") :+ getCC("3>")
        extra = extra.filterNot(_.chordComponentString == "3>") :+ getCC("3")
      }
    }
  }

  private def handleFifthAlterationIn236Chords(): Unit = {
    if (getMode == MINOR && getDegree == II) {
      if (extra.exists(_.chordComponentString == "5<"))
        if (omit.exists(_.chordComponentString == "5"))
          omit = omit.filterNot(_.chordComponentString == "5")
      if (!omit.exists(_.chordComponentString == "5>"))
        omit = omit :+ getCC("5>")
      extra = extra.filterNot(_.chordComponentString == "5<") :+ getCC("5")
    }
  }

  def handle35AlterationsIn236Chords(): Unit = {
    handleThirdAlterationIn236Chords()
    handleFifthAlterationIn236Chords()
  }

  override def initializeHarmonicFunction(): HarmonicFunction = {
    if (isDown) {
      HarmonicFunction(
        baseFunction.getOrElse(sys.error("Base function has to be defined when initializing HarmonicFunction")),
        getDegree,
        position.flatMap(cc => Some(ChordComponentManager.chordComponentWithIsDown(cc))),
        ChordComponentManager.chordComponentWithIsDown(getRevolution),
        delay.map(d => Delay(ChordComponentManager.chordComponentWithIsDown(d.first), ChordComponentManager.chordComponentWithIsDown(d.second))),
        extra.map(e => ChordComponentManager.chordComponentWithIsDown(e)),
        omit.map(o => ChordComponentManager.chordComponentWithIsDown(o)),
        isDown,
        system,
        mode,
        key,
        isRelatedBackwards
      )
    } else {
      HarmonicFunction(
        baseFunction.getOrElse(sys.error("Base function has to be defined when initializing HarmonicFunction")),
        degree,
        position,
        revolution,
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
}
