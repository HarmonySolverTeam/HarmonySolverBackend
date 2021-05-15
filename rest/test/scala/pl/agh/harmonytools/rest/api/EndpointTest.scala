package pl.agh.harmonytools.rest.api

import akka.stream.testkit.NoMaterializer
import akka.util.Timeout
import org.scalatestplus.play.PlaySpec
import play.api.mvc.Results

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration

abstract class EndpointTest(protected val endpoint: String, protected val method: String)
  extends PlaySpec
  with Results {
  protected implicit val timeout: Timeout         = Timeout(FiniteDuration(10, TimeUnit.SECONDS))
  protected implicit val mat: NoMaterializer.type = NoMaterializer
}
