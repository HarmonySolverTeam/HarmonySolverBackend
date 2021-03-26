package pl.agh.harmonytools.solver.harmonics.utils

import pl.agh.harmonytools.model.chord.ChordComponent
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.key.Mode.MAJOR
import pl.agh.harmonytools.model.util.ChordComponentManager
import pl.agh.harmonytools.utils.IntervalUtils

case class ExerciseCorrector(
  exerciseKey: Key,
  harmonicFunctions: List[HarmonicFunction],
  isDefinedBassLine: Boolean = false
) {

  private def getKey(hf: HarmonicFunction): Key = {
    hf.key match {
      case Some(value) => value
      case None => exerciseKey
    }
  }

  private def resolveRevolution(hf: HarmonicFunction): ChordComponent = {
    val key = getKey(hf)
    if (IntervalUtils.getThirdMode(key, hf.degree) == MAJOR)
      ChordComponentManager.chordComponentFromString("3")
    else ChordComponentManager.chordComponentFromString("3>")
  }



  private var resultHarmonicFunctions: List[HarmonicFunction] = harmonicFunctions

  def handleDominantConnectionsWith7InBass(dominantIndex: Int, tonicIndex: Int): Boolean = {
    if (isDefinedBassLine) return false
    val dominantHarmonicFunction = resultHarmonicFunctions(dominantIndex)
    val tonicHarmonicFunction    = resultHarmonicFunctions(tonicIndex)
    if (
      dominantHarmonicFunction.isInDominantRelation(tonicHarmonicFunction) &&
      dominantHarmonicFunction.revolution.baseComponent == 7 &&
      tonicHarmonicFunction.revolution.baseComponent == 1
    ) {
      val revolution = resolveRevolution(tonicHarmonicFunction)
      resultHarmonicFunctions =
        resultHarmonicFunctions.updated(tonicIndex, tonicHarmonicFunction.copy(revolution = revolution))
      return tonicHarmonicFunction.delay.nonEmpty
    }
    if (dominantHarmonicFunction.isInDominantRelation(tonicHarmonicFunction) &&
      tonicHarmonicFunction.revolution.baseComponent == 7 &&
      dominantHarmonicFunction.revolution.baseComponent == 1) {
      val revolution = resolveRevolution(dominantHarmonicFunction)
      resultHarmonicFunctions =
        resultHarmonicFunctions.updated(dominantIndex, dominantHarmonicFunction.copy(revolution = revolution))
      return false
    }
    false
  }

  def handleChopinTonicConnection(chopinIndex: Int, tonicIndex: Int): Unit = {
    val chopinHarmonicFunction = resultHarmonicFunctions(chopinIndex)
    val tonicHarmonicFunction = resultHarmonicFunctions(tonicIndex)
    if (chopinHarmonicFunction.isChopin && chopinHarmonicFunction.isInDominantRelation(tonicHarmonicFunction)) {
      if (!tonicHarmonicFunction.omit.contains(tonicHarmonicFunction.getFifth)) {
        resultHarmonicFunctions = resultHarmonicFunctions.updated(tonicIndex, tonicHarmonicFunction.copy(omit = tonicHarmonicFunction.omit + tonicHarmonicFunction.getFifth))
      }
    }
  }

  def makeChordsIncompleteToAvoidConcurrent5(startIndex: Int, endIndex: Int): Unit = {
    var changeCurrentChord = (endIndex - startIndex) % 2 == 0
    if (changeCurrentChord && startIndex > 0) {
      val startHf = resultHarmonicFunctions(startIndex-1)
      if (startHf.omit.contains(startHf.getFifth) && startHf.extra.contains(startHf.getFifth))
        changeCurrentChord = !changeCurrentChord
    }
    for (i <- startIndex until endIndex) {
      if (changeCurrentChord) {
        val hf = resultHarmonicFunctions(i)
        if (hf.position.contains(hf.getFifth)) {
          resultHarmonicFunctions = resultHarmonicFunctions.updated(i, hf.copy(revolution = hf.getThird, omit = hf.omit + hf.getPrime))
        } else {
          resultHarmonicFunctions = resultHarmonicFunctions.updated(i, hf.copy(omit = hf.omit + hf.getFifth))
        }
      }
      changeCurrentChord = !changeCurrentChord
    }
  }

  def run(): List[HarmonicFunction] = {
    var startIndexOfChain = -1
    var insideChain       = false
    for (i <- resultHarmonicFunctions.indices) {
      if (i < resultHarmonicFunctions.length-1) {
        if (handleDominantConnectionsWith7InBass(i, i+1)) {
          val hf = resultHarmonicFunctions(i+2)
          val revolution = resolveRevolution(hf)
          resultHarmonicFunctions = resultHarmonicFunctions.updated(i+2, hf.copy(revolution = revolution))
        }
        handleChopinTonicConnection(i, i+1)
        if (resultHarmonicFunctions(i).isInDominantRelation(resultHarmonicFunctions(i+1)) &&
          resultHarmonicFunctions(i).revolution.baseComponent == 1 &&
          resultHarmonicFunctions(i+1).revolution.baseComponent == 1 &&
          resultHarmonicFunctions(i).extra.exists(_.baseComponent == 7) &&
          resultHarmonicFunctions(i).omit.isEmpty && resultHarmonicFunctions(i+1).omit.isEmpty) {
          if (!insideChain) {
            startIndexOfChain = i
            insideChain = true
          }
        } else {
          if (insideChain) {
            insideChain = false
            makeChordsIncompleteToAvoidConcurrent5(startIndexOfChain, i+1)
          }
        }
      } else {
        if (insideChain) {
          insideChain = false
          makeChordsIncompleteToAvoidConcurrent5(startIndexOfChain, i+1)
        }
      }
    }
    resultHarmonicFunctions
  }
}
