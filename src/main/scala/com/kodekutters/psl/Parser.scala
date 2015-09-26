package com.kodekutters.psl

import java.io.InputStream

import scala.io.{Codec, Source}


/**
 * The parser for the Public Suffix List file.
 */
object Parser {

  private var ruleLine = """^(\S+)""".r.pattern
  private var commentLine = """^//.*$""".r.pattern
  private var whiteSpaceLine = """^\\s*$""".r.pattern

  def apply() = new Parser()
}

/**
 * The parser takes the Public Suffix List file as input and returns a list of {@link Rule}.
 */
class Parser {

  import Parser._

  /**
   * Parses all rules from a stream and returns the list of rules
   *
   * @param stream the stream with lines of rules
   * @param charset the character encoding of that stream
   *
   */
  def parse(stream: InputStream, charset: Codec): List[Rule] = {
    val theList = (for (line <- Source.fromInputStream(stream, charset.name).getLines()) yield parseLine(line)).toList
    theList.flatten
  }

  /**
   * Parses a line for a rule and returns a rule or None if no rule was found
   *
   * @param line the line with one rule
   * @return the parsed { @code Rule}, or None if no rule was found
   */
  def parseLine(line: String): Option[Rule] = {
    if (line == null) None
    else {
      val trimLine = line.trim
      if (trimLine.isEmpty || commentLine.matcher(trimLine).matches || whiteSpaceLine.matcher(trimLine).matches)
        None
      else {
        val matcher = ruleLine.matcher(trimLine)
        if (matcher.find) Option(Rule(matcher.group(1))) else None
      }
    }
  }

}
