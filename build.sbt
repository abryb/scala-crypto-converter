name := """scala-crypto-converter"""
organization := "com.github.abryb"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.6"

routesImport += "binders.Binders._"

libraryDependencies += guice
libraryDependencies += ws
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "org.scalatestplus" %% "mockito-3-4" % "3.2.9.0" % "test"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.github.abryb.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.github.abryb.binders._"
