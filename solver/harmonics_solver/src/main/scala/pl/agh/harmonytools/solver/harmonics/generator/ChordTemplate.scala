package pl.agh.harmonytools.solver.harmonics.generator

import pl.agh.harmonytools.model.chord.ChordComponent

case class ChordTemplate(fourPartChordComponents: FourPartChordComponents, needToAdd: List[ChordComponent])
