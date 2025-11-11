package momo3159.scalagit

import momo3159.scalagit.command.Command
import momo3159.scalagit.domain.{CommitObject, GitObject}

object Main extends App {
  private val commitHash   = "509e634cfd498ee72f5edbc0158adf1f6ca5dea1"

  GitObject.createFrom(commitHash, ".git/objects") match {
    case Right(v: CommitObject) => Command.log(v, ".git/objects")
    case Right(v) => println(s"invalid object: $v")
    case Left(e) => println(e)
  }
}
