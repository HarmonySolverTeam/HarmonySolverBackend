package pl.agh.harmonytools.rest.api

import play.api.libs.json.Json
import play.api.test.{FakeRequest, Helpers}
import play.api.test.Helpers.contentAsString
import play.shaded.ahc.io.netty.handler.codec.http.HttpMethod

class ValidatorEndpointTest extends EndpointTest("harmonytools/validator", HttpMethod.POST.name()) {
  private val exerciseJson = Json.parse("""[
        {
            "sopranoNote": {
                "pitch": 72,
                "baseNote": "C",
                "chordComponent": "1",
                "duration": 0
            },
            "altoNote": {
                "pitch": 67,
                "baseNote": "G",
                "chordComponent": "5",
                "duration": 0
            },
            "tenorNote": {
                "pitch": 64,
                "baseNote": "E",
                "chordComponent": "3",
                "duration": 0
            },
            "bassNote": {
                "pitch": 48,
                "baseNote": "C",
                "chordComponent": "1",
                "duration": 0
            },
            "harmonicFunction": {
                "functionName": "T"
            },
            "duration": 0
        },
        {
            "sopranoNote": {
                "pitch": 71,
                "baseNote": "B",
                "chordComponent": "3",
                "duration": 0
            },
            "altoNote": {
                "pitch": 67,
                "baseNote": "G",
                "chordComponent": "1",
                "duration": 0
            },
            "tenorNote": {
                "pitch": 62,
                "baseNote": "D",
                "chordComponent": "5",
                "duration": 0
            },
            "bassNote": {
                "pitch": 43,
                "baseNote": "G",
                "chordComponent": "1",
                "duration": 0
            },
            "harmonicFunction": {
                "functionName": "D"
            },
            "duration": 0
        },
        {
            "sopranoNote": {
                "pitch": 72,
                "baseNote": "C",
                "chordComponent": "1",
                "duration": 0
            },
            "altoNote": {
                "pitch": 67,
                "baseNote": "G",
                "chordComponent": "5",
                "duration": 0
            },
            "tenorNote": {
                "pitch": 64,
                "baseNote": "E",
                "chordComponent": "3",
                "duration": 0
            },
            "bassNote": {
                "pitch": 48,
                "baseNote": "C",
                "chordComponent": "1",
                "duration": 0
            },
            "harmonicFunction": {
                "functionName": "T"
            },
            "duration": 0
         }
        ]""".stripMargin)

  s"POST $endpoint" should {
    "should be validated correctly" in {
      val request          = FakeRequest.apply(method, endpoint).withJsonBody(exerciseJson)
      val controller       = new DefaultApiController(Helpers.stubControllerComponents(), new DefaultApiImpl())
      val result           = controller.validateSolvedExercise().apply(request)
      val bodyText: String = contentAsString(result)
      bodyText.slice(1, 8) mustEqual "Correct"
    }
  }
}
