package pl.agh.harmonytools.solver.walking.generator

import pl.agh.harmonytools.algorithm.generator.LayerGenerator
import pl.agh.harmonytools.solver.walking.generator.BassGenerator.{MaxPitch, MinPitch}

case class BassGenerator() extends LayerGenerator[WalkingBassNote, BassGeneratorInput] {
  override def generate(input: BassGeneratorInput): List[WalkingBassNote] =
    input.possibleNotes.flatMap { n =>
      (n.pitch % 12 to MaxPitch by 12)
        .dropWhile(_ < MinPitch)
        .map(i => WalkingBassNote(n.copy(pitch = i), input))
    }.toList
      .filter { n =>
        if (input.basicChordNotes.forall(_.chordComponent.baseComponent != 3))
          n.note.chordComponent.baseComponent != 3
        else true
      }
}

object BassGenerator {

  val MinPitch = 40
  val MaxPitch = 67
}
