package com.kodekutters.psl

/**
 * utility support object
 */

object Util {

  /**
   * Splits a domain or pattern into its labels. Splitting is done at "."
   *
   * @param domain the domain name or rule pattern, null returns null
   * @return the domain or rule label
   */
  def splitLabels(domain: String): Array[String] = domain.split('.')

  /**
   * Joins labels to a domain name or rule pattern. Joining is done with "."
   *
   * @param labels the domain or rule labels
   * @return the domain name or rule pattern
   */
  def joinLabels(labels: List[String]): String = labels.mkString(".")

}
