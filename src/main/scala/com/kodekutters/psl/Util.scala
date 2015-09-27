package com.kodekutters.psl

/**
 * supporting utilities
 */

object Util {

  /**
   * Splits a domain or pattern into its labels. Splitting is done at "."
   *
   * @param domain the domain name or rule pattern
   * @return the domain or rule label, null returns null
   */
  def splitLabels(domain: String): Array[String] = domain.split('.')

  /**
   * Joins labels to a domain name or rule pattern. Joining is done with "."
   *
   * @param labels the domain or rule labels
   * @return the domain name or rule pattern
   */
  def joinLabels(labels: List[String]): String = labels.mkString(".")

  /**
   * Matches a string pattern with a string label.
   * Empty strings or null never match. Matching is case insensitive.
   *
   * @param pattern the rule pattern
   * @param label the label
   * @return true if the label matches the pattern
   */
  def isLabelMatch(pattern: String, label: String): Boolean = {
      if (pattern == null || pattern.isEmpty || label == null || label.isEmpty) false
      else if (pattern == Rule.WILDCARD) true
      else pattern.equalsIgnoreCase(label)
  }

}
