package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.scale.ScaleDegree
import pl.agh.harmonytools.model.scale.ScaleDegree.Degree
import pl.agh.harmonytools.rest.dto.HarmonicFunctionDto.Degree

object DegreeMapper extends Mapper[ScaleDegree.Degree, Degree.Value] {
  override def mapToModel(dto: Degree.Value): ScaleDegree.Degree = {
    dto match {
      case Degree.I   => ScaleDegree.I
      case Degree.II  => ScaleDegree.II
      case Degree.III => ScaleDegree.III
      case Degree.IV  => ScaleDegree.IV
      case Degree.V   => ScaleDegree.V
      case Degree.VI  => ScaleDegree.VI
      case Degree.VII => ScaleDegree.VII
    }
  }

  override def mapToDTO(model: Degree): Degree.Value = {
    model match {
      case ScaleDegree.I   => Degree.I
      case ScaleDegree.II  => Degree.II
      case ScaleDegree.III => Degree.III
      case ScaleDegree.IV  => Degree.IV
      case ScaleDegree.V   => Degree.V
      case ScaleDegree.VI  => Degree.VI
      case ScaleDegree.VII => Degree.VII
    }
  }
}
