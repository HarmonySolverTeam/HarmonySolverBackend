package pl.agh.harmonytools.rest.api

import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import play.api.mvc.Results
import play.api.test.Helpers.contentAsString
import play.api.test.{FakeRequest, Helpers}
import play.shaded.ahc.io.netty.handler.codec.http.HttpMethod


class ParserEndpointTest extends EndpointTest("harmonytools/parser", HttpMethod.POST.name()) {

  private val exerciseJson = Json.parse("""{
    "exercise": "C\n4/4\n(D{})D{}To{position:3/revolution:1/delay:4-3,6-5/extra:7/omit:1/degree:3/system:close/down/isRelatedBackwards}"
  }""".stripMargin)

  private val expectedJsonResponse = Json.parse(
    """{
      |"key":"C",
      |"meter":"4/4",
      |"measures":[
      |   {
      |   "elements":
      |     [
      |       {"functionName":"D","degree":"V","revolution":"1","delays":[],"extra":[],"omit":[],"isDown":false,"system":"undefined","mode":"major","key":"G","isRelatedBackwards":false},
      |       {"functionName":"D","degree":"V","revolution":"1","delays":[],"extra":[],"omit":[],"isDown":false,"system":"undefined","mode":"major","isRelatedBackwards":false},
      |       {"functionName":"T","degree":"III","position":"3","revolution":"3","delays":[{"first":"4","second":"3"},{"first":"6","second":"5"}],"extra":["7"],"omit":["1"],"isDown":true,"system":"close","mode":"minor","isRelatedBackwards":true}
      |     ]
      |    }
      |]
      |}
      |""".stripMargin)

  s"POST $endpoint" should {
    "should be parsed" in {
      val request          = FakeRequest.apply(method, endpoint).withJsonBody(exerciseJson)
      val controller       = new DefaultApiController(Helpers.stubControllerComponents(), new DefaultApiImpl())
      val result           = controller.parseHarmonicsExercise().apply(request)
      val bodyText: String = contentAsString(result)
      Json.parse(bodyText.stripMargin) mustEqual expectedJsonResponse
    }
  }
}
