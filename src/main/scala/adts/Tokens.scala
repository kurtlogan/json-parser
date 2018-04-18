package adts

sealed trait Tokens
final case object LeftBrace   extends Tokens
final case object RightBrace  extends Tokens
final case object LeftSquare  extends Tokens
final case object RightSquare extends Tokens
final case object Colon       extends Tokens
final case object Comma       extends Tokens
final case object SemiColon   extends Tokens
final case object Null        extends Tokens
