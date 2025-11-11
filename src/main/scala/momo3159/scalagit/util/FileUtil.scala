package momo3159.scalagit.util

import java.nio.file.{Files, Path, Paths}
import scala.util.Try

object FileUtil {
  def getPath(path: String): Either[Throwable, Path] = {
    Try(Paths.get(path)).toEither
  }

  def readAllBytes(path: Path): Either[Throwable, Seq[Byte]] = {
    Try(Files.readAllBytes(path).toSeq).toEither
  }
}