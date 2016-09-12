
name := "vrepapiscala"

version := "0.2.1"

scalaVersion := "2.11.8"

organization := "com.github.troxid"

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { x => false }

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomExtra := (
  <url>https://github.com/Troxid/vrep-api-scala</url>
    <licenses>
      <license>
        <name>MIT</name>
        <url>http://opensource.org/licenses/MIT</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:troxid/vrep-api-scala.git</url>
      <connection>scm:git:git@github.com:troxid/vrep-api-scala.git</connection>
    </scm>
    <developers>
      <developer>
        <id>troxid</id>
        <name>trox</name>
        <url>https://github.com/troxid</url>
      </developer>
    </developers>
)

