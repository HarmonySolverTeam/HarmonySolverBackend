package pl.agh.harmonytools.rest.api

import play.api.libs.json.Json
import play.api.test.Helpers.contentAsString
import play.api.test.{FakeRequest, Helpers}
import play.shaded.ahc.io.netty.handler.codec.http.HttpMethod

class SopranoExerciseEndpointTest extends EndpointTest("harmonytools/soprano", HttpMethod.POST.name()) {
  private val exerciseJson = Json.parse("""{
    "exercise": {
        "key": "C",
        "meter": [4, 4],
        "measures": [
            {
                "notes": [
                    {
                        "pitch": 72,
                        "baseNote": 0,
                        "duration": 0.25
                    },
                    {
                    "pitch": 71,
                    "baseNote": 6,
                    "duration": 0.25
                    },
                    {
                        "pitch": 72,
                        "baseNote": 0,
                        "duration": 0.5
                    }
                ]
            }
        ],
        "possibleFunctionsList": [
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
      |  "exercise": {
      |    "key": "C",
      |    "meter": [
      |      4,
      |      4
      |    ],
      |    "measures": [
      |      {
      |        "notes": [
      |          {
      |            "pitch": 72,
      |            "baseNote": 0,
      |            "duration": 0.25
      |          },
      |          {
      |            "pitch": 71,
      |            "baseNote": 6,
      |            "duration": 0.25
      |          },
      |          {
      |            "pitch": 72,
      |            "baseNote": 0,
      |            "duration": 0.5
      |          }
      |        ]
      |      }
      |    ],
      |    "possibleFunctionsList": [
      |      {
      |        "functionName": "T",
      |        "degree": "I",
      |        "inversion": "1",
      |        "delays": [],
      |        "extra": [],
      |        "omit": [],
      |        "isDown": false,
      |        "system": "undefined",
      |        "mode": "major",
      |        "isRelatedBackwards": false
      |      },
      |      {
      |        "functionName": "D",
      |        "degree": "V",
      |        "inversion": "1",
      |        "delays": [],
      |        "extra": [],
      |        "omit": [],
      |        "isDown": false,
      |        "system": "undefined",
      |        "mode": "major",
      |        "isRelatedBackwards": false
      |      }
      |    ],
      |    "evaluateWithProlog": false
      |  },
      |  "rating": 91,
      |  "chords": [
      |    {
      |      "sopranoNote": {
      |        "pitch": 72,
      |        "baseNote": 0,
      |        "chordComponent": "1",
      |        "duration": 0
      |      },
      |      "altoNote": {
      |        "pitch": 67,
      |        "baseNote": 4,
      |        "chordComponent": "5",
      |        "duration": 0
      |      },
      |      "tenorNote": {
      |        "pitch": 64,
      |        "baseNote": 2,
      |        "chordComponent": "3",
      |        "duration": 0
      |      },
      |      "bassNote": {
      |        "pitch": 48,
      |        "baseNote": 0,
      |        "chordComponent": "1",
      |        "duration": 0
      |      },
      |      "harmonicFunction": {
      |        "functionName": "T",
      |        "degree": "I",
      |        "position": "1",
      |        "inversion": "1",
      |        "delays": [],
      |        "extra": [],
      |        "omit": [],
      |        "isDown": false,
      |        "system": "undefined",
      |        "mode": "major",
      |        "isRelatedBackwards": false
      |      },
      |      "duration": 0.25
      |    },
      |    {
      |      "sopranoNote": {
      |        "pitch": 71,
      |        "baseNote": 6,
      |        "chordComponent": "3",
      |        "duration": 0
      |      },
      |      "altoNote": {
      |        "pitch": 67,
      |        "baseNote": 4,
      |        "chordComponent": "1",
      |        "duration": 0
      |      },
      |      "tenorNote": {
      |        "pitch": 62,
      |        "baseNote": 1,
      |        "chordComponent": "5",
      |        "duration": 0
      |      },
      |      "bassNote": {
      |        "pitch": 43,
      |        "baseNote": 4,
      |        "chordComponent": "1",
      |        "duration": 0
      |      },
      |      "harmonicFunction": {
      |        "functionName": "D",
      |        "degree": "V",
      |        "position": "3",
      |        "inversion": "1",
      |        "delays": [],
      |        "extra": [],
      |        "omit": [],
      |        "isDown": false,
      |        "system": "undefined",
      |        "mode": "major",
      |        "isRelatedBackwards": false
      |      },
      |      "duration": 0.25
      |    },
      |    {
      |      "sopranoNote": {
      |        "pitch": 72,
      |        "baseNote": 0,
      |        "chordComponent": "1",
      |        "duration": 0
      |      },
      |      "altoNote": {
      |        "pitch": 67,
      |        "baseNote": 4,
      |        "chordComponent": "5",
      |        "duration": 0
      |      },
      |      "tenorNote": {
      |        "pitch": 64,
      |        "baseNote": 2,
      |        "chordComponent": "3",
      |        "duration": 0
      |      },
      |      "bassNote": {
      |        "pitch": 48,
      |        "baseNote": 0,
      |        "chordComponent": "1",
      |        "duration": 0
      |      },
      |      "harmonicFunction": {
      |        "functionName": "T",
      |        "degree": "I",
      |        "position": "1",
      |        "inversion": "1",
      |        "delays": [],
      |        "extra": [],
      |        "omit": [],
      |        "isDown": false,
      |        "system": "undefined",
      |        "mode": "major",
      |        "isRelatedBackwards": false
      |      },
      |      "duration": 0.5
      |    }
      |  ],
      |  "success": true
      |}""".stripMargin
  )

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
