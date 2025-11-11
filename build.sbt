ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.16"
ThisBuild / scalafmtOnCompile := true

lazy val root = (project in file("."))
  .settings(
    name := "scalagit"
  )
