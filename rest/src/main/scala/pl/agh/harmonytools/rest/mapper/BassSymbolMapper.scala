package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.bass.{AlterationType, BassSymbol}
import pl.agh.harmonytools.error.UnexpectedInternalError
import pl.agh.harmonytools.rest.dto.BassSymbolDto

object BassSymbolMapper extends Mapper[BassSymbol, BassSymbolDto] {
  override def mapToModel(dto: BassSymbolDto): BassSymbol = {
    val altType = dto.alteration match {
      case Some(alt) =>
        alt match {
          case AlterationType.NATURAL.value => AlterationType.NATURAL
          case AlterationType.SHARP.value   => AlterationType.SHARP
          case AlterationType.FLAT.value    => AlterationType.FLAT
          case AlterationType.EMPTY.value   => AlterationType.EMPTY
          case unknown                      => throw UnexpectedInternalError(s"Unknown alteration type: $unknown")
        }
      case None => AlterationType.EMPTY
    }
    BassSymbol(
      dto.component.getOrElse(3),
      altType
    )
  }

  override def mapToDTO(model: BassSymbol): BassSymbolDto = {
    val alterationType = model.alteration match {
      case AlterationType.SHARP   => Some(AlterationType.SHARP.value)
      case AlterationType.FLAT    => Some(AlterationType.FLAT.value)
      case AlterationType.NATURAL => Some(AlterationType.NATURAL.value)
      case AlterationType.EMPTY   => None
    }
    BassSymbolDto(Some(model.component), alterationType)
  }
}
