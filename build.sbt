name := """ADLServer"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.11"

libraryDependencies += guice

libraryDependencies += javaJdbc

libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.11"

libraryDependencies ++= Seq(
	"org.apache.spark" %% "spark-core" % "2.3.0",
	"org.apache.spark" %% "spark-mllib" % "2.3.0" % "runtime"
	)