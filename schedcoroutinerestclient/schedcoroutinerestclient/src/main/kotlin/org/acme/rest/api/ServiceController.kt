package org.acme.rest.api

import Service
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

@ApplicationScoped
@Path("/service")
class ServiceController {

    @Inject
    lateinit var service: Service

    @GET
    fun notScheduled() {
        val ids = (1 .. 128).toList()

        // This method is not scheduled, so it will not be executed by the Quarkus scheduler
        runBlocking(Dispatchers.Default) {
            ids.map { id ->
                async {
                    println("(API) in thread ${Thread.currentThread().name}#${Thread.currentThread().threadId()}")
                    service.doSomething(id.toString())
                }
            }.awaitAll()
        }

    }

}
