
name := "scalaspl"

organization := "com.github.workingDog"

version := (version in ThisBuild).value

scalaVersion := "2.11.8"

crossScalaVersions := Seq("2.11.8")

libraryDependencies ++= Seq("com.typesafe" % "config" % "1.3.0")

homepage := Some(url("https://github.com/workingDog/scalaPSL"))

licenses := Seq("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

scalacOptions ++= Seq(
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-feature", // Emit warning and location for usages of features that should be imported explicitly.
  "-unchecked", // Enable additional warnings where generated code depends on assumptions.
  "-Xlint" // Enable recommended additional warnings.
)
