package momo3159.scalagit.logger
import momo3159.scalagit.domain.DomainError

sealed abstract class Level(val value: String)
case object ERROR extends Level("ERROR")
case object WARN  extends Level("WARN")
case object INFO  extends Level("INFO")

case class Logger(level: Level, message: String, error: Option[DomainError]) {
  def log() =
    println(s"""
        |{
        | "level": "${level.value}",
        | "message": "$message",
        | "error": "$error",
        | ${error.map(e => s"\"trace\": ${e.getStackTrace.mkString("[", ", ", "]")}").getOrElse("\"trace\":\"\"")}
        |}
        |""".stripMargin)

}
