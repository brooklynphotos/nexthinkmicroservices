package photos.brooklyn.interviews.nexthink.microservices.model

import photos.brooklyn.interviews.nexthink.microservices.MicroserviceReplicates

/**
 * describes the result of a deployment
 */
case class Deployment(isSuccessful: Boolean,startTime: Long,deploymentCompletionTime: Long,entryMicroservice: String,microservices: Map[String, MicroserviceReplicates])
