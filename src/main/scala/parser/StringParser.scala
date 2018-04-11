package parser

import adts._

import scala.util.parsing.combinator.RegexParsers

trait StringParser extends RegexParsers {
  val leftBrace: Parser[Tokens]   = "{" ^^^ LeftBrace
  val rightBrace: Parser[Tokens]  = "}" ^^^ RightBrace
  val leftSquare: Parser[Tokens]  = "[" ^^^ LeftSquare
  val rightSquare: Parser[Tokens] = "]" ^^^ RightSquare
  val colon: Parser[Tokens]       = ":" ^^^ Colon
  val comma: Parser[Tokens]       = "," ^^^ Comma
  val semiColon: Parser[Tokens]   = ";" ^^^ SemiColon

  val boolean: Parser[Boolean]   = "true|false".r ^^ { _.toBoolean }
  val numberInt: Parser[Int]     = "-?\\d+".r ^^ { _.toInt }
  val numberFloat: Parser[Float] = "-?\\d+\\.\\d+(E-?\\d+)?".r ^^ { _.toFloat }

  val singleQuotedString: Parser[String] = "'.*?'".r ^^ {
    _.replaceAll("'", "")
  }

  val doubleQuotedString: Parser[String] = """".*?"""".r ^^ {
    _.replaceAll(""""""", "")
  }

  val enclosedString: Parser[String] = singleQuotedString | doubleQuotedString
}
