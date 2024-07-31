import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.acme.rest.client.ExtensionsService
import org.eclipse.microprofile.rest.client.inject.RestClient

@ApplicationScoped
class Service {

    @RestClient
    @Inject
    lateinit var extensionsService: ExtensionsService

    fun doSomething(id: String) {
        val id = extensionsService.getById(id)
        println("Doing something with $extensionsService : $id")
    }

}
