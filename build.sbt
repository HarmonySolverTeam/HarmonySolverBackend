// Project setup
val scalaV   = "2.13.4"
val projectV = "1.0"

lazy val settings = Seq(
  version := projectV,
  scalaVersion := scalaV
)

lazy val testDependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.0.8" % Test
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
    solver
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
    harmonics_solver
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
