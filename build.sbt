scalaVersion := "2.12.8"


name := "procaas-scala-client"
organization := "ch.epfl.scala"
version := "1.0"

libraryDependencies += "com.softwaremill.sttp" %% "okhttp-backend" % "1.5.8"
// or, for the monix version:
libraryDependencies += "com.softwaremill.sttp" %% "okhttp-backend-monix" % "1.5.8"
// https://mvnrepository.com/artifact/org.asynchttpclient/async-http-client
libraryDependencies += "org.asynchttpclient" % "async-http-client" % "2.7.0"
// https://mvnrepository.com/artifact/net.databinder.dispatch/dispatch-core
libraryDependencies += "net.databinder.dispatch" %% "dispatch-core" % "0.13.4"
// https://mvnrepository.com/artifact/com.typesafe.play/play-ahc-ws-standalone
libraryDependencies += "com.typesafe.play" %% "play-ahc-ws-standalone" % "2.0.1"
// https://mvnrepository.com/artifact/io.vertx/vertx-web-scala
libraryDependencies += "io.vertx" %% "vertx-web-scala" % "3.6.2"
// https://mvnrepository.com/artifact/com.typesafe.scala-logging/scala-logging
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"

libraryDependencies += "io.vertx" %% "vertx-lang-scala-stack" % "3.6.2" % "provided" pomOnly()
