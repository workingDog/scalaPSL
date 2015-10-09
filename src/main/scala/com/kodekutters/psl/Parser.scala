package com.kodekutters.psl

import scala.collection.parallel.immutable.ParSeq
import scala.io.BufferedSource


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
 * The Public Suffix List source input is returned as a list of {@link Rule}.
 */
class Parser {

  import Parser._

  /**
   * Parse all rules from the source and return the list of rules
   *
   * @param source contains the rules one to each line
   *
   */
  def parse(source: BufferedSource): ParSeq[Rule] = (for (line <- source.getLines()) yield parseLine(line)).flatten.toList.par

  /**
   * Parse a string and return a rule or None if no rule was found
   *
   * @param line the line string with one rule
   * @return the parsed Rule or None if no rule was found
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
