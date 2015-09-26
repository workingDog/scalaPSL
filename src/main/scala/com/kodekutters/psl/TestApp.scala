package com.kodekutters.psl

/**
 * test of the PublicSuffixList
 *
 * references:
 *    https://publicsuffix.org/list/
 *    https://raw.githubusercontent.com/publicsuffix/list/master/tests/test_psl.txt
 */
object TestApp {

  def main(args: Array[String]) {
    test1()
    test2()
  }

  def test1(): Unit = {
    println("......test1......")
    val psl = PublicSuffixList()
    println("net isPublicSuffix should be true ---> "+ psl.isPublicSuffix("net"))
    println("net isRegistrable should be false ---> " + psl.isRegistrable("net"))
    println("net getRegistrableDomain should be None ---> " + psl.getRegistrableDomain("net"))
    println("example.net isPublicSuffix should be false ---> "+ psl.isPublicSuffix("example.net"))
    println("example.net isRegistrable should be true ---> " + psl.isRegistrable("example.net"))
    println("www.example.net isRegistrable should be false ---> " + psl.isRegistrable("www.example.net"))
    println("example.net getRegistrableDomain should be Some(example.net) ---> " + psl.getRegistrableDomain("example.net"))
    println("www.example.net getRegistrableDomain should be Some(example.net) ---> " + psl.getRegistrableDomain("www.example.net"))
    println("example.co.uk getRegistrableDomain should be Some(co.uk) ---> " + psl.getRegistrableDomain("example.co.uk"))
    println("www.example.co.uk getRegistrableDomain should be Some(co.uk) ---> " + psl.getRegistrableDomain("www.example.co.uk"))
    println()
  }

