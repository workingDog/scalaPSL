package com.kodekutters.psl

/**
 * a simple example of using PublicSuffixList
 */
object Example {

  def main(args: Array[String]) {
    val psl = PublicSuffixList()
    println("the public suffix of \"www.example.net\" is: " + psl.getPublicSuffix("www.example.net").get)
    println("\"www.example.net\" is a public suffix: " + psl.isPublicSuffix("www.example.net"))
    println("\"www.example.net\" is registrable: " + psl.isRegistrable("www.example.net"))
    println("\"www.example.net\" getRegistrableDomain: " + psl.getRegistrableDomain("www.example.net"))
  }

}
