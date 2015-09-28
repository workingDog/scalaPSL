package com.kodekutters.psl

/**
 * The rule matcher. The matcher is case insensitive.
 */
class RuleMatcher(labels: Array[String]) {

  def this(pattern: String) = this(Util.splitLabels(pattern))

  /**
   * Rule labels in reversed order.
   */
  private val reversedLabels = labels.reverse

  /**
   * Returns the matched public suffix. Matching is case insensitive.
   *
   * @param domain the domain name
   */
  def doMatch(domain: String): Option[String] = {
    if (domain == null || domain.isEmpty) None
    else {
      val reversedDomainLabels = Util.splitLabels(domain).reverse
      if (reversedDomainLabels.length < reversedLabels.length) None
      else {
        val reversedMatchedLabels = new Array[String](reversedLabels.length)
        var matchOk = true
        for (i <- reversedLabels.indices) {
            if (i < reversedDomainLabels.length && Util.isLabelMatch(reversedLabels(i), reversedDomainLabels(i)))
              reversedMatchedLabels(i) = reversedDomainLabels(i)
            else
              matchOk = false
          }
        if (matchOk) Option(Util.joinLabels(reversedMatchedLabels.toList.reverse)) else None
      }
    }
  }

  /**
   * Returns the rule pattern.
   */
  def getPattern: String = Util.joinLabels(reversedLabels.toList.reverse)

  override def toString = getPattern

}
