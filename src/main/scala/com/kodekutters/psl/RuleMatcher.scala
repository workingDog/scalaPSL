package com.kodekutters.psl

/**
 * The rule matcher. The matcher is case insensitive.
 */
class RuleMatcher(labels: Array[String]) {

  def this(pattern: String) = this(pattern.split('.'))

  /**
   * Rule labels in reversed order.
   */
  private val reversedLabels = labels.reverse

  /**
   * Returns the matched public suffix of the input domain name. Matching is case insensitive.
   *
   * @param domain the domain name
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
        if (matchOk) Option(reversedMatchedLabels.reverse.mkString(".")) else None
      }
    }
  }

  /**
   * Matches a string pattern with a string label.
   * Empty strings or null never match. Matching is case insensitive.
   *
   * @return true if the label matches the pattern
   */
  private def isLabelMatch(pattern: String, label: String): Boolean = {
    if (pattern == null || pattern.isEmpty || label == null || label.isEmpty) false
    else if (pattern == Rule.WILDCARD) true
    else pattern.equalsIgnoreCase(label)
  }

  /**
   * Returns the rule pattern.
   */
  def getPattern: String = reversedLabels.mkString(".").reverse

  override def toString = getPattern

}
