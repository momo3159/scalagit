package momo3159.scalagit.command

import momo3159.scalagit.domain.{CommitObject, DomainError, ObjectLocation}

object Command {
  def log(root: CommitObject)(implicit gitObjectPath: ObjectLocation): Either[DomainError, Unit] =
    root.history match {
      case Right(v) => Right(v.foreach(commitInfo => println(commitInfo.format)))
      case Left(e)  => Left(e)
    }
}
