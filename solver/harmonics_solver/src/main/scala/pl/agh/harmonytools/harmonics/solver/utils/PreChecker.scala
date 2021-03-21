package pl.agh.harmonytools.harmonics.solver.utils

import pl.agh.harmonytools.algorithm.evaluator.Connection
import pl.agh.harmonytools.harmonics.solver.evaluator.ChordRulesChecker
import pl.agh.harmonytools.harmonics.solver.generator.{ChordGenerator, ChordGeneratorInput}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.note.Note

object PreChecker {
  def run(
    harmonicFunctions: List[HarmonicFunction],
    chordGenerator: ChordGenerator,
    bassLine: Option[List[Note]] = None,
    sopranoLine: Option[List[Note]] = None
  ): Unit = {
    if (sopranoLine.isEmpty) {
      val indexes = getHarmonicFunctionsIndexesBasedOnDelays(harmonicFunctions)
      checkForImpossibleConnections(harmonicFunctions, chordGenerator, bassLine, indexes)
    }
  }

  private def getHarmonicFunctionsIndexesBasedOnDelays(harmonicFunctions: List[HarmonicFunction]): List[Int] = {
    var result       = List.empty[Int]
    var currentIndex = 1
    var a            = 0
    while (a < harmonicFunctions.length) {
      result = result :+ currentIndex
      if (harmonicFunctions(a).delay.nonEmpty) {
        result = result :+ currentIndex
        a += 1
      }
      a += 1
      currentIndex += 1
    }
    result
  }

  def checkForImpossibleConnections(
    harmonicFunctions: List[HarmonicFunction],
    chordGenerator: ChordGenerator,
    bassLine: Option[List[Note]],
    indexes: List[Int]
  ): Unit = {
    var currentChords = List.empty[Chord]
    var prevChords = List.empty[Chord]
    var goodCurrentChords = List.empty[Chord]
    var usedCurrentChords = List.empty[Boolean]
    val rulesChecker = ChordRulesChecker(isFixedBass = bassLine.isDefined)
    for (i <- harmonicFunctions.indices) {
      var allConnections = 0
      if (i != 0) {
        prevChords = goodCurrentChords
      }
      goodCurrentChords = List.empty[Chord]
      usedCurrentChords = List.empty[Boolean]
      currentChords = bassLine match {
        case Some(bl) =>
          chordGenerator.generate(ChordGeneratorInput(harmonicFunctions(i), i != 0, bassNote = Some(bl(i))))
        case None =>
          chordGenerator.generate(ChordGeneratorInput(harmonicFunctions(i), i != 0))
      }

      if (currentChords.isEmpty) {
        throw PreCheckerError("Could not generate any chord for harmonic function:" + indexes(i))
      }
      usedCurrentChords = currentChords.map(_ => false)

      rulesChecker.initializeBrokenRulesCounter()

      if (i != 0) {
        for (prevChord <- prevChords) {
          for (i <- currentChords.indices) {
            val currentChord = currentChords(i)
            allConnections += 1
            if (!usedCurrentChords(i)) {
              if (rulesChecker.getNumberOfRulesBrokenBy(Connection(currentChord, prevChord)) == 0) {
                goodCurrentChords = goodCurrentChords :+ currentChord
                usedCurrentChords = usedCurrentChords.updated(i, true)
              }
            }
          }
        }
      } else {
        for (currentChord <- currentChords) {
          allConnections += 1
          goodCurrentChords = goodCurrentChords :+ currentChord
        }
      }

      if (goodCurrentChords.isEmpty) {
        val brokenRulesInfo = rulesChecker.getBrokenRulesCounter.getBrokenRulesInfo(allConnections)

        if (i != 0) {
          throw PreCheckerError(
            s"Could not find valid connection between chords ${indexes(i - 1)} and " +
              s"${indexes(i)}\nChord ${indexes(i - 1)}\n${harmonicFunctions(i - 1)}\n\n" +
              s"Chord ${indexes(i)}\n${harmonicFunctions(i)}\n\nBroken rules for all ${allConnections} " +
              s"possible connections between these chords:\n${brokenRulesInfo}"
          )
        } else {
          throw PreCheckerError(
            s"Could not generate any correct chord for first chord\n${harmonicFunctions(i)}\nBroken rules:\n${brokenRulesInfo}"
          )
        }
      }
    }
  }
}

case class PreCheckerError(msg: String) extends RuntimeException(msg)