  def test2(): Unit = {
    println("......test2......")
    val psl = PublicSuffixList()
    // null input.
    psl.checkPublicSuffix(null, null)
    // Mixed case.
    psl.checkPublicSuffix("COM", null)
    psl.checkPublicSuffix("example.COM", "example.com")
    psl.checkPublicSuffix("WwW.example.COM", "example.com")
    // Leading dot.
    psl.checkPublicSuffix(".com", null)
    psl.checkPublicSuffix(".example", null)
    psl.checkPublicSuffix(".example.com", null)
    psl.checkPublicSuffix(".example.example", null)
    // Unlisted TLD.
    psl.checkPublicSuffix("example", null)
    psl.checkPublicSuffix("example.example", "example.example")
    psl.checkPublicSuffix("b.example.example", "example.example")
    psl.checkPublicSuffix("a.b.example.example", "example.example")
    // Listed, but non-Internet, TLD.
    // psl.checkPublicSuffix("local", null)
    // psl.checkPublicSuffix("example.local", null)
    // psl.checkPublicSuffix("b.example.local", null)
    // psl.checkPublicSuffix("a.b.example.local", null)
    // TLD with only 1 rule.
    psl.checkPublicSuffix("biz", null)
    psl.checkPublicSuffix("domain.biz", "domain.biz")
    psl.checkPublicSuffix("b.domain.biz", "domain.biz")
    psl.checkPublicSuffix("a.b.domain.biz", "domain.biz")
    // TLD with some 2-level rules.
    psl.checkPublicSuffix("com", null)
    psl.checkPublicSuffix("example.com", "example.com")
    psl.checkPublicSuffix("b.example.com", "example.com")
    psl.checkPublicSuffix("a.b.example.com", "example.com")
    psl.checkPublicSuffix("uk.com", null)
    psl.checkPublicSuffix("example.uk.com", "example.uk.com")
    psl.checkPublicSuffix("b.example.uk.com", "example.uk.com")
    psl.checkPublicSuffix("a.b.example.uk.com", "example.uk.com")
    psl.checkPublicSuffix("test.ac", "test.ac")
    // TLD with only 1 (wildcard) rule.
    psl.checkPublicSuffix("il", null)
    psl.checkPublicSuffix("c.il", null)
    psl.checkPublicSuffix("b.c.il", "b.c.il")
    psl.checkPublicSuffix("a.b.c.il", "b.c.il")
    // More complex TLD.
    psl.checkPublicSuffix("jp", null)
    psl.checkPublicSuffix("test.jp", "test.jp")
    psl.checkPublicSuffix("www.test.jp", "test.jp")
    psl.checkPublicSuffix("ac.jp", null)
    psl.checkPublicSuffix("test.ac.jp", "test.ac.jp")
    psl.checkPublicSuffix("www.test.ac.jp", "test.ac.jp")
    psl.checkPublicSuffix("kyoto.jp", null)
    psl.checkPublicSuffix("test.kyoto.jp", "test.kyoto.jp")
    psl.checkPublicSuffix("ide.kyoto.jp", null)
    psl.checkPublicSuffix("b.ide.kyoto.jp", "b.ide.kyoto.jp")
    psl.checkPublicSuffix("a.b.ide.kyoto.jp", "b.ide.kyoto.jp")
    psl.checkPublicSuffix("c.kobe.jp", null)
    psl.checkPublicSuffix("b.c.kobe.jp", "b.c.kobe.jp")
    psl.checkPublicSuffix("a.b.c.kobe.jp", "b.c.kobe.jp")
    psl.checkPublicSuffix("city.kobe.jp", "city.kobe.jp")
    psl.checkPublicSuffix("www.city.kobe.jp", "city.kobe.jp")
    // TLD with a wildcard rule and exceptions.
    psl.checkPublicSuffix("ck", null)
    psl.checkPublicSuffix("test.ck", null)
    psl.checkPublicSuffix("b.test.ck", "b.test.ck")
    psl.checkPublicSuffix("a.b.test.ck", "b.test.ck")
    psl.checkPublicSuffix("www.ck", "www.ck")
    psl.checkPublicSuffix("www.www.ck", "www.ck")
    // US K12.
    psl.checkPublicSuffix("us", null)
    psl.checkPublicSuffix("test.us", "test.us")
    psl.checkPublicSuffix("www.test.us", "test.us")
    psl.checkPublicSuffix("ak.us", null)
    psl.checkPublicSuffix("test.ak.us", "test.ak.us")
    psl.checkPublicSuffix("www.test.ak.us", "test.ak.us")
    psl.checkPublicSuffix("k12.ak.us", null)
    psl.checkPublicSuffix("test.k12.ak.us", "test.k12.ak.us")
    psl.checkPublicSuffix("www.test.k12.ak.us", "test.k12.ak.us")
    // IDN labels.
    psl.checkPublicSuffix("食狮.com.cn", "食狮.com.cn")
    psl.checkPublicSuffix("食狮.公司.cn", "食狮.公司.cn")
    psl.checkPublicSuffix("www.食狮.公司.cn", "食狮.公司.cn")
    psl.checkPublicSuffix("shishi.公司.cn", "shishi.公司.cn")
    psl.checkPublicSuffix("公司.cn", null)
    psl.checkPublicSuffix("食狮.中国", "食狮.中国")
    psl.checkPublicSuffix("www.食狮.中国", "食狮.中国")
    psl.checkPublicSuffix("shishi.中国", "shishi.中国")
    psl.checkPublicSuffix("中国", null)
    // Same as above, but punycoded.
    psl.checkPublicSuffix("xn--85x722f.com.cn", "xn--85x722f.com.cn")
    psl.checkPublicSuffix("xn--85x722f.xn--55qx5d.cn", "xn--85x722f.xn--55qx5d.cn")
    psl.checkPublicSuffix("www.xn--85x722f.xn--55qx5d.cn", "xn--85x722f.xn--55qx5d.cn")
    psl.checkPublicSuffix("shishi.xn--55qx5d.cn", "shishi.xn--55qx5d.cn")
    psl.checkPublicSuffix("xn--55qx5d.cn", null)
    psl.checkPublicSuffix("xn--85x722f.xn--fiqs8s", "xn--85x722f.xn--fiqs8s")
    psl.checkPublicSuffix("www.xn--85x722f.xn--fiqs8s", "xn--85x722f.xn--fiqs8s")
    psl.checkPublicSuffix("shishi.xn--fiqs8s", "shishi.xn--fiqs8s")
    psl.checkPublicSuffix("xn--fiqs8s", null)
    println()
  }

}
