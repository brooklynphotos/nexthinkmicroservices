# Microservices Exercise for Nexthink Interview
## How to Run
### Packaging for run
After running `sbt stage` there should be executable scripts for both the main and the REST service under `target/universal/scripts/bin/`
### For a basic sanity check, run the `Main`
`photos.brooklyn.interviews.nexthink.microservices.Main deploy input-sample.json`
`photos.brooklyn.interviews.nexthink.microservices.Main cyclic input-sample.json`
`photos.brooklyn.interviews.nexthink.microservices.Main health input-sample.json`
## General Comments
### Choice of HTTP Service
Akka HTTP is used for these reasons
* Its DSL makes it easier to read for me
* Follows the principle of reactive programming, which is useful in this case since spawning microservices can take time and we don't want to block requests
* Easy to test
* Works well with Actors
### Time spent
About 15 hours, including all testing, documentation, setup, and coding
## Front End
The front end project can be found here: https://github.com/brooklynphotos/nexthinkmicroservicesfe/tree/feature/exercise
## Limitations and Future Improvements
* Does not allow you to list the deployments and check on the health of a given one
* Did not do stress testing to see if the DFS really runs on linear time
* Should use something other than a NoOp microservice deployer
* Should use `Future` instead of just a synchronous `Try`
## Docker
Docker runs very slowly on my laptop, to the point of halting all programs. But it shouldn't be difficult to create a Docker image using `sbt  docker:stage`, as detailed here:
https://www.freecodecamp.org/news/how-to-dockerise-a-scala-and-akka-http-application-the-easy-way-23310fc880fa/

Instead of Docker compose, I would consider just creating a kubernetes cluster with a pod containing a container running this Docker image and another container running the front end. 