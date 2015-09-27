package com.kodekutters.psl

import scala.util.Sorting

/**
 * A List based Rule finder.
 */
final class RuleFinder(val rules: List[Rule]) {

  // testing TreeSet
  //  private val treeSet = new TreeSet()(RuleComparator) ++ rules

  /**
   * Finds a list of matching rules.
   * This list may not include all matching rules, but includes the prevailing rule.
   */
  protected def findRules(domain: String): List[Rule] = rules.filter(_.doMatch(domain).isDefined)

  /**
   * Returns all rules
   */
  def getRules = rules

  /**
   * Finds the prevailing rule.
   */
  def findRule(domain: String): Option[Rule] = {
    val theMatchedRules = findRules(domain).toArray
    Sorting.quickSort(theMatchedRules)(RuleComparator)
    theMatchedRules match {
      case list if list.isEmpty => None
      case list => Option(list.last)
    }
  }

}