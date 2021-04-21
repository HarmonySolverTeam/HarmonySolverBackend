package pl.agh.harmonytools.rest

import akka.util.ByteString
import pl.agh.harmonytools.error.HarmonySolverError
import play.api.http.{DefaultHttpErrorHandler, Writeable}
import play.api.libs.json.JsResultException
import play.api.mvc.Results._
import play.api.mvc.{RequestHeader, Result}

import scala.concurrent.Future

class ErrorHandler extends DefaultHttpErrorHandler {
  private implicit val hsErrorWriteable: Writeable[HarmonySolverError] = {
    Writeable.apply(h => {
        h.details match {
          case Some(details) =>
            ByteString.apply(
              s"""{
                 |"message": "${h.message}",
                 |"source": "${h.source}",
                 |"details": "${details.replace("\n","\\n")}"
                 |}""".stripMargin
            )
          case None =>
            ByteString.apply(
              s"""{
                 |"message": "${h.message}",
                 |"source": "${h.source}"
                 |}""".stripMargin
            )
        }
      }, None)
  }

  override def onServerError(request: RequestHeader, e: Throwable): Future[Result] = e match {
    case hsError: HarmonySolverError =>
      Future.successful(BadRequest(hsError)) //todo throw json with source and details?
    case _: OpenApiExceptions.MissingRequiredParameterException =>
      Future.successful(BadRequest(e.getMessage))
    case _: JsResultException =>
      Future.successful(BadRequest(e.getMessage))
    case _ =>
      // Handles dev mode properly, or otherwise returns internal server error in production mode
      super.onServerError(request, e)
  }
}
