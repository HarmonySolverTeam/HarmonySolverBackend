package pl.agh.harmonytools.finder

import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.measure.{Measure, Meter}

class Chorale(val measures: List[Measure[ChoraleChord]], val key: Key, val meter: Meter)
