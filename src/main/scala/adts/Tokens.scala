package adts

sealed trait Tokens
case object LeftBrace   extends Tokens
case object RightBrace  extends Tokens
case object LeftSquare  extends Tokens
case object RightSquare extends Tokens
case object Colon       extends Tokens
case object Comma       extends Tokens
case object SemiColon   extends Tokens
