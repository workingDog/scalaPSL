package com.kodekutters.psl

import java.net.URL
import com.typesafe.config.{ConfigFactory, Config}
import scala.io.{Codec, Source}

/**
 * The Public Suffix List.
 *
 */
object PublicSuffixList {

  /**
   * URL to the Public Suffix List (PSL). for example:
   *
   * "https://publicsuffix.org/list/public_suffix_list.dat" or
   * "file:///Users/.../src/main/resources/public_suffix_list.dat"
   *
   */
  val PROPERTY_URL = "psl.url"
  /**
   * Character encoding of the list.
   */
  val PROPERTY_CHARSET = "psl.charset"
  /**
   * if true print the basic input checks error messages, else suppress the printing, see BasicChecker
   */
  val PRINT_CHECKS = "psl.printChecks"

  /**
   * loads the default properties from the application.conf file in the resources directory.
   */
  def getDefaults: Config = ConfigFactory.load()

  /**
   * create a PublicSuffixList with the given properties.
   *
   * @param properties the configuration properties for building the PublicSuffixList.
   *
   */
  def apply(properties: Config): PublicSuffixList = {
    try {
      val printFlag = properties.getBoolean(PRINT_CHECKS)
      // default codec is "UTF-8"
      implicit val charset = if (properties.getString(PROPERTY_CHARSET).isEmpty) Codec.UTF8 else Codec(properties.getString(PROPERTY_CHARSET))
      // the PSL file from the url
      var sourceBuffer = Source.fromURL(new URL(properties.getString(PROPERTY_URL))) // implicit codec charset
      // parse the rules file into a list of rules and add the default rule to it
      val rules = Parser().parse(sourceBuffer) :+ Rule.DEFAULT_RULE
      new PublicSuffixList(new RuleList(rules), printFlag)
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
 * The Public Suffix List.
 *
 * Use UTF-8 domain names or Punycode encoded ASCII domain names in the methods.
 * The methods will return the results in the same type as their input.
 *
 * references:
 *
 * https://publicsuffix.org/
 *
 * https://github.com/whois-server-list/public-suffix-list <-- the original java source code
 *
 */
final class PublicSuffixList(val ruleList: RuleList, val printFlag: Boolean) {

  /**
   * returns the registrable par of a domain.
   *
   * E.g. "www.example.net" and "example.net" will return "example.net".
   * Null, and empty string or domains with a leading dot or invalid input will return None.
   *
   * @param domain the domain name input
   * @return the registrable part of a domain, None if the domain is not registrable
   */
  def registrableDomain(domain: String): Option[String] = {
    if (isValidInput(domain)) {
      val punycode = new PunyCodeAutoDecoder()
      val decodedDomain = punycode.decode(domain.toLowerCase)
      getPublicSuffix(decodedDomain) match {
        case None => None
        case Some(suffix) =>
          if (decodedDomain == suffix) None
          else {
            val suffixLabels = suffix.split('.')
            val labels = decodedDomain.split('.')
            val offset = labels.length - suffixLabels.length - 1
            val registrableDomain = labels.slice(offset, labels.length).mkString(".")
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
   * @return true if the domain is registrable
   */
  def isRegistrable(domain: String): Boolean = registrableDomain(domain).contains(domain.toLowerCase)

  /**
   * returns the public suffix of a domain.
   *
   * If the domain is already a public suffix, it will be returned unchanged.
   * e.g. "www.example.net" will return "net".
   *
   * @param domain the domain name
   * @return the public suffix of a domain, None if none found
   */
  def publicSuffix(domain: String): Option[String] = if (isValidInput(domain)) getPublicSuffix(domain) else None

  private def getPublicSuffix(domain: String): Option[String] = {
    val punycode = new PunyCodeAutoDecoder()
    val decodedDomain = punycode.recode(domain.toLowerCase)
    ruleList.findRule(decodedDomain).flatMap(rule => rule.doMatch(decodedDomain).map(dmain => punycode.decode(dmain)))
  }

  /**
   * determines if a domain is a public suffix or not.
   *
   * e.g. "com" is a public suffix, "example.com" is not.
   *
   * @param domain the domain name
   * @return true if the domain is a public suffix
   */
  def isPublicSuffix(domain: String): Boolean = if (isValidInput(domain)) getPublicSuffix(domain).contains(domain.toLowerCase) else false

  private def isValidInput(domain: String): Boolean = !(domain == null || domain.isEmpty || domain.charAt(0) == '.' || !BasicChecker.isValid(domain, printFlag))

  /**
   * for testing, see TestApp
   */
  def checkPublicSuffix(domain: String, expected: String): Unit = {
    registrableDomain(domain) match {
      case None => println(Option(expected).isEmpty + "  tld: " + tld(domain) + " sld: " + sld(domain) + " trd: " + trd(domain) + "  input domain: " + domain + " expected: " + expected)
      case Some(regDomain) => println((regDomain == expected) + "  tld: " + tld(domain) + " sld: " + sld(domain) + " trd: " + trd(domain) + "  input domain: " + domain + " expected: " + expected)
    }
  }

  /**
   * returns the top level public domain name if it exist else None
   */
  def tld(domain: String): Option[String] = {
    registrableDomain(domain) match {
      case None => None
      case Some(regDomain) =>
        val labels = regDomain.split('.')
        if (labels.length >= 1) Option(labels.last) else None
    }
  }

  /**
   * returns the second level public domain name if it exist else None
   */
  def sld(domain: String): Option[String] = {
    registrableDomain(domain) match {
      case None => None
      case Some(regDomain) =>
        val labels = regDomain.split('.')
        if (labels.length > 1) Option(labels.dropRight(1).last) else None
    }
  }

  /**
   * returns the third level public domain name if it exist else None
   */
  def trd(domain: String): Option[String] = {
    registrableDomain(domain) match {
      case None => None
      case Some(regDomain) =>
        val labels = regDomain.split('.')
        if (labels.length > 2) Option(labels.dropRight(2).last) else None
    }
  }

}