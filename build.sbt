name := """ADLServer"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.11"

//conflictManager := ConflictManager.strict

libraryDependencies += guice

libraryDependencies += javaJdbc

libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.11"

//libraryDependencies += "org.webjars.npm" % "jszip" % "3.1.5"

libraryDependencies ++= Seq(
	"org.apache.spark" %% "spark-core" % "2.3.0",
	"org.apache.spark" %% "spark-mllib" % "2.3.0"
	)
	
dependencyOverrides ++= Seq(
	"com.fasterxml.jackson.core" % "jackson-core" % "2.8.11",
	"com.fasterxml.jackson.core" % "jackson-databind" % "2.8.11",
	"com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.8.11"
	)
	
dependencyOverrides += "com.google.guava" % "guava" % "15.0"