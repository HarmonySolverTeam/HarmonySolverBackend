package pl.agh.harmonytools.utils

import java.io.File
import scala.io.Source

trait ResourcesHelper {
  protected def getFileContent(filePath: String): String = {
    val source = Source.fromURL(getClass.getResource(filePath))
    try source.mkString
    finally source.close()
  }

  protected def getFileContent(file: File): String = {
    val source = Source.fromFile(file)
    try source.mkString
    finally source.close()
  }
}
