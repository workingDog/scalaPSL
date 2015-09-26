package com.kodekutters.psl

/**
 * A List based Rule index.
 */
final class ListIndex(val rules: List[Rule]) extends Index {

  /**
   * Finds a list of matching rules.
   * This list may not include all matching rules, but includes the prevailing rule.
   */
  protected def findRules(domain: String): List[Rule] = rules.filter(_.doMatch(domain).isDefined)

  def getRules = rules
}