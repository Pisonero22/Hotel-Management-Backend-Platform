package es.upsa.ssi.trabajo1.adapters.output;

import es.upsa.ssi.trabajo1.adapters.output.providers.GatewayResponseExceptionMapper;
import es.upsa.ssi.trabajo1.application.dtos.PersonaDto;
import es.upsa.ssi.trabajo1.domain.entities.Persona;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient(configKey = "rest.client.personas")
@RegisterProvider(GatewayResponseExceptionMapper.class)
@Path("/personas")
public interface PersonasServiceClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<Persona> findAllPersonas() throws AppException;

    @GET
    @Path("/{dni}")
    @Produces(MediaType.APPLICATION_JSON)
    Persona findPersonaByDni(@PathParam("dni")String dni) throws AppException;


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Persona createPersona(Persona persona) throws AppException;


    @PUT
    @Path("/{dni}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Persona updatePersona(@PathParam("dni")String dni, PersonaDto personaDto) throws AppException;


    @DELETE
    @Path("/{dni}")
    String deletePersona(@PathParam("dni")String dni) throws AppException;

}
