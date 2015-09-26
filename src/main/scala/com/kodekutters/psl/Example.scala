package com.kodekutters.psl


object Example {

  def main(args: Array[String]) {
    val psl = PublicSuffixList()
    println("the public suffix of \"example.net\" is: "+psl.getPublicSuffix("example.net").get)
  }

}
