package com.kodekutters.psl

/**
 * List based Rule index.
 */
final class ListIndex(val rules: List[Rule]) extends Index {

  protected def findRules(domain: String): List[Rule] = rules.filter(_.doMatch(domain).isDefined)

  def getRules = rules
}