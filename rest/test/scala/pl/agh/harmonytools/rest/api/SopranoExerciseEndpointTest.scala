package pl.agh.harmonytools.rest.api

import play.api.libs.json.Json
import play.api.test.{FakeRequest, Helpers}
import play.api.test.Helpers.contentAsString
import play.shaded.ahc.io.netty.handler.codec.http.HttpMethod

class SopranoExerciseEndpointTest extends EndpointTest("harmonytools/soprano", HttpMethod.POST.name()) {
  private val exerciseJson = Json.parse("""{
    "exercise": {
        "key": "C",
        "meter": "4/4",
        "measures": [
            {
                "elements": [
                    {
                        "pitch": 72,
                        "baseNote": "C",
                        "duration": 0.5
                    },
                    {
                    "pitch": 71,
                    "baseNote": "B",
                    "duration": 0.5
                    },
                    {
                        "pitch": 72,
                        "baseNote": "C",
                        "duration": 0.5
                    }
                ]
            }
        ],
        "harmonicFunctions": [
            {
                "functionName": "T"
            },
            {
                "functionName": "D"
            }
        ]

    },
    "punishmentRatios": {
        "hiddenOctaves": 0.5
    }
}""".stripMargin)

  private val expectedJsonResponse = Json.parse(
    """{
      |    "exercise": {
      |        "key": "C",
      |        "meter": "4/4",
      |        "measures": [
      |            {
      |                "elements": [
      |                    {
      |                        "pitch": 72,
      |                        "baseNote": "C",
      |                        "duration": 0.5
      |                    },
      |                    {
      |                        "pitch": 71,
      |                        "baseNote": "B",
      |                        "duration": 0.5
      |                    },
      |                    {
      |                        "pitch": 72,
      |                        "baseNote": "C",
      |                        "duration": 0.5
      |                    }
      |                ]
      |            }
      |        ],
      |        "harmonicFunctions": [
      |            {
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
      |            {
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
      |            }
      |        ]
      |    },
      |    "rating": 102,
      |    "chords": [
      |        {
      |            "sopranoNote": {
      |                "pitch": 72,
      |                "baseNote": "C",
      |                "chordComponent": "1",
      |                "duration": 0
      |            },
      |            "altoNote": {
      |                "pitch": 67,
      |                "baseNote": "G",
      |                "chordComponent": "5",
      |                "duration": 0
      |            },
      |            "tenorNote": {
      |                "pitch": 64,
      |                "baseNote": "E",
      |                "chordComponent": "3",
      |                "duration": 0
      |            },
      |            "bassNote": {
      |                "pitch": 48,
      |                "baseNote": "C",
      |                "chordComponent": "1",
      |                "duration": 0
      |            },
      |            "harmonicFunction": {
      |                "functionName": "T",
      |                "degree": "I",
      |                "position": "1",
      |                "revolution": "1",
      |                "delays": [],
      |                "extra": [],
      |                "omit": [],
      |                "isDown": false,
      |                "system": "undefined",
      |                "mode": "major",
      |                "isRelatedBackwards": false
      |            }
      |        },
      |        {
      |            "sopranoNote": {
      |                "pitch": 71,
      |                "baseNote": "B",
      |                "chordComponent": "3",
      |                "duration": 0
      |            },
      |            "altoNote": {
      |                "pitch": 67,
      |                "baseNote": "G",
      |                "chordComponent": "1",
      |                "duration": 0
      |            },
      |            "tenorNote": {
      |                "pitch": 62,
      |                "baseNote": "D",
      |                "chordComponent": "5",
      |                "duration": 0
      |            },
      |            "bassNote": {
      |                "pitch": 43,
      |                "baseNote": "G",
      |                "chordComponent": "1",
      |                "duration": 0
      |            },
      |            "harmonicFunction": {
      |                "functionName": "D",
      |                "degree": "V",
      |                "position": "3",
      |                "revolution": "1",
      |                "delays": [],
      |                "extra": [],
      |                "omit": [],
      |                "isDown": false,
      |                "system": "undefined",
      |                "mode": "major",
      |                "isRelatedBackwards": false
      |            }
      |        },
      |        {
      |            "sopranoNote": {
      |                "pitch": 72,
      |                "baseNote": "C",
      |                "chordComponent": "1",
      |                "duration": 0
      |            },
      |            "altoNote": {
      |                "pitch": 67,
      |                "baseNote": "G",
      |                "chordComponent": "5",
      |                "duration": 0
      |            },
      |            "tenorNote": {
      |                "pitch": 64,
      |                "baseNote": "E",
      |                "chordComponent": "3",
      |                "duration": 0
      |            },
      |            "bassNote": {
      |                "pitch": 48,
      |                "baseNote": "C",
      |                "chordComponent": "1",
      |                "duration": 0
      |            },
      |            "harmonicFunction": {
      |                "functionName": "T",
      |                "degree": "I",
      |                "position": "1",
      |                "revolution": "1",
      |                "delays": [],
      |                "extra": [],
      |                "omit": [],
      |                "isDown": false,
      |                "system": "undefined",
      |                "mode": "major",
      |                "isRelatedBackwards": false
      |            }
      |        }
      |    ],
      |    "success": true
      |}
      |""".stripMargin)

  s"POST $endpoint" should {
    "should be solved" in {
      val request          = FakeRequest.apply(method, endpoint).withJsonBody(exerciseJson)
      val controller       = new DefaultApiController(Helpers.stubControllerComponents(), new DefaultApiImpl())
      val result           = controller.solveSopranoExercise().apply(request)
      val bodyText: String = contentAsString(result)
      Json.parse(bodyText.stripMargin) mustEqual expectedJsonResponse
    }
  }
}
