package momo3159.scalagit.domain
import scala.util.Try

sealed abstract class DomainError(val reason: String, val cause: Throwable) extends RuntimeException(reason, cause)
case class InvalidValueError(override val reason: String, override val cause: Throwable)
    extends DomainError(s"InvalidValueError: $reason", cause)
case class SystemError(override val reason: String, override val cause: Throwable) extends DomainError(s"SystemError: $reason", cause)

object Converter {
  implicit class RichTry[A](v: Try[A]) {
    def toEitherSystemError(reason: String): Either[DomainError, A] =
      v.fold(
        e => Left(SystemError(reason, e)),
        a => Right(a)
      )
  }
}
