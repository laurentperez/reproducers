import io.quarkus.scheduler.Scheduled
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@ApplicationScoped
class JobService {

    @Inject
    lateinit var service: Service

    @Scheduled(cron = "0 0 * * * ? *")
    fun schedule() {
        val ids = (1 .. 128).toList()

        // when NOT using suspend on parent method
        // using runBlocking(Dispatchers.Unconfined) all run on same vert.x-worker-thread-XX
        // using runBlocking(Dispatchers.Default) only one DefaultDispatcher-worker is used, only one "in thread" print stmt is shown, doSomething() is never called
        // using runBlocking(Dispatchers.IO) only one DefaultDispatcher-worker is used, only one "in thread" print stmt is shown, doSomething() is never called
        // withContext can't be called if 'suspend' is not used on parent method

        // when using suspend on parent method
        // using withcontext(Dispatchers.Unconfined) all run on same vert.x-eventloop-thread-XXX and we get an expected warning "has been blocked for 3478 ms, time limit is 2000 ms: io.vertx.core.VertxException: Thread blocked"
        // using withcontext(Dispatchers.Default) all run on different DefaultDispatcher-worker-XXX threads, doSomething() is never called
        // using withcontext(Dispatchers.IO) only one DefaultDispatcher-worker is used, only one "in thread" print stmt is shown, doSomething() is never called

        // using runBlocking(Dispatchers.Unconfined) all run on same vert.x-eventloop-thread-XXX and we get has been blocked for 3478 ms, time limit is 2000 ms: io.vertx.core.VertxException: Thread blocked
        // using runBlocking(Dispatchers.Default) all run on different DefaultDispatcher-worker-XXX threads, doSomething() is never called
        // using runBlocking(Dispatchers.IO) all run on different DefaultDispatcher-worker-XXX threads, doSomething() is never called

        runBlocking {
            ids.map { id ->
                async {
                    println("in thread ${Thread.currentThread().name}#${Thread.currentThread().threadId()}")
                    service.doSomething(id.toString())
                }
            }.awaitAll()
        }
    }


}
