val Http4sVersion = "0.21.7"
val TapirVersion = "0.16.16"
val CirceVersion = "0.13.0"
val LogbackVersion = "1.2.3"
val PureConfVersion = "0.13.0"
lazy val root = (project in file("."))
  .settings(
    organization := "org.self",
    name := "tapirpoc",
    version := "0.0.2-SNAPSHOT",
    scalaVersion := "2.12.12",
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-core" % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-http4s" % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-sttp-client" % TapirVersion,
      "com.github.pureconfig" %% "pureconfig" % PureConfVersion,
      "com.github.pureconfig" %% "pureconfig-cats-effect" % PureConfVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "io.circe" %% "circe-literal" % CirceVersion,
      "ch.qos.logback" % "logback-classic" % LogbackVersion
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
  )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Ypartial-unification",
  "-Xfatal-warnings",
)

enablePlugins(JavaServerAppPackaging)
