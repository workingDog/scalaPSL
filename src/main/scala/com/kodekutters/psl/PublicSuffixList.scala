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
      // if true print the basic input checks error messages, else suppress the printing, see BasicChecker
      val printFlag = properties.getBoolean("psl.printChecks")
      // character encoding of the list, default codec is "UTF-8"
      implicit val charset = if (properties.getString("psl.charset").isEmpty) Codec.UTF8 else Codec(properties.getString("psl.charset"))
      // the PSL file from the URL to the Public Suffix List (PSL).
      var sourceBuffer = Source.fromURL(new URL(properties.getString("psl.url"))) // implicit codec charset
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
  * https://github.com/whois-server-list/public-suffix-list
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
  def registrable(domain: String): Option[String] = {
    if (isValidInput(domain)) {
      val punycode = new PunyCodeAutoDecoder()
      val decodedDomain = punycode.decode(domain.toLowerCase)
      getPublicSuffix(decodedDomain) match {
        case Some(suffix) if decodedDomain != suffix =>
          val labels = decodedDomain.split('.')
          val offset = labels.length - suffix.split('.').length - 1
          val registrableDomain = labels.slice(offset, labels.length).mkString(".")
          Option(punycode.recode(registrableDomain))

        case _ => None
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
  def isRegistrable(domain: String): Boolean = registrable(domain).contains(domain.toLowerCase)

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

  /**
    * returns the public suffix of a domain.
    *
    * If the domain is already a public suffix, it will be returned unchanged.
    * e.g. "www.example.net" will return "net".
    *
    * @param domain the domain name
    * @return the public suffix of a domain, None if none found
    */
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

  /**
    * do basic checks of the input domain name
    */
  private def isValidInput(domain: String): Boolean = !(domain == null || domain.isEmpty || domain.charAt(0) == '.' || !BasicChecker.isValid(domain, printFlag))

  /**
    * returns the top level public domain name if it exist else None
    */
  def tld(domain: String) = domainLevel(domain, 1)

  /**
    * returns the second level public domain name if it exist else None
    */
  def sld(domain: String) = domainLevel(domain, 2)

  /**
    * returns the third level public domain name if it exist else None
    */
  def trd(domain: String) = domainLevel(domain, 3)

  /**
    * returns the desired registrable public domain level name if it exist else None
    * @param domain input domain name
    * @param level desired level should be > 0, e.g. level=1 gives TLD
    */
  def domainLevel(domain: String, level: Int): Option[String] = {
    if (level < 1) None
    else {
      val lvl = level - 1
      registrable(domain) match {
        case None => None
        case Some(regDomain) =>
          val labels = regDomain.split('.')
          if (labels.length > lvl) Option(labels.dropRight(lvl).last) else None
      }
    }
  }

  /**
    * returns all registrable public domain levels as an Array[String] starting with TLD at index 0
    * @param domain input domain name
    */
  def domainLevels(domain: String): Array[String] = {
    registrable(domain) match {
      case None => Array[String]()
      case Some(regDomain) => for (label <- regDomain.split('.').reverse) yield label
    }
  }

}