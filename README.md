# Public Suffix List Scala API

This Scala library (**scalapsl**) presents a simple API to use the Public Suffix List[1]. With this library you can parse a **URL** into its component subdomains, such as: Top Level Domain (TLD), 
Second Level Domain (SLD), Third Level Domain (TRD), etc... registrable domain and public suffix parts.

From the [Public Suffix List](https://publicsuffix.org/):

A "public suffix" is one under which Internet users can (or historically could) directly register names. 
Some examples of public suffixes are .com, .co.uk and pvt.k12.ma.us. 
The Public Suffix List is a list of all known public suffixes.
The Public Suffix List is an initiative of Mozilla, but is maintained as a community resource. 
It is available for use in any software, but was originally created to meet the needs of browser manufacturers. 
It allows browsers to, for example:

-  Avoid privacy-damaging "supercookies" being set for high-level domain name suffixes
-  Highlight the most important part of a domain name in the user interface
-  Accurately sort history entries by site.

Since there was and remains no algorithmic method of finding the highest level at which a domain 
may be registered for a particular top-level domain (the policies differ with each registry), 
the only method is to create a list. This is the aim of the Public Suffix List. This Scala library is a basic conversion of the [Java code of reference 2](https://github.com/whois-server-list/public-suffix-list).


## Documentation

The Public Suffix List is a list of simple rules for all known public suffixes, see [documentation](https://publicsuffix.org/).

## Installation

Add the following dependency to your application *build.sbt*:

    libraryDependencies += "com.github.workingDog" %% "scalaspl" % "1.1"

Note the typo in the library name, should have been scalapsl.

To compile and generate an independent fat jar file from source, use [SBT](http://www.scala-sbt.org/):

    sbt assembly

The jar file **scalapsl-1.2-SNAPSHOT.jar** will be in created in the *./target/scala-2.12* directory.


## How to use

Very simple to use, see the **Example.scala** and **TestApp.scala**.

From *Example.scala*:

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


To read the PSL directly from reference 1, you need to be connected to the internet and 
have the following property set in the configuration file *application.conf*:
 
    psl.url="https://publicsuffix.org/list/public_suffix_list.dat"

If you do not want to read the list from the internet, but from a local file, use something like:

    psl.url="file:///Users/.../scalaPSL/src/main/resources/public_suffix_list.dat"

See *application.conf* example in the *./src/main/resources* directory.

## References

1) [The Public Suffix List](https://publicsuffix.org/)

2) [Original java code](https://github.com/whois-server-list/public-suffix-list)

3) [Other code](https://github.com/wrangr/psl)

## Dependencies

Uses scala 2.12.4 and TypeSafe [Configuration library for JVM languages](https://github.com/typesafehub/config). 
The latest *Public Suffix List* can be obtained from [The Public Suffix List on github](https://github.com/publicsuffix/list). 

## Status 

Passes all tests, see TestApp.scala and the [test file](https://raw.githubusercontent.com/publicsuffix/list/master/tests/test_psl.txt)

