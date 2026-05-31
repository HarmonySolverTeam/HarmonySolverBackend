// Project setup
val scalaV   = "2.12.6"
val projectV = "1.0"

externalResolvers += Resolver.sonatypeRepo("snapshots")

lazy val settings = Seq(
  version := projectV,
  scalaVersion := scalaV
)

lazy val testDependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.0.4" % Test
)

lazy val playDependencies = Seq(
  guice,
  ws,
  "org.webjars"             % "swagger-ui"           % "5.32.5",
  "javax.annotation"        % "javax.annotation-api" % "1.3.2" % "compile",
  "org.scalatestplus.play" %% "scalatestplus-play"   % "5.1.0" % Test
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
    algorithms,
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
  .dependsOn(walking_bass_solver)

lazy val model = project
  .settings(
    name := "model",
    settings,
    libraryDependencies ++= testDependencies
  )
  .dependsOn(
    algorithms
  )

lazy val algorithms = project
  .settings(
    name := "algorithms",
    settings,
    libraryDependencies ++= testDependencies
  )

lazy val solver = project
  .settings(
    name := "solver",
    settings,
    libraryDependencies ++= testDependencies
  )
  .aggregate(walking_bass_solver)
  .dependsOn(model)

lazy val walking_bass_solver = project
  .in(file("solver/walking_bass_solver"))
  .settings(
    name := "walking_bass_solver",
    settings,
    libraryDependencies ++= testDependencies
  )
  .dependsOn(
    algorithms,
    model
  )
