package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.bass.{AlterationType, BassSymbol}

object BassSymbolMapper extends Mapper[BassSymbol, String] {
  override def mapToModel(dto: String): BassSymbol = {
    val altType = if (!dto.last.isDigit) {
      dto.last.toString match {
        case AlterationType.NATURAL.value => Some(AlterationType.NATURAL)
        case AlterationType.SHARP.value   => Some(AlterationType.SHARP)
        case AlterationType.FLAT.value    => Some(AlterationType.FLAT)
        case unknown                      => throw new InternalError("Unknown alteration type: " + unknown)
      }
    } else None
    BassSymbol(
      dto.takeWhile(_.isDigit).toInt,
      altType
    )
  }

  override def mapToDTO(model: BassSymbol): String = {
    model.mapToChordComponentSymbol().toString
  }
}
