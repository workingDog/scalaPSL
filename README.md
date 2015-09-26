# Public Suffix List Scala API

From reference 1 [Public Suffix List](https://publicsuffix.org/):

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
the only method is to create a list. This is the aim of the Public Suffix List.

This Scala library presents a simple API to use the Public Suffix List. 
This Scala library is a basic conversion of the [Java code of reference 2] (https://github.com/whois-server-list/public-suffix-list)

## Documentation

The Public Suffix List is a list of simple rules for all known public suffixes, see [documentation](https://publicsuffix.org/)

## References

1) The Public Suffix List at: https://publicsuffix.org/

2) Original java code at: https://github.com/whois-server-list/public-suffix-list

3) Other code at: https://github.com/wrangr/psl

## How to use

Very simple to use, see the Example.scala and TestApp.scala

## Dependencies

Uses scala 2.11.7 and TypeSafe [Configuration library for JVM languages] (https://github.com/typesafehub/config). 
Also you MUST download the latest "Public Suffix List" from reference 1, and put it in the data directory.

## Status

Passes all tests, see TestApp.scala and the [test file] (https://raw.githubusercontent.com/publicsuffix/list/master/tests/test_psl.txt)

