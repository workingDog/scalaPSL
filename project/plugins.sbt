
resolvers += Resolver.sonatypeRepo("public")

resolvers += Resolver.sonatypeRepo("releases")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.10")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.1")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "2.3")
