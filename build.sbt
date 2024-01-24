val akkaVersion = "2.5.21"
val akkaHttpVersion = "10.1.7"
val scalaTestVersion = "3.0.5"

lazy val send_db = project
  .in(file("send_db"))
  .enablePlugins(
    JavaAppPackaging,
    DockerPlugin
  )
  .settings(
    resolvers += "Akka library repository".at("https://repo.akka.io/maven"),
    name := "send-data-to-db",
    version := "send-data-to-db",
    scalaVersion := "2.12.8",
    Compile / mainClass := Some("send_db.SendDataToDBApp"),
    Docker / packageName := "traffic-violations",
//    Docker / dockerBaseImage := "openjdk",
    Docker / dockerExposedPorts ++= Seq(2552),
//    Docker / dockerRepository :=Some("traffic-violations"),
    Docker / dockerUsername :=Some("kmilev79"),
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "com.typesafe.akka" %% "akka-remote" % akkaVersion,
      // akka streams
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      // testing
      "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
      "org.scalatest" %% "scalatest" % scalaTestVersion,
      "io.aeron" % "aeron-driver" % "1.42.1",
      "io.aeron" % "aeron-client" % "1.42.1"
    )
  )

lazy val traffic_violations = project
  .in(file("traffic_violations"))
  .enablePlugins(
    JavaAppPackaging,
    DockerPlugin
  )
  .settings(
    resolvers += "Akka library repository".at("https://repo.akka.io/maven"),
    name := "traffic-violations",
    version := "api",
    scalaVersion := "2.12.8",
    Compile / mainClass := Some("traffic_violations.TrafficViolations"),
    Docker / packageName := "traffic-violations",
  //  Docker / dockerBaseImage := "openjdk",
    Docker / dockerExposedPorts ++= Seq(2551, 8080),
//    Docker / dockerRepository :=Some("traffic-violations"),
    Docker / dockerUsername :=Some("kmilev79"),
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "com.typesafe.akka" %% "akka-remote" % akkaVersion,
      // akka streams
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      // akka http
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion,
      // testing
      "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
      "org.scalatest" %% "scalatest" % scalaTestVersion,
      // JWT
      "com.pauldijou" %% "jwt-spray-json" % "2.1.0",
      "io.aeron" % "aeron-driver" % "1.42.1",
      "io.aeron" % "aeron-client" % "1.42.1"
    )
  )

lazy val send_external_system = project
  .in(file("send_external_system"))
  .enablePlugins(
    JavaAppPackaging,
    DockerPlugin
  )
  .settings(
    resolvers += "Akka library repository".at("https://repo.akka.io/maven"),
    name := "external-system",
    version := "external-system",
    scalaVersion := "2.12.8",
    Compile / mainClass := Some("send_external_system.SendNotificationToExternalSystemApp"),
    Docker / packageName := "traffic-violations",
    // Docker / dockerBaseImage := "openjdk",
    Docker / dockerExposedPorts ++= Seq(2553),
//    Docker / dockerRepository :=Some("traffic-violations"),
    Docker / dockerUsername :=Some("kmilev79"),
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "com.typesafe.akka" %% "akka-remote" % akkaVersion,
      // akka streams
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      // testing
      "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
      "org.scalatest" %% "scalatest" % scalaTestVersion,
      "io.aeron" % "aeron-driver" % "1.42.1",
      "io.aeron" % "aeron-client" % "1.42.1"
    )
  )
