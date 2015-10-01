package com.kodekutters.psl

import java.net.IDN

/**
 * Automatic Punycode Codec.
 *
 * This codec remembers the input type as encoded or not.
 * The recode() method will return the same format as the original input.
 *
 */
final class PunycodeAutoDecoder() {
  /**
   * the decode state
   */
  private var decoded = false

  /**
   * Decodes a domain name into UTF-8 if it was originally in Punycode ASCII.
   *
   * If the domain name is already UTF-8 no change occurs.
   * The original format (Punycode or UTF-8) is saved in decoded.
   *
   * @param domain the domain name
   * @return the UTF-8 domain name
   */
  def decode(domain: String): String = {
    val asciiDomain = IDN.toUnicode(domain)
    decoded = !(asciiDomain == domain)
    asciiDomain
  }

  /**
   * Returns the UTF-8 domain name in the original format.
   *
   * The original format is Punycode ASCII or UTF-8. The format is determined in decode.
   *
   * @param domain the UTF-8 domain name
   * @return the domain name in the original format
   */
  def recode(domain: String): String = if (decoded) IDN.toASCII(domain) else domain

  /**
   * determines if the original format was Punnycode ASCII.
   * The original format is set in decode.
   *
   * @return true if the original format was Punnycode ASCII
   */
  def isConverted: Boolean = decoded

}
