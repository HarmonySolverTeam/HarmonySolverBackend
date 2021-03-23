package pl.agh.harmonytools.harmonics.solver.generator

import pl.agh.harmonytools.model.chord.ChordComponent

case class ChordTemplate(fourPartChordComponents: FourPartChordComponents, needToAdd: List[ChordComponent])
