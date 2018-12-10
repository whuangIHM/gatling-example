## Scenario

```scala
package computerdatabase // 1
import io.gatling.core.Predef._ // 2
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation { // 3

  val httpConf = http // 4
    .baseURL("http://computer-database.gatling.io") // 5
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // 6
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val scn = scenario("BasicSimulation") // 7
    .exec(http("request_1")  // 8
    .get("/")) // 9
    .pause(5) // 10

  setUp( // 11
    scn.inject(atOnceUsers(1)) // 12
  ).protocols(httpConf) // 13
}
```

1. The optional package.
2. The required imports.
3. The class declaration. Note that it extends Simulation.
4. The common configuration to all HTTP requests.
5. The baseURL that will be prepended to all relative urls.
6. Common HTTP headers that will be sent with all the requests.
7. The scenario definition.
8. A HTTP request, named request_1. This name will be displayed in the final reports.
9. The url this request targets with the GET method.
10. Some pause/think time.
11. Where one sets up the scenarios that will be launched in this Simulation.
12. Declaring to inject into scenario named scn one single user.
13. Attaching the HTTP configuration declared above.

## Simulation

```scala
setUp(
    scn.inject(
      nothingFor(4 seconds),          // 1
      atOnceUsers(10),                // 2
      rampUsers(10) over(5 seconds),  // 3
      constantUsersPerSec(20) during(15 seconds), // 4
      constantUsersPerSec(20) during(15 seconds) randomized, // 5
      rampUsersPerSec(10) to 20 during(10 minutes), // 6
      rampUsersPerSec(10) to 20 during(10 minutes) randomized, // 7
      splitUsers(1000) into(rampUsers(10) over(10 seconds)) separatedBy(10 seconds), // 8
      splitUsers(1000) into(rampUsers(10) over(10 seconds)) separatedBy atOnceUsers(30), // 9
      heavisideUsers(1000) over(20 seconds) // 10
      rampUsers(1000) over(20 minutes))).maxDuration(10 minutes) // 11
    ).protocols(httpConf)
  )
```

1. Pause for a given duration.
2. Injects a given number of users at once.
3. Injects a given number of users with a liner ramp over a given duration.
4. Injects users at a constant rate, defined in users per second, during a given duration. Users will be injected at regular intervals.
5. Injects users at a constant rate, defined in users per second, during a given duration. Users will be injected at randomized intervals.
6. Injects users from starting rate to target rate, defined in users per second, during a given duration, user will be injected at regular intervals.
7. Injects users from starting rate to target rate, defined in users per second, during a given duration. Users will be injected at randomized intervals.
8. Repeatedly execute the defined injection step separated by a pause of the given duration until reaching nbUsers, the total number of users to inject.
9. Repeatedly execute the first defined injection step (injectionStep1) separated by the execution of the second injection step (injectionStep2) until reaching nbUsers, the total number of users to inject.
10. Injects a given number of users following a smooth approximation of the heaviside step function stretched to a given duration.
11. the maximum duration of your simulation with the method maxDuration.

**Note: Throttling - This simulation will reach 100 req/s with a ramp of 10 seconds, then hold this throughput for 1 minute, jump to 50 req/s and finally hold this throughput for 2 hours.**
```scala
setUp(
    scn.inject(constantUsersPerSec(100) during(30 minutes)))
    .throttle(
        reachRps(100) in (10 seconds),
        holdFor(1 minute),
        jumpToRps(50),
        holdFor(2 hours)
    )
  )
```