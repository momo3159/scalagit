package momo3159.scalagit.command

import momo3159.scalagit.domain.CommitObject

object Command {
  def log(root: CommitObject, gitObjectPath: String): Unit =
    root.history(gitObjectPath) match {
      case Right(v) =>
        v.foreach(commitInfo => println(s"""
                                           |${commitInfo.hash}: ${commitInfo.committer}
                                           |parents: ${commitInfo.parents.mkString}
                                           |
                                           |${commitInfo.message}
                                           |""".stripMargin))
      case Left(e) => throw e
    }
}
