package com.kodekutters.psl

import scala.collection.immutable.TreeSet

/**
 * TreeSet based Rule index.
 */
final class TreeSetIndex(val rules: List[Rule]) extends Index {

  private val treeSet = new TreeSet()(RuleComparator) ++ rules

  protected def findRules(domain: String): List[Rule] = rules.filter(_.doMatch(domain).isDefined)

  def getRules = treeSet.toList
}
