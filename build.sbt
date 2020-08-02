name := "play_quill_attemp1"

version := "1.0"

lazy val `play_quill_attemp1` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(jdbc, ehcache, ws, specs2 % Test, guice)

libraryDependencies += "org.postgresql" % "postgresql" % "42.2.14"

libraryDependencies += "io.getquill" %% "quill-async-postgres" % "3.5.2"

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

      