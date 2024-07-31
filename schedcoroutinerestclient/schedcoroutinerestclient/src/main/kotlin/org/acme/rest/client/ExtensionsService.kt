package org.acme.rest.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import java.util.Set;

@Path("/extensions")
@ApplicationScoped
@RegisterRestClient(configKey="extensions-api")
interface ExtensionsService {

    @GET
    fun getById(@QueryParam("id") id: String) : Set<Extension>
}
