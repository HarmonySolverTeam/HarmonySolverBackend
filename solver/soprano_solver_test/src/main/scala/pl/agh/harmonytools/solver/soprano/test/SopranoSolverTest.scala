package pl.agh.harmonytools.solver.soprano.test

import net.liftweb.json.{DefaultFormats, Serialization}
import pl.agh.harmonytools.solver.soprano.test.ExercisesDef._
import pl.agh.harmonytools.utils.Extensions.ExtendedLocalDateTime

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}
import java.time.LocalDateTime

object SopranoSolverTest extends App {
  val exerciseNum = args(0).toInt
  val (name, exercise) = (majorExercises ++ minorExercises ++ majorWithAlterationsExercises ++ minorWithAlterationsExercises).toList(exerciseNum)
  implicit val formats: DefaultFormats.type = DefaultFormats
  val fileName                                  = s"${LocalDateTime.now.dateString}.json"
  val results = SolversDef.solveByAll(name, exercise)
  Files.write(
    Paths.get(s"solver/soprano_solver_test/src/main/resources/ratings/$name/$fileName"),
    Serialization.write(results).getBytes(StandardCharsets.UTF_8)
  )

}
