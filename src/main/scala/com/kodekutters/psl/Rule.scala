package com.kodekutters.psl


/**
 * The Public Suffix rule.
 */

object Rule {

  val EXCEPTION_TOKEN = "!"
  val WILDCARD = "*"
  val DEFAULT_RULE = new Rule(WILDCARD)

  def apply(pattern: String) = {
    val exceptionRule = pattern.startsWith(Rule.EXCEPTION_TOKEN)
    val matchPattern = if (exceptionRule) pattern.substring(1) else pattern
    new Rule(matchPattern, exceptionRule)
  }

}

/**
 * A Public Suffix rule.
 *
 * @param pattern the rule matching pattern
 * @param exceptionRule is an exception rule or not
 */
case class Rule(pattern: String, exceptionRule: Boolean) {

  def this(pattern: String) = this(pattern, false)

  import Rule._

  private val matcher = new RuleMatcher(pattern)

  /**
   * The exception token is not included in the pattern
   */
  def getPattern = matcher.getPattern

  /**
   * The label count is used for determining the prevailing rule.
   */
  def getLabelCount = Util.splitLabels(matcher.getPattern).length

  /**
   * Returns the matched public suffix of a domain.
   */
  def doMatch(domain: String): Option[String] = {
    matcher.doMatch(domain).flatMap(mtch =>
      if (exceptionRule) Option(Util.joinLabels(Util.splitLabels(mtch).drop(1).toList)) else Option(mtch))
  }

  override def toString = if (exceptionRule) EXCEPTION_TOKEN + matcher.toString else matcher.toString

}
