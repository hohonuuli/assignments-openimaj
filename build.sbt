// PROJECT PROPERTIES ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
organization in ThisBuild := "org.mbari"

name := "assignments-openimaj"

version in ThisBuild := "1.0-SNAPSHOT"

scalaVersion in ThisBuild := "2.11.4"

scalacOptions in ThisBuild += "-deprecation"

javacOptions in ThisBuild ++= Seq("-target", "1.8", "-source","1.8")

// DEPENDENCIES ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
updateOptions := updateOptions.value.withCachedResolution(true) 

// Add openimaj and breeze
libraryDependencies ++= {
  val openimajVersion = "1.3.1"
  val breezeVersion = "0.10"
  val twelveMonkeysVersion = "3.0.2"
  val scilubeVersion = "2.0-SNAPSHOT"
  Seq(
    "com.twelvemonkeys.common" % "common-lang" % twelveMonkeysVersion,
    "com.twelvemonkeys.common" % "common-io" % twelveMonkeysVersion,
    "com.twelvemonkeys.common" % "common-image" % twelveMonkeysVersion,
    "com.twelvemonkeys.imageio" % "imageio-core" % twelveMonkeysVersion,
    "com.twelvemonkeys.imageio" % "imageio-metadata" % twelveMonkeysVersion,
    "com.twelvemonkeys.imageio" % "imageio-jpeg" % twelveMonkeysVersion,
    "com.twelvemonkeys.imageio" % "imageio-tiff" % twelveMonkeysVersion,
    "org.mbari" % "mbarix4j" % "1.9.3-SNAPSHOT",
    "org.openimaj" % "image-annotation" % openimajVersion exclude("log4j", "log4j"),
    "org.openimaj" % "image-feature-extraction" % openimajVersion exclude("log4j", "log4j"),
    "org.openimaj" % "image-local-features" % openimajVersion exclude("log4j", "log4j"),
    "org.openimaj" % "image-processing" % openimajVersion exclude("log4j", "log4j"),
    "org.openimaj" % "object-detection" % openimajVersion exclude("log4j", "log4j"),
    "org.openimaj" % "xuggle-video" % openimajVersion exclude("log4j", "log4j"),
    "org.scalanlp" %% "breeze" % breezeVersion,
    "org.scalanlp" %% "breeze-natives" % breezeVersion,
    "scilube" %% "scilube-core" % scilubeVersion)
}

// Add Testing libs
libraryDependencies ++= Seq(
    "junit" % "junit" % "4.11" % "test",
    "org.scalatest" %% "scalatest" % "2.2.2" % "test"
)

// Add SLF4J and Logback libs
libraryDependencies ++= {
  val slf4jVersion = "1.7.7"
  val logbackVersion = "1.1.2"
  Seq(
    "org.slf4j" % "slf4j-api" % slf4jVersion,
    "org.slf4j" % "log4j-over-slf4j" % slf4jVersion,
    "ch.qos.logback" % "logback-classic" % logbackVersion,
    "ch.qos.logback" % "logback-core" % logbackVersion)
}

resolvers += Resolver.mavenLocal

publishMavenStyle := true

publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))

// OTHER SETTINGS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Adds commands for dependency reporting
net.virtualvoid.sbt.graph.Plugin.graphSettings

// set the prompt (for this build) to include the project id.
shellPrompt in ThisBuild := { state => 
  val user = System.getProperty("user.name")
  "\n" + user + "@" + Project.extract(state).currentRef.project + "\nsbt> " 
}

// Add this setting to your project to generate a version report (See ExtendedBuild.scala too.)
// Use as 'sbt versionReport' or 'sbt version-report'
versionReport <<= (externalDependencyClasspath in Compile, streams) map {
  (cp: Seq[Attributed[File]], streams) =>
    val report = cp.map {
      attributed =>
        attributed.get(Keys.moduleID.key) match {
          case Some(moduleId) => "%40s %20s %10s %10s".format(
            moduleId.organization,
            moduleId.name,
            moduleId.revision,
            moduleId.configurations.getOrElse("")
          )
          case None =>
            // unmanaged JAR, just
            attributed.data.getAbsolutePath
        }
    }.sortBy(a => a.trim.split("\\s+").map(_.toUpperCase).take(2).last).mkString("\n")
    streams.log.info(report)
    report
}

// For sbt-pack
packAutoSettings

// fork a new JVM for run and test:run
fork := true

// Aliases
addCommandAlias("cleanall", ";clean;clean-files")

initialCommands in console :=
  """
    |import scilube.Matlib
    |import org.mbari.breeze._
    |import breeze.linalg._
  """.stripMargin

