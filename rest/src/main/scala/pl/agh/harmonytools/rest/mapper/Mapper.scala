package pl.agh.harmonytools.rest.mapper

trait Mapper[M, DTO] {
  def mapToModel(dto: DTO): M
  def mapToDTO(model: M): DTO
}
