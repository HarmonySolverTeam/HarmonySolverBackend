package pl.agh.harmonytools.rest.api

import play.api.libs.json.Json
import play.api.test.Helpers.contentAsString
import play.api.test.{FakeRequest, Helpers}
import play.shaded.ahc.io.netty.handler.codec.http.HttpMethod

class HarmonicsExerciseEndpointTest extends EndpointTest("harmonytools/harmonics", HttpMethod.POST.name()) {
  private val exerciseJson = Json.parse("""{
    "exercise": {
        "key": "C",
        "meter": [4, 4],
        "measures": [
            {
                "elements": [
                    {
                        "functionName": "D"
                    },
                    {
                        "functionName": "T"
                    }
                ]
            }
        ]
    }
}""".stripMargin)

  private val expectedJsonResponse = Json.parse(
    """{
      |    "exercise": {
      |        "key": "C",
      |        "meter": [4, 4],
      |        "measures": [
      |            {
      |                "elements": [
      |                    {
      |                        "functionName": "D",
      |                        "degree": "V",
      |                        "revolution": "1",
      |                        "delays": [],
      |                        "extra": [],
      |                        "omit": [],
      |                        "isDown": false,
      |                        "system": "undefined",
      |                        "mode": "major",
      |                        "isRelatedBackwards": false
      |                    },
      |                    {
      |                        "functionName": "T",
      |                        "degree": "I",
      |                        "revolution": "1",
      |                        "delays": [],
      |                        "extra": [],
      |                        "omit": [],
      |                        "isDown": false,
      |                        "system": "undefined",
      |                        "mode": "major",
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
      |                "pitch": 71,
      |                "baseNote": 6,
      |                "chordComponent": "3",
      |                "duration": 0
      |            },
      |            "altoNote": {
      |                "pitch": 67,
      |                "baseNote": 4,
      |                "chordComponent": "1",
      |                "duration": 0
      |            },
      |            "tenorNote": {
      |                "pitch": 62,
      |                "baseNote": 1,
      |                "chordComponent": "5",
      |                "duration": 0
      |            },
      |            "bassNote": {
      |                "pitch": 55,
      |                "baseNote": 4,
      |                "chordComponent": "1",
      |                "duration": 0
      |            },
      |            "harmonicFunction": {
      |                "functionName": "D",
      |                "degree": "V",
      |                "revolution": "1",
      |                "delays": [],
      |                "extra": [],
      |                "omit": [],
      |                "isDown": false,
      |                "system": "undefined",
      |                "mode": "major",
      |                "isRelatedBackwards": false
      |            },
      |            "duration": 0.5
      |        },
      |        {
      |            "sopranoNote": {
      |                "pitch": 72,
      |                "baseNote": 0,
      |                "chordComponent": "1",
      |                "duration": 0
      |            },
      |            "altoNote": {
      |                "pitch": 67,
      |                "baseNote": 4,
      |                "chordComponent": "5",
      |                "duration": 0
      |            },
      |            "tenorNote": {
      |                "pitch": 64,
      |                "baseNote": 2,
      |                "chordComponent": "3",
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
      |                "revolution": "1",
      |                "delays": [],
      |                "extra": [],
      |                "omit": [],
      |                "isDown": false,
      |                "system": "undefined",
      |                "mode": "major",
      |                "isRelatedBackwards": false
      |            },
      |            "duration": 0.5
      |        }
      |    ],
      |    "success": true
      |}""".stripMargin)

  s"POST $endpoint" should {
    "should be solved" in {
      val request          = FakeRequest.apply(method, endpoint).withJsonBody(exerciseJson)
      val controller       = new DefaultApiController(Helpers.stubControllerComponents(), new DefaultApiImpl())
      val result           = controller.solveHarmonicFunctionsExercise().apply(request)
      val bodyText: String = contentAsString(result)
      Json.parse(bodyText.stripMargin) mustEqual expectedJsonResponse
    }
  }
}
