package momo3159.scalagit.util

import momo3159.scalagit.domain.{DomainError, SystemError}

import java.nio.file.{Files, Path, Paths}
import scala.util.Try

object FileUtil {
  def getPath(path: String): Either[DomainError, Path] =
    Try(Paths.get(path)).fold(
      e => Left(SystemError("getPath in FileUtil", e)),
      v => Right(v)
    )

  def readAllBytes(path: Path): Either[DomainError, Seq[Byte]] = {
    Try(Files.readAllBytes(path).toSeq).fold(
      e => Left(SystemError("readAllBytes in FileUtil", e)),
      v => Right(v)
    )
  }
}
