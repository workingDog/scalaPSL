package com.kodekutters.psl

import scala.util.Sorting


/**
 * Rule index.
 */
abstract class Index {
  /**
   * Finds a list of matching rules.
   * This list may not include all matching rules, but must include the prevailing rule.
   */
  protected def findRules(domain: String): List[Rule]

  /**
   * Returns all rules of this index.
   */
  def getRules: List[Rule]

  /**
   * Finds the prevailing rule.
   */
  final def findRule(domain: String): Option[Rule] = {
    val theList = findRules(domain).toArray
    Sorting.quickSort(theList)(RuleComparator)
    theList match {
      case list if list.isEmpty => None
      case list => Option(list.last)
    }
  }

}