import sbt._

object Dependencies {
  lazy val parserCombinators = "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.0"
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
  lazy val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.13.4"
}
