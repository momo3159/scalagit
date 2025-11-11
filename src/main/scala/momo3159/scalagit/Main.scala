package momo3159.scalagit

import momo3159.scalagit.command.Command
import momo3159.scalagit.domain.{CommitObject, GitObject, ObjectLocation}
import momo3159.scalagit.logger.{ERROR, Logger}

object Main extends App {
  private val commitHash                          = "f179dfac9dc4ee6eeaf7ab8e5e503c56e433a27f"
  private implicit val objectsLoc: ObjectLocation = ObjectLocation(".git/objects")

  GitObject.createFrom(commitHash) match {
    case Right(v: CommitObject) =>
      Command.log(v).swap.foreach(e => Logger(ERROR, s"createForm in Main: Right(commitObject)", Some(e)).log())
    case Right(v) => Logger(ERROR, s"createForm in Main: Right($v)", None).log()
    case Left(e)  => Logger(ERROR, "crateForm in Main: Left", Some(e)).log()
  }
}
