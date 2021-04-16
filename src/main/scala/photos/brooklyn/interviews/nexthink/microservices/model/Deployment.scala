package photos.brooklyn.interviews.nexthink.microservices.model

import photos.brooklyn.interviews.nexthink.microservices.MicroserviceReplicas

/**
 * describes the result of a deployment
 */
case class Deployment(isSuccessful: Boolean, startTime: Long, entryMicroservice: String, microservices: Map[String, MicroserviceReplicas], deploymentCompletionTime: Long = System.currentTimeMillis(), error: Option[Exception] = None)
