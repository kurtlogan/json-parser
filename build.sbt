import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.4",
      version      := "0.1.0-SNAPSHOT",
      scapegoatVersion := "1.3.4",
      scapegoatRunAlways := true,
      scalafmtOnCompile := true
    )),
    name := "json-parser",
    libraryDependencies += parserCombinators,
    libraryDependencies += scalaTest % Test,
    libraryDependencies += scalaCheck % Test
  )
