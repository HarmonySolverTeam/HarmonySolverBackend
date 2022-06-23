package pl.agh.harmonytools.model.measure

import pl.agh.harmonytools.error.RequirementChecker

case class Measure[T <: MeasureContent](meter: Meter, contents: List[T]) {
//  RequirementChecker.isRequired(
//    contents.exists(!_.supportsDuration) || contents.map(_.duration).sum == meter.asDouble,
//    MeasureParseError("Measure content doesn't match to the measure's meter")
//  )

  def contentCount: Int = contents.length
}
