val scala3Version = "3.8.3"

lazy val root = project
  .in(file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "brkga",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "org.scalameta" %%% "munit" % "1.0.0" % Test,
      "org.scala-js" %%% "scalajs-dom" % "2.8.1")
  )


// This is an application with a main method
scalaJSUseMainModuleInitializer := true

// Single Threaded Tests
Test / parallelExecution := false