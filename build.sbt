
name := "scalaspl"

organization := "com.github.workingDog"

version := (version in ThisBuild).value

scalaVersion := "2.12.8"

libraryDependencies += "com.typesafe" % "config" % "1.3.4"

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-Xlint")

test in assembly := {}

assemblyMergeStrategy in assembly := {
  case PathList(xs@_*) if xs.last.toLowerCase endsWith ".dsa" => MergeStrategy.discard
  case PathList(xs@_*) if xs.last.toLowerCase endsWith ".sf" => MergeStrategy.discard
  case PathList(xs@_*) if xs.last.toLowerCase endsWith ".des" => MergeStrategy.discard
  case PathList(xs@_*) if xs.last endsWith "LICENSES.txt" => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

homepage := Some(url("https://github.com/workingDog/scalaPSL"))

licenses := Seq("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

assemblyJarName in assembly := "scalapsl-" + version.value + ".jar"

