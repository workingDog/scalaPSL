package com.kodekutters.psl

import scala.collection.parallel.immutable.ParSeq
import scala.util.Sorting

/**
 * Holds the list of rules and provides for finding the prevailing rule.
 */
final class RuleList(val rules: ParSeq[Rule]) {

  /**
   * Finds the prevailing rule from the list of rules.
   */
  def findRule(domain: String): Option[Rule] = {
    // find the list of matching rules. This list may not include all matching rules, but includes the prevailing rule.
    val theMatchedRules = rules.filter(_.doMatch(domain).isDefined).toArray
    Sorting.quickSort(theMatchedRules)
    theMatchedRules match {
      case list if list.isEmpty => None
      case list => Option(list.last)
    }
  }

}