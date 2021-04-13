package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.bass.{AlterationType, BassSymbol}

object BassSymbolMapper extends Mapper[BassSymbol, String] {
  override def mapToModel(dto: String): BassSymbol = {
    val altType = if (!dto.last.isDigit) {
      dto.last.toString match {
        case AlterationType.NATURAL.value => AlterationType.NATURAL
        case AlterationType.SHARP.value   => AlterationType.SHARP
        case AlterationType.FLAT.value    => AlterationType.FLAT
        case unknown                      => throw new InternalError("Unknown alteration type: " + unknown)
      }
    } else AlterationType.EMPTY
    BassSymbol(
      dto.takeWhile(_.isDigit).toInt,
      altType
    )
  }

  override def mapToDTO(model: BassSymbol): String = {
    model.toString
  }
}
