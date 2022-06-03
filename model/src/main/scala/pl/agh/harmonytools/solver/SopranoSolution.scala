package pl.agh.harmonytools.solver

import net.liftweb.json.{DefaultFormats, Serialization}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.exercise.Exercise
import pl.agh.harmonytools.model.note.NoteWithoutChordContext
import pl.agh.harmonytools.utils.Extensions.ExtendedLocalDateTime

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}
import java.time.LocalDateTime
import scala.sys.process.Process

case class SopranoSolution(
  exercise: Exercise[NoteWithoutChordContext],
  rating: Double,
  chords: List[Chord],
  success: Boolean = true,
  minEpoch: Option[Int] = None
) extends ExerciseSolution[NoteWithoutChordContext](exercise, rating, chords, success) {
  def save(path: String): String = {
    implicit val formats: DefaultFormats.type = DefaultFormats
    val name                                  = s"${LocalDateTime.now.dateString}_${rating.toInt}.json"
    Files.write(
      Paths.get(s"$path/$name"),
      Serialization.write(this).getBytes(StandardCharsets.UTF_8)
    )
    name
  }
}

object SopranoSolution {
  def showSolution(name: String, path: String): Unit = {
    Process(s"python score_printer.py $name $path", new File(".")).!!
  }

  def saveAndShowSolution(solution: SopranoSolution, path: String): Unit = {
    showSolution(solution.save(path), path)
  }
}
