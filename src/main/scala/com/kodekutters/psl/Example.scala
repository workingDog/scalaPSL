package com.kodekutters.psl

/**
 * a simple example of using PublicSuffixList
 */
object Example {

  def main(args: Array[String]) {
    val psl = PublicSuffixList()
    println("the public suffix of \"www.example.net\" is: " + psl.publicSuffix("www.example.net").get)
    println("\"www.example.net\" is a public suffix: " + psl.isPublicSuffix("www.example.net"))
    println("\"www.example.net\" is registrable: " + psl.isRegistrable("www.example.net"))
    println("the registrable domain of: www.example.net is: " + psl.registrable("www.example.net").get)
    println()
    val domain = "x.a.b.c.kobe.jp"
    println("domain: " + domain + "\n  tld: " + psl.tld(domain) + "\n  sld: " + psl.sld(domain) + "\n  trd: " + psl.trd(domain))
    println()
    println("domainLevel 1: " + psl.domainLevel(domain, 1))
    println("domainLevel 2: " + psl.domainLevel(domain, 2))
    println("domainLevel 3: " + psl.domainLevel(domain, 3))
    println("domainLevel 4: " + psl.domainLevel(domain, 4))
    println("domainLevel 5: " + psl.domainLevel(domain, 5))
    println()
    psl.domainLevels(domain).foreach(lvl => println("domainLevels: " + lvl))
  }

}
