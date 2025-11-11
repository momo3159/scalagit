package momo3159.scalagit.domain

import scala.util.{Failure, Success, Try}

sealed trait GitObjectType
case object Commit extends GitObjectType
case object Tree   extends GitObjectType
case object Blob   extends GitObjectType
case object Tag    extends GitObjectType

object GitObjectType {
  def fromString(typeStr: String): Try[GitObjectType] = {
    typeStr match {
      case "commit" => Success(Commit)
      case "tree"   => Success(Tree)
      case "blob"   => Success(Blob)
      case "tag"    => Success(Tag)
      case _        => Failure(new IllegalArgumentException(s"object type should be commit or tree or blob or tag, but arg is $typeStr"))
    }
  }
}
