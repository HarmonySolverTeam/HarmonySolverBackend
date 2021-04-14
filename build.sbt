// Project setup
val scalaV   = "2.12.6"
val projectV = "1.0"

externalResolvers += Resolver.sonatypeRepo("snapshots")

lazy val settings = Seq(
  version := projectV,
  scalaVersion := scalaV
)

lazy val testDependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.0.8" % Test
)

lazy val playDependencies = Seq(
  guice,
  ws,
  "org.webjars" % "swagger-ui" % "3.1.5",
  "javax.annotation" % "javax.annotation-api" % "1.3.2" % "compile",
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test
)

/** projects */
lazy val rootProject = project
  .in(file("."))
  .settings(
    name := "HarmonySolverBackend",
    version := projectV
  )
  .aggregate(
    model,
    harmonics_parser,
    algorithms,
    bass_translator,
    solver,
    rest
  )

lazy val rest = project
  .in(file("rest"))
  .enablePlugins(OpenApiGeneratorPlugin)
  .enablePlugins(PlayScala)
  .settings(
    Compile / unmanagedSourceDirectories += baseDirectory.value / "src/main/scala",
    openApiGeneratorName := "scala-play-server",
    openApiInputSpec := file("rest/src/main/resources/harmonySolverApi.yaml").getPath,
    openApiConfigFile := file("rest/src/main/resources/config.yaml").getPath,
    openApiOutputDir := file("rest").getPath,
    openApiValidateSpec := SettingDisabled,
    openApiGenerateModelTests := SettingEnabled,
    name := "rest",
    settings,
    libraryDependencies ++= testDependencies,
    libraryDependencies ++= playDependencies
  )
  .dependsOn(
    harmonics_parser,
    bass_solver,
    soprano_solver,
    validator
  )

lazy val model = project
  .settings(
    name := "model",
    settings,
    libraryDependencies ++= testDependencies
  )
  .dependsOn(
    algorithms
  )

lazy val harmonics_parser = project
  .settings(
    name := "harmonics_parser",
    settings,
    libraryDependencies ++= testDependencies ++ Seq("org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2")
  )
  .dependsOn(model)

lazy val algorithms = project
  .settings(
    name := "algorithms",
    settings,
    libraryDependencies ++= testDependencies
  )

lazy val bass_translator = project
  .settings(
    name := "bass_translator",
    settings,
    libraryDependencies ++= testDependencies
  )
  .dependsOn(model, harmonics_parser)

lazy val solver = project
  .settings(
    name := "solver",
    settings,
    libraryDependencies ++= testDependencies
  )
  .aggregate(
    harmonics_solver,
    bass_solver,
    soprano_solver
  )
  .dependsOn(
    model
  )

lazy val harmonics_solver = project
  .in(file("solver/harmonics_solver"))
  .settings(
    name := "harmonics_solver",
    settings,
    libraryDependencies ++= testDependencies
  )
  .dependsOn(
    harmonics_parser,
    algorithms
  )

lazy val bass_solver = project
  .in(file("solver/bass_solver"))
  .settings(
    name := "bass_solver",
    settings,
    libraryDependencies ++= testDependencies ++ Seq("io.spray" %% "spray-json" % "1.3.6")
  )
  .dependsOn(
    harmonics_solver,
    bass_translator
  )

lazy val soprano_solver = project
  .in(file("solver/soprano_solver"))
  .settings(
    name := "soprano_solver",
    settings,
    libraryDependencies ++= testDependencies
  )
  .dependsOn(
    harmonics_solver
  )

lazy val validator = project
  .settings(
    name := "validator",
    settings,
    libraryDependencies ++= testDependencies
  )
  .dependsOn(
    harmonics_solver
  )