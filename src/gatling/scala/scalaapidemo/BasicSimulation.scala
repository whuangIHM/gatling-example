package scalaapidemo

import io.gatling.http.Predef._
import io.gatling.core.Predef._

class BasicSimulation extends Simulation{

//  val httpConf = http.baseURL("https://api.linkedin.com/")
//
//  val scn = scenario("Basic Simulation")
//    .exec(http("Retrieve Current Member's Profile")
//      .get("/v2/me")) //resourceURI
//    .pause(5)
//
//  setUp(
//    scn.inject(atOnceUsers(1))
//  ).protocols(httpConf)

  val csvFeeder = csv("./src/test/resources/TestData.csv")

  val httpConf = http
    .baseURL("https://www.google.com")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val scn = scenario("BasicSimulation")
    .feed((csvFeeder).circular)
    .exec(http("Request: ${testCase}")
    .get("/"))
    .pause(5)

  setUp(
    scn.inject(atOnceUsers(5))
      .protocols(httpConf)
    )
}
