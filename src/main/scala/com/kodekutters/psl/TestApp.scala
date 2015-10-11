package com.kodekutters.psl


/**
 * test of the PublicSuffixList
 *
 * references:
 *
 * https://publicsuffix.org/list/
 *
 * https://raw.githubusercontent.com/publicsuffix/list/master/tests/test_psl.txt
 */
object TestApp {

  def main(args: Array[String]) {
    val psl = PublicSuffixList()
    test1(psl)
    test2(psl)
    test3(psl)
  //  timeThis { for (i <- 0 to 20) test2(psl) }
  }

  /**
   * for testing the test data at: https://raw.githubusercontent.com/publicsuffix/list/master/tests/test_psl.txt
   */
  private def checkPublicSuffix(domain: String, expected: String)(implicit psl: PublicSuffixList): Unit = {
    psl.registrable(domain) match {
      case None => println(Option(expected).isEmpty + "  tld: " + psl.tld(domain) + " sld: " + psl.sld(domain) + " trd: " + psl.trd(domain) + "  input domain: " + domain + " expected: " + expected)
      case Some(regDomain) => println((regDomain == expected) + "  tld: " + psl.tld(domain) + " sld: " + psl.sld(domain) + " trd: " + psl.trd(domain) + "  input domain: " + domain + " expected: " + expected)
    }
  }

  /**
   * for timing a block of code
   */
  private def timeThis[R](block: => R): R = {
    val t0 = System.currentTimeMillis()
    val result = block // call-by-name
    val t1 = System.currentTimeMillis()
    println("elapsed time: " + (t1 - t0) / 1000.0 + "s")
    result
  }

  def test1(psl: PublicSuffixList): Unit = {
    println("......test1......")
    println("net isPublicSuffix should be true ---> " + psl.isPublicSuffix("net"))
    println("net isRegistrable should be false ---> " + psl.isRegistrable("net"))
    println("net getRegistrableDomain should be None ---> " + psl.registrable("net"))
    println("example.net isPublicSuffix should be false ---> " + psl.isPublicSuffix("example.net"))
    println("example.net isRegistrable should be true ---> " + psl.isRegistrable("example.net"))
    println("www.example.net isRegistrable should be false ---> " + psl.isRegistrable("www.example.net"))
    println("example.net getRegistrableDomain should be Some(example.net) ---> " + psl.registrable("example.net"))
    println("www.example.net getRegistrableDomain should be Some(example.net) ---> " + psl.registrable("www.example.net"))
    println("example.co.uk getRegistrableDomain should be Some(example.co.uk) ---> " + psl.registrable("example.co.uk"))
    println("www.example.co.uk getRegistrableDomain should be Some(example.co.uk) ---> " + psl.registrable("www.example.co.uk"))
    println("食狮.com.cn getRegistrableDomain should be Some(食狮.com.cn) ---> " + psl.registrable("食狮.com.cn"))
    println("xn--85x722f.com.cn getRegistrableDomain should be Some(xn--85x722f.com.cn) ---> " + psl.registrable("xn--85x722f.com.cn"))
  }

