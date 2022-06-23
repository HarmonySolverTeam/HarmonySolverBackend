package pl.agh.harmonytools.solver.soprano.test

import net.liftweb.json.{DefaultFormats, Serialization}
import pl.agh.harmonytools.solver.soprano.test.ExercisesDef._
import pl.agh.harmonytools.utils.Extensions.ExtendedLocalDateTime
import smile.License

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}
import java.time.LocalDateTime

object SopranoSolverTest extends App {
  new License("SMILE LICENSE 6496c72d 3bb877f6 fb9bddc1 " + "THIS IS AN ACADEMIC LICENSE AND CAN BE USED " + "SOLELY FOR ACADEMIC RESEARCH AND TEACHING, " + "AS DEFINED IN THE BAYESFUSION ACADEMIC " + "SOFTWARE LICENSING AGREEMENT. " + "Serial #: 43an42u232fz4pgpvfc2vubye " + "Issued for: Jakub Sroka (jakubsroka3@gmail.com) " + "Academic institution: AGH the University of Science and Technology " + "Valid until: 2022-07-20 " + "Issued by BayesFusion activation server", Array[Byte](53, -13, -25, 86, -48, 63, 97, 86, -108, -13, 86, 101, 80, -63, -15, 7, -17, -33, -107, -77, -82, -81, -97, -121, -96, -72, -127, -121, 7, -95, 33, 39, -96, 74, -49, 101, 58, 55, 115, -94, 40, 0, -96, -72, 64, 112, 65, 69, -111, 26, -26, 55, -92, -68, -46, 124, 121, 99, -35, 48, 116, -60, 9, 119))

//  val exerciseNum = args(0).toInt
  for (exerciseNum <- 8 until 10) {
    val (name, exercise) = (majorExercises ++ minorExercises ++ majorWithAlterationsExercises ++ minorWithAlterationsExercises).toList(exerciseNum)
    implicit val formats: DefaultFormats.type = DefaultFormats
    val fileName = s"${LocalDateTime.now.dateString}.json"
    val results = SolversDef.solveByAll(name, exercise)
    Files.write(
      Paths.get(s"solver/soprano_solver_test/src/main/resources/ratings/$name/$fileName"),
      Serialization.write(results).getBytes(StandardCharsets.UTF_8)
    )
    println(s"Exercise ${exerciseNum+1} processed.")
  }
}
