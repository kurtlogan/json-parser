package parser

import adts._

import scala.util.parsing.combinator.RegexParsers

trait StringParser extends RegexParsers {
  val leftBrace: Parser[Tokens] = "\\{".r ^^ { _ ⇒
    LeftBrace
  }

  val rightBrace: Parser[Tokens] = "\\}".r ^^ { _ ⇒
    LeftBrace
  }

  val leftSquare: Parser[Tokens] = "\\[".r ^^ { _ ⇒
    LeftSquare
  }

  val rightSquare: Parser[Tokens] = "\\]".r ^^ { _ ⇒
    RightSquare
  }

  val colon: Parser[Tokens] = ":".r ^^ { _ ⇒
    Colon
  }

  val comma: Parser[Tokens] = ",".r ^^ { _ ⇒
    Comma
  }

  val semiColon: Parser[Tokens] = ";".r ^^ { _ ⇒
    SemiColon
  }

  val singleQuotedString: Parser[String] = "'.*?'".r ^^ {
    _.replaceAll("'", "")
  }

  val doubleQuotedString: Parser[String] = """".*?"""".r ^^ {
    _.replaceAll(""""""", "")
  }

  val boolean: Parser[Boolean] = "true|false".r ^^ { _.toBoolean }

  val numberInt: Parser[Int] = "-?\\d+".r ^^ { _.toInt }

  val numberFloat: Parser[Float] = "-?\\d+\\.\\d+(E-?\\d+)?".r ^^ { _.toFloat }

  val enclosedString: Parser[String] = singleQuotedString | doubleQuotedString
}