  def test2(implicit psl: PublicSuffixList): Unit = {
    println("......test2......")
    // null input.
    checkPublicSuffix(null, null)
    // Mixed case.
    checkPublicSuffix("COM", null)
    checkPublicSuffix("example.COM", "example.com")
    checkPublicSuffix("WwW.example.COM", "example.com")
    // Leading dot.
    checkPublicSuffix(".com", null)
    checkPublicSuffix(".example", null)
    checkPublicSuffix(".example.com", null)
    checkPublicSuffix(".example.example", null)
    // Unlisted TLD.
    checkPublicSuffix("example", null)
    checkPublicSuffix("example.example", "example.example")
    checkPublicSuffix("b.example.example", "example.example")
    checkPublicSuffix("a.b.example.example", "example.example")
    // Listed, but non-Internet, TLD.
    // checkPublicSuffix("local", null)
    // checkPublicSuffix("example.local", null)
    // checkPublicSuffix("b.example.local", null)
    // checkPublicSuffix("a.b.example.local", null)
    // TLD with only 1 rule.
    checkPublicSuffix("biz", null)
    checkPublicSuffix("domain.biz", "domain.biz")
    checkPublicSuffix("b.domain.biz", "domain.biz")
    checkPublicSuffix("a.b.domain.biz", "domain.biz")
    // TLD with some 2-level rules.
    checkPublicSuffix("com", null)
    checkPublicSuffix("example.com", "example.com")
    checkPublicSuffix("b.example.com", "example.com")
    checkPublicSuffix("a.b.example.com", "example.com")
    checkPublicSuffix("uk.com", null)
    checkPublicSuffix("example.uk.com", "example.uk.com")
    checkPublicSuffix("b.example.uk.com", "example.uk.com")
    checkPublicSuffix("a.b.example.uk.com", "example.uk.com")
    checkPublicSuffix("test.ac", "test.ac")
    // TLD with only 1 (wildcard) rule.
    checkPublicSuffix("il", null)
    checkPublicSuffix("c.il", null)
    checkPublicSuffix("b.c.il", "b.c.il")
    checkPublicSuffix("a.b.c.il", "b.c.il")
    // More complex TLD.
    checkPublicSuffix("jp", null)
    checkPublicSuffix("test.jp", "test.jp")
    checkPublicSuffix("www.test.jp", "test.jp")
    checkPublicSuffix("ac.jp", null)
    checkPublicSuffix("test.ac.jp", "test.ac.jp")
    checkPublicSuffix("www.test.ac.jp", "test.ac.jp")
    checkPublicSuffix("kyoto.jp", null)
    checkPublicSuffix("test.kyoto.jp", "test.kyoto.jp")
    checkPublicSuffix("ide.kyoto.jp", null)
    checkPublicSuffix("b.ide.kyoto.jp", "b.ide.kyoto.jp")
    checkPublicSuffix("a.b.ide.kyoto.jp", "b.ide.kyoto.jp")
    checkPublicSuffix("c.kobe.jp", null)
    checkPublicSuffix("b.c.kobe.jp", "b.c.kobe.jp")
    checkPublicSuffix("a.b.c.kobe.jp", "b.c.kobe.jp")
    checkPublicSuffix("city.kobe.jp", "city.kobe.jp")
    checkPublicSuffix("www.city.kobe.jp", "city.kobe.jp")
    // TLD with a wildcard rule and exceptions.
    checkPublicSuffix("ck", null)
    checkPublicSuffix("test.ck", null)
    checkPublicSuffix("b.test.ck", "b.test.ck")
    checkPublicSuffix("a.b.test.ck", "b.test.ck")
    checkPublicSuffix("www.ck", "www.ck")
    checkPublicSuffix("www.www.ck", "www.ck")
    // US K12.
    checkPublicSuffix("us", null)
    checkPublicSuffix("test.us", "test.us")
    checkPublicSuffix("www.test.us", "test.us")
    checkPublicSuffix("ak.us", null)
    checkPublicSuffix("test.ak.us", "test.ak.us")
    checkPublicSuffix("www.test.ak.us", "test.ak.us")
    checkPublicSuffix("k12.ak.us", null)
    checkPublicSuffix("test.k12.ak.us", "test.k12.ak.us")
    checkPublicSuffix("www.test.k12.ak.us", "test.k12.ak.us")
    // IDN labels.
    checkPublicSuffix("食狮.com.cn", "食狮.com.cn")
    checkPublicSuffix("食狮.公司.cn", "食狮.公司.cn")
    checkPublicSuffix("www.食狮.公司.cn", "食狮.公司.cn")
    checkPublicSuffix("shishi.公司.cn", "shishi.公司.cn")
    checkPublicSuffix("公司.cn", null)
    checkPublicSuffix("食狮.中国", "食狮.中国")
    checkPublicSuffix("www.食狮.中国", "食狮.中国")
    checkPublicSuffix("shishi.中国", "shishi.中国")
    checkPublicSuffix("中国", null)
    // Same as above, but punycoded.
    checkPublicSuffix("xn--85x722f.com.cn", "xn--85x722f.com.cn")
    checkPublicSuffix("xn--85x722f.xn--55qx5d.cn", "xn--85x722f.xn--55qx5d.cn")
    checkPublicSuffix("www.xn--85x722f.xn--55qx5d.cn", "xn--85x722f.xn--55qx5d.cn")
    checkPublicSuffix("shishi.xn--55qx5d.cn", "shishi.xn--55qx5d.cn")
    checkPublicSuffix("xn--55qx5d.cn", null)
    checkPublicSuffix("xn--85x722f.xn--fiqs8s", "xn--85x722f.xn--fiqs8s")
    checkPublicSuffix("www.xn--85x722f.xn--fiqs8s", "xn--85x722f.xn--fiqs8s")
    checkPublicSuffix("shishi.xn--fiqs8s", "shishi.xn--fiqs8s")
    checkPublicSuffix("xn--fiqs8s", null)
  }

  // bad characters tests
  def test3(psl: PublicSuffixList) = {
    println("......test3......")
    println("net@ubx getRegistrableDomain should return None and print an error message if psl.printChecks=true ---> " + psl.registrable("net@ubx"))
  }

}
