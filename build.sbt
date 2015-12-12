name := "scala_day"

version := "1.0"

scalaVersion := "2.11.7"


libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.5"

libraryDependencies += "org.apache.commons" % "commons-io" % "1.3.2"

libraryDependencies += "io.spray" % "spray-json_2.11" % "1.3.2"

libraryDependencies += "com.datastax.cassandra" % "cassandra-driver-core" % "2.1.6"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.5.0"

libraryDependencies += "com.datastax.spark" %% "spark-cassandra-connector-java" % "1.5.0-M1"

libraryDependencies += "nz.ac.waikato.cms.weka" % "weka-dev" % "3.7.13"