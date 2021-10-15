import sbt._
import Keys._

object dependencies {

  private object versions {
    val akka           = "2.6.16"
    val jackson        = "2.13.0"
    val logback        = "1.2.6"
    val logbackContrib = "0.1.5"
    val logstash       = "6.6"
    val scalaLogging   = "3.9.4"
  }

  private val jackson        = "com.fasterxml.jackson.core"  % "jackson-databind"         % versions.jackson
  private val logbackClassic = "ch.qos.logback"              % "logback-classic"          % versions.logback
  private val logbackJackson = "ch.qos.logback.contrib"      % "logback-jackson"          % versions.logbackContrib
  private val logbackJson    = "ch.qos.logback.contrib"      % "logback-json-classic"     % versions.logbackContrib
  private val logstash       = "net.logstash.logback"        % "logstash-logback-encoder" % versions.logstash
  private val scalaLogging   = "com.typesafe.scala-logging" %% "scala-logging"            % versions.scalaLogging

  val logging: Seq[ModuleID] = Seq(jackson, logbackClassic, logbackJackson, logbackJson, logstash, scalaLogging)

  private val akkaActor = "com.typesafe.akka" %% "akka-actor-typed" % versions.akka

  val akka: Seq[ModuleID] = Seq(akkaActor)

}
