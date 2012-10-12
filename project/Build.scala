import sbt._
import Keys._

object EddgyBuild extends Build {

  lazy val project = Project(
    id = "eddgy",
    base = file("."),
    settings = eddgySettings
  )
    
  object V {
    // val specs2 = "1.9"
  }
  
  def eddgySettings = Defaults.defaultSettings ++ Seq (
    organization := "com.eddgy",
    name := "nlp",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "2.9.1",
    scalacOptions += "-unchecked",
    scalacOptions += "-deprecation",
    initialCommands in console := "import com.eddgy.nlp._;",

    libraryDependencies ++= Seq(
      "org.pingel" %% "axle" % "0.1-SNAPSHOT",
      "org.apache.opennlp" % "opennlp-tools" % "1.5.2-incubating",
      "org.clapper" % "argot_2.9.1" % "0.3.8",
      "net.sf.opencsv" % "opencsv" % "2.1",
      "com.codecommit" % "anti-xml_2.9.1" % "0.3",
      "com.codahale" % "jerkson_2.9.1" % "0.5.0",
      "org.scalanlp" % "breeze-learn_2.9.2" % "0.2-SNAPSHOT" changing(),
      "org.apache.commons" % "commons-lang3" % "3.0.1",
      "commons-logging" % "commons-logging" % "1.1.1",
      "log4j" % "log4j" % "1.2.16",
      "org.scalatest" %% "scalatest" % "1.6.1" % "test",
      "junit" % "junit" % "4.10" % "test",
      "org.scalaz" %% "scalaz-core" % "6.0.4",
      "com.novocode" % "junit-interface" % "0.6" % "test->default"
    ),
    
    resolvers ++= Seq(
      "Typesafe Maven Repository" at "http://repo.typesafe.com/typesafe/maven-ivy-releases",
      "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases",
      "Sonatype Public Repository" at "http://oss.sonatype.org/content/groups/public/",
      "Sonatype OSS" at "http://oss.sonatype.org/content/repositories/releases/",
      "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
      "Novus Snapshot Repository" at "http://repo.novus.com/snapshots/",
      "spray repo" at "http://repo.spray.cc/",
      // "Scales Repo" at "http://scala-scales.googlecode.com/svn/repo",
      "MVN Repo" at "http://mvnrepository.com/artifact/",
      "array.ca" at "http://www.array.ca/nest-web/maven/"
    ),

    publishTo <<= version { (v: String) =>
      val nexus = "http://nexus.eddgy.com/nexus/"
      if (v.trim.endsWith("SNAPSHOT")) 
        Some("nexus.eddgy.com snapshots" at nexus + "content/repositories/snapshots/") 
      else
        Some("nexus.eddgy.com releases"  at nexus + "content/repositories/releases/") 
    },

    credentials += Credentials(Path.userHome / ".sbt" / "nexus.eddgy.com.credentials")

  )

}
