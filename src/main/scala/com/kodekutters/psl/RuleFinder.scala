package com.kodekutters.psl

import scala.collection.parallel.immutable.ParSeq
import scala.util.Sorting

/**
 * A List based Rule finder.
 */
final class RuleFinder(val rules: ParSeq[Rule]) {

  /**
   * Finds a list of matching rules.
   * This list may not include all matching rules, but includes the prevailing rule.
   */
  protected def findRules(domain: String): List[Rule] = rules.filter(_.doMatch(domain).isDefined).toList

  /**
   * Finds the prevailing rule from the list of rules.
   */
  def findRule(domain: String): Option[Rule] = {
    val theMatchedRules = findRules(domain).toArray
    Sorting.quickSort(theMatchedRules)
    theMatchedRules match {
      case list if list.isEmpty => None
      case list => Option(list.last)
    }
  }

}