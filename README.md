# Microservices Exercise for Nexthink Interview
## How to Run
### For a basic sanity check, run the `Main`
photos.brooklyn.interviews.nexthink.microservices.Main input-sample.json
## General Comments
### Choice of HTTP Service
Akka HTTP is used for these reasons
* Its DSL makes it easier to read for me
* Follows the principle of reactive programming, which is useful in this case since spawning microservices can take time and we don't want to block requests
* Easy to test
* Works well with Actors
### Time spent
#### Backend
About 10 hours, including all testing, documentation, setup, and coding
#### Front End
About 4 hours, not including relearning basics of ReactJS
### Limitations
### Future Ideas
### Critiques