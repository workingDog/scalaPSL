package com.kodekutters.psl


/**
 * Orders prevailing rules higher.
 *
 * The rule with the highest {@link Rule#getLabelCount()} is the prevailing rule.
 * An exception rule is always the prevailing rule.
 */
object RuleComparator extends Ordering[Rule] {

  override def compare(rule1: Rule, rule2: Rule): Int = {
    if (rule1.exceptionRule && rule2.exceptionRule) 0
    else if (rule1.exceptionRule) 1
    else if (rule2.exceptionRule) -1
    else rule1.getLabelCount.compareTo(rule2.getLabelCount)
  }

}