package com.kodekutters.psl


object LabelMatcher {
  def apply(pattern: String) = new LabelMatcher(pattern)
}

/**
 * The label matcher. The matcher is case insensitive.
 *
 * @param pattern the rule pattern
 */
class LabelMatcher(val pattern: String) {

  /**
   * Matches a string label. Empty labels or null never match. Matching is case insensitive.
   *
   * @param label the label
   * @return { @code true} if the label matches
   */
  def isMatch(label: String): Boolean = {
    if (label == null || label.isEmpty) false
    else if (pattern == Rule.WILDCARD) true
    else pattern.equalsIgnoreCase(label)
  }

  override def toString = pattern

}
