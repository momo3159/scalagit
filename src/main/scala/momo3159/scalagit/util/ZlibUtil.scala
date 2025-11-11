package momo3159.scalagit.util

import java.io.ByteArrayOutputStream
import java.util.zip.InflaterOutputStream
import scala.util.Using

object ZlibUtil {
  def inflate(compressed: Seq[Byte]): Either[Throwable, Seq[Byte]] = {
    val bo = new ByteArrayOutputStream()

    Using(new InflaterOutputStream(bo)) { ios =>
      ios.write(compressed.toArray)
      bo.toByteArray.toSeq
    }.toEither
  }
}
