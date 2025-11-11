package momo3159.scalagit.domain

import momo3159.scalagit.util.{FileUtil, ZlibUtil}

import scala.util.{Try, Success, Failure}

sealed trait GitObject {
  val hash: String
  val size: Int
  val data: String
}
object GitObject {
  private def readObjectHeader(str: String): Either[Throwable, (GitObjectType, Int)] = {
    str.split("\u0000").toSeq match {
      case Seq(header, _) =>
        header.split(" ").toSeq match {
          case Seq(kindStr, sizeStr) =>
            (GitObjectType.fromString(kindStr), Try(sizeStr.toInt)) match {
              case (Success(kind), Success(size)) => Right((kind, size))
              case (Success(_), Failure(e))       => Left(e)
              case (Failure(e), Success(_))       => Left(e)
              case (Failure(e1), Failure(_))      => Left(e1)
            }
          case _ => Left(new RuntimeException("invalid object."))
        }
      case _ => Left(new RuntimeException("invalid object."))
    }
  }

  private def readObjectBody(str: String): Either[Throwable, String] = {
    str.split("\u0000").toSeq match {
      case Seq(_, body) => Right(body)
      case _            => Left(new RuntimeException("invalid object."))
    }
  }

  def createFrom(hash: String, gitObjectPath: String): Either[Throwable, GitObject] = {
    for {
      path        <- FileUtil.getPath(s"$gitObjectPath/${hash.substring(0, 2)}/${hash.substring(2)}")
      bytes       <- FileUtil.readAllBytes(path)
      deflated    <- ZlibUtil.inflate(bytes)
      deflatedStr <- Try(new String(deflated.toArray)).toEither
      header      <- readObjectHeader(deflatedStr)
      body        <- readObjectBody(deflatedStr)
      gitObject <- header match {
        case (Commit, fileSize) => Right(CommitObject(hash, fileSize, body))
        case (Tree, fileSize) => Right(TreeObject(hash, fileSize, body))
        case (Blob, fileSize) => Right(BlobObject(hash, fileSize, body))
        case (Tag, fileSize)  => Right(TagObject(hash, fileSize, body))
        case _                => Left(new RuntimeException(s"invalid object: "))
      }
    } yield gitObject
  }
}

case class CommitObject(hash: String, size: Int, data: String) extends GitObject {
  def toCommitInfo: CommitInfo = {
    var builder = data
      .split("\n")
      .foldLeft(CommitInfo.builder.withHash(hash).withSize(size)) { (builder, line) =>
        line.split(" ", 2) match {
          case Array(key, value) if key == "parent"    => builder.withAccParents(value)
          case Array(key, value) if key == "author"    => builder.withAuthor(value)
          case Array(key, value) if key == "committer" => builder.withCommitter(value)
          case _                                       => builder
        }
      }

    builder = data.split("\n\n") match {
      case Array(_, message) => builder.withMessage(message)
      case _                 => builder
    }
    builder.build()
  }

  def history(gitObjectPath: String): Either[Throwable, Seq[CommitInfo]] = {
    def loop(hashes: Seq[String], visited: Set[String]): Either[Throwable, Seq[CommitInfo]] =
      hashes match {
        case Nil                                    => Right(Seq.empty)
        case hash :: rest if visited.contains(hash) => loop(rest, visited)
        case hash :: rest =>
          for {
            obj <- GitObject.createFrom(hash, gitObjectPath)
            commitInfo <- obj match {
              case v: CommitObject => Right(v.toCommitInfo)
              case v               => Left(new RuntimeException(""))
            }
            restHistory <- loop(rest ++ commitInfo.parents, visited + hash)
          } yield commitInfo +: restHistory
      }

    loop(Seq(hash), Set.empty)
  }
}
case class TreeObject(hash: String, size: Int, data: String) extends GitObject
case class BlobObject(hash: String, size: Int, data: String) extends GitObject
case class TagObject(hash: String, size: Int, data: String)  extends GitObject