package pl.agh.harmonytools.rest.api

import play.api.libs.json.Json
import play.api.test.{FakeRequest, Helpers}
import play.api.test.Helpers.contentAsString
import play.shaded.ahc.io.netty.handler.codec.http.HttpMethod

class BassExerciseEndpointTest extends EndpointTest("harmonytools/bass", HttpMethod.POST.name()) {
  private val exerciseJson = Json.parse("""{
    "exercise": {
        "key": "c",
        "meter": [4, 4],
        "elements": [
            {
                "bassNote": {
                    "pitch": 60,
                    "baseNote": 0,
                    "duration": 1.0
                }
            }
        ]
    }
}""".stripMargin)

  private val expectedJsonResponse = Json.parse("""{
      |    "exercise": {
      |        "key": "c",
      |        "meter": [4, 4],
      |        "measures": [
      |            {
      |                "elements": [
      |                    {
      |                        "functionName": "T",
      |                        "degree": "I",
      |                        "inversion": "1",
      |                        "delays": [],
      |                        "extra": [],
      |                        "omit": [],
      |                        "isDown": false,
      |                        "system": "undefined",
      |                        "mode": "minor",
      |                        "isRelatedBackwards": false
      |                    }
      |                ]
      |            }
      |        ]
      |    },
      |    "rating": 0,
      |    "chords": [
      |        {
      |            "sopranoNote": {
      |                "pitch": 75,
      |                "baseNote": 2,
      |                "chordComponent": "3>",
      |                "duration": 0
      |            },
      |            "altoNote": {
      |                "pitch": 67,
      |                "baseNote": 4,
      |                "chordComponent": "5",
      |                "duration": 0
      |            },
      |            "tenorNote": {
      |                "pitch": 67,
      |                "baseNote": 4,
      |                "chordComponent": "5",
      |                "duration": 0
      |            },
      |            "bassNote": {
      |                "pitch": 60,
      |                "baseNote": 0,
      |                "chordComponent": "1",
      |                "duration": 0
      |            },
      |            "harmonicFunction": {
      |                "functionName": "T",
      |                "degree": "I",
      |                "inversion": "1",
      |                "delays": [],
      |                "extra": [],
      |                "omit": [],
      |                "isDown": false,
      |                "system": "undefined",
      |                "mode": "minor",
      |                "isRelatedBackwards": false
      |            },
      |            "duration": 1.0
      |        }
      |    ],
      |    "success": true
      |}
      |""".stripMargin)

  s"POST $endpoint" should {
    "should be solved" in {
      val request          = FakeRequest.apply(method, endpoint).withJsonBody(exerciseJson)
      val controller       = new DefaultApiController(Helpers.stubControllerComponents(), new DefaultApiImpl())
      val result           = controller.solveBassExercise().apply(request)
      val bodyText: String = contentAsString(result)
      Json.parse(bodyText.stripMargin) mustEqual expectedJsonResponse
    }
  }
}
