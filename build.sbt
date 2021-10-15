import dependencies._

ThisBuild / organization := "com.streese.strulogsca"
ThisBuild / scalaVersion := "2.13.6"
ThisBuild / version      := "0.1.0"

lazy val strulogsca = (project in file("."))
  .aggregate(strulogscaBeautiful, strulogscaUgly)
  .settings(
    name           := "strulogsca",
    publish / skip := true
  )

lazy val strulogscaBeautiful = (project in file("beautiful"))
  .settings(
    name                 := "strulogsca-beautiful",
    libraryDependencies  := logging ++ akka
  )

lazy val strulogscaUgly = (project in file("ugly"))
  .settings(
    name                 := "strulogsca-ugly",
    libraryDependencies  := logging ++ akka
  )

ThisBuild / scapegoatVersion := "1.4.9"
