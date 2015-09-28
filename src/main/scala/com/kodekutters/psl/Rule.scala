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

  implicit val order = new Ordering[Rule] {
    def compare(rule1: Rule, rule2: Rule): Int = {
      if (rule1.exceptionRule && rule2.exceptionRule) 0
      else if (rule1.exceptionRule) 1
      else if (rule2.exceptionRule) -1
      else rule1.getLabelCount.compareTo(rule2.getLabelCount)
    }
  }

}

/**
 * A Public Suffix rule.
 *
 * @param pattern  the rule matching pattern
 * @param exceptionRule  is an exception rule or not
 */
case class Rule(pattern: String, exceptionRule: Boolean) {

  def this(pattern: String) = this(pattern, false)

  import Rule._

  private val matcher = new RuleMatcher(pattern)

  /**
   * returns the rule pattern without the exception token "!"
   */
  def getPattern = matcher.getPattern

  /**
   * The label count is the number of constituent labels of a rule pattern,
   * it is used for determining the prevailing rule.
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
