name := "tsdl-jena-test"

version := "0.1"

scalaVersion := "2.12.10"

libraryDependencies += "org.apache.jena" % "jena-core" % "3.13.1"
libraryDependencies += "org.apache.jena" % "jena-rdfconnection" % "3.13.1"

libraryDependencies += "dev.zio" %% "zio" % "1.0.0-RC17"
libraryDependencies += "dev.zio" %% "zio-streams" % "1.0.0-RC17"
libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.6.7"
libraryDependencies += "com.github.pureconfig" %% "pureconfig" % "0.12.1"
