package com.kodekutters.psl

import java.net.URL
import com.typesafe.config.{ConfigFactory, Config}
import scala.io.Codec


object PublicSuffixList {

  /**
   * URL of the Public Suffix List. ---> not yet used
   */
  val PROPERTY_URL = "psl.url"
  /**
   * Character encoding of the list.
   */
  val PROPERTY_CHARSET = "psl.charset"
  /**
   * The file name with the Public Suffix List rules.
   */
  val PROPERTY_LIST_FILE = "psl.file"

  /**
   * loads the default properties from the application.conf file in the resource directory.
   */
  def getDefaults: Config = ConfigFactory.load()

  /**
   * create a PublicSuffixList with custom properties.
   *
   * @param properties the configuration properties for building the { @link PublicSuffixList}.
   * @return a public suffix list created with the given properties
   *
   */
  def apply(properties: Config): PublicSuffixList = {
    try {
      val listStream = getClass.getResourceAsStream(properties.getString(PROPERTY_LIST_FILE))
      val url = new URL(properties.getString(PROPERTY_URL))
      val charset = Codec(properties.getString(PROPERTY_CHARSET))
      // parse the rules file into a list of rules and add the default rule to it
      val rules = Parser().parse(listStream, charset) :+ Rule.DEFAULT_RULE
      val index = new ListIndex(rules)
      new PublicSuffixList(index, url, charset)
    } catch {
      case e: Exception => println("exception caught: " + e); null
    }
  }

  /**
   * create a PublicSuffixList with the default properties.
   */
  def apply(): PublicSuffixList = apply(getDefaults)

}

/**
 * The Public Suffix List API.
 *
 * Use the methods with UTF-8 domain names or Punycode encoded ASCII domain names.
 * The methods will return the results in the same type as the input.
 *
 * references:
 *
 * https://publicsuffix.org/
 *
 * https://github.com/whois-server-list/public-suffix-list <-- the main java code source
 *
 * https://github.com/wrangr/psl
 *
 * @param index    the rule index
 * @param url      the Public Suffix List url
 * @param charset  the character encoding of the list
 *
 */
final class PublicSuffixList(val index: Index, val url: URL, val charset: Codec) {

  /**
   * gets the registrable domain.
   *
   * E.g. "www.example.net" and "example.net" will return "example.net".
   * Null, and empty string or domains with a leading dot will return None.
   *
   * @param domain the domain name
   * @return the registrable domain, None if the domain is not registrable
   */
  def getRegistrableDomain(domain: String): Option[String] = {
    if (isValidInput(domain)) {
      val punycode = new PunycodeAutoDecoder()
      val decodedDomain = punycode.decode(domain)
      doGetPublicSuffix(decodedDomain) match {
        case None => None
        case Some(suffix) =>
          if (decodedDomain == suffix) None
          else {
            val suffixLabels = Util.splitLabels(suffix)
            val labels = Util.splitLabels(decodedDomain)
            val offset = labels.length - suffixLabels.length - 1
            val registrableDomain = Util.joinLabels(labels.slice(offset, labels.length).toList)
            Option(punycode.recode(registrableDomain))
          }
      }
    }
    else
      None
  }

  /**
   * determines if a domain is registrable.
   *
   * E.g. "example.net" is registrable, "www.example.net" and "net" are not.
   *
   * @param domain the domain name
   * @return { @code true} if the domain is registrable
   */
  def isRegistrable(domain: String): Boolean = getRegistrableDomain(domain).exists(d => domain.toLowerCase == d)

  /**
   * returns the public suffix from a domain.
   *
   * If the domain is already a public suffix, it will be returned unchanged as an option.
   * E.g. "www.example.net" will return "net".
   *
   * @param domain the domain name
   * @return the public suffix, None if none matched
   */
  def getPublicSuffix(domain: String): Option[String] = {
    if (isValidInput(domain))
      doGetPublicSuffix(domain)
    else
      None
  }

  private def doGetPublicSuffix(domain: String): Option[String] = {
    val punycode = new PunycodeAutoDecoder()
    val decodedDomain = punycode.recode(domain)
    index.findRule(decodedDomain).flatMap(rule =>
      rule.doMatch(decodedDomain).map(dmain => punycode.decode(dmain)))
  }

  /**
   * determines if a domain is a public suffix or not.
   *
   * Example: "com" is a public suffix, "example.com" is not.
   *
   * @param domain the domain name
   * @return { @code true} if the domain is a public suffix
   */
  def isPublicSuffix(domain: String): Boolean = {
    if (isValidInput(domain))
      doGetPublicSuffix(domain).exists(d => domain.toLowerCase == d)
    else
      false
  }

  private def isValidInput(domain: String): Boolean = !(domain == null || domain.isEmpty() || domain.charAt(0) == '.' || !BasicChecker.isValid(domain))

  /**
   * for testing, see TestApp
   */
  def checkPublicSuffix(domain: String, expected: String): Unit = {
    getRegistrableDomain(domain) match {
      case None => println(Option(expected).isEmpty + "  domain: " + domain + " expected: " + expected)
      case Some(mtch) => println((mtch.toLowerCase == expected)+ "  domain: " + domain + " expected: " + expected)
    }
  }

}