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

  /**
    * Orders prevailing rules higher.
    *
    * The rule with the highest getLabelCount is the prevailing rule.
    * An exception rule is always the prevailing rule.
    */
  implicit val order = new Ordering[Rule] {
    def compare(rule1: Rule, rule2: Rule): Int = {
      if (rule1.exceptionRule && rule2.exceptionRule) 0
      else if (rule1.exceptionRule) 1
      else if (rule2.exceptionRule) -1
      else rule1.labelCount.compareTo(rule2.labelCount)
    }
  }

  /**
    * Matches a string pattern with a string label.
    * Empty strings or null never match. Matching is case insensitive.
    *
    * @return true if the label matches the rule pattern
    */
  private def isLabelMatch(pattern: String, label: String): Boolean = {
    if (pattern == null || pattern.isEmpty || label == null || label.isEmpty) false
    else if (pattern == Rule.WILDCARD) true
    else pattern.equalsIgnoreCase(label)
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

  /**
    * Rule labels in reversed order.
    */
  private val reversedLabels = pattern.split('.').reverse

  /**
    * Returns the matched public suffix of the input domain name
    */
  def doMatch(domain: String): Option[String] = {
    if (domain == null || domain.isEmpty) None
    else {
      val reversedDomainLabels = domain.split('.').reverse
      if (reversedDomainLabels.length < reversedLabels.length) None
      else {
        val reversedMatchedLabels = new Array[String](reversedLabels.length)
        var matchOk = true
        for (i <- reversedLabels.indices) {
          if (i < reversedDomainLabels.length && isLabelMatch(reversedLabels(i), reversedDomainLabels(i)))
            reversedMatchedLabels(i) = reversedDomainLabels(i)
          else
            matchOk = false
        }
        if (matchOk) {
          val mtch = reversedMatchedLabels.reverse.mkString(".")
          if (exceptionRule) Option(mtch.split('.').drop(1).mkString(".")) else Option(mtch)
        }
        else
          None
      }
    }
  }

  /**
    * The label count is the number of constituent labels of a rule pattern,
    * it is used for determining the prevailing rule.
    */
  val labelCount: Int = pattern.split('.').length

  override def toString = if (exceptionRule) EXCEPTION_TOKEN + pattern else pattern

}
