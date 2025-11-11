package momo3159.scalagit.domain

case class CommitInfo(
    hash: String,
    size: Int,
    parents: Seq[String],
    author: String,
    committer: String,
    message: String
) {
  def format: String =
    s"""
       |$hash: $committer
       |parents: ${parents.mkString}
       |
       |$message
       |""".stripMargin
}
object CommitInfo {
  class Builder private[CommitInfo] (
      private var hash: Option[String] = None,
      private var size: Option[Int] = None,
      private var parents: Seq[String] = Seq.empty,
      private var author: Option[String] = None,
      private var committer: Option[String] = None,
      private var message: Option[String] = None
  ) {
    def withHash(v: String): Builder = {
      this.hash = Some(v)
      this
    }
    def withSize(v: Int): Builder = {
      this.size = Some(v)
      this
    }
    def withParents(v: Seq[String]): Builder = {
      this.parents = v
      this
    }
    def withAccParents(v: String): Builder = {
      this.parents = this.parents :+ v
      this
    }
    def withAuthor(v: String): Builder = {
      this.author = Some(v)
      this
    }
    def withCommitter(v: String): Builder = {
      this.committer = Some(v)
      this
    }
    def withMessage(v: String): Builder = {
      this.message = Some(v)
      this
    }

    def build(): CommitInfo =
      CommitInfo(
        hash = hash.get,
        size = size.get,
        parents = parents,
        author = author.get,
        committer = committer.get,
        message = message.get
      )
  }

  def builder: Builder = new Builder()
}
