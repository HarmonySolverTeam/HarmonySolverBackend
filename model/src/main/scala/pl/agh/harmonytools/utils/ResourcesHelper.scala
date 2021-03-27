package pl.agh.harmonytools.utils

import scala.io.Source

trait ResourcesHelper {
  protected def getFileContent(filePath: String): String = {
    val source = Source.fromURL(getClass.getResource(filePath))
    try source.mkString
    finally source.close()
  }
}
