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
    val domain = "example.uk.com"
    println("domain: " + domain + "\n  tld: " + psl.tld(domain) + "\n  sld: " + psl.sld(domain) + "\n  trd: " + psl.trd(domain) )
  }

}
