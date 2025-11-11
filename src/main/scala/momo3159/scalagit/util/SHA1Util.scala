package momo3159.scalagit.util
import java.security.MessageDigest

object SHA1Util {
  def get(sb: Seq[Byte]) = {
    MessageDigest.getInstance("SHA1").digest(sb.toArray).map(_ & 0xff).map(_.toHexString).mkString
  }
}