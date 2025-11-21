package es.upsa.ssi.trabajo1.personas.adapters.input.rest;

import es.upsa.ssi.trabajo1.domain.entities.Persona;
import es.upsa.ssi.trabajo1.personas.adapters.input.rest.dtos.ExceptionDto;
import es.upsa.ssi.trabajo1.personas.adapters.input.rest.dtos.PersonaDto;
import es.upsa.ssi.trabajo1.personas.application.*;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;

@Tag(name = "personas")
@Path("/personas")
@RequestScoped
public class PersonasResource {

    @Context
    UriInfo uriInfo;

    @Inject
    FindAllPersonasUseCase findAllPersona;
    @Inject
    FindPersonaByDNIUseCase findPersonaByDNIUseCase;
    @Inject
    AddPersonaUseCase addPersonaUseCase;
    @Inject
    UpdatePersonaUseCase updatePersonaUseCase;
    @Inject
    DeletePersonaByDniUseCase deletePersonaByDniUseCase;



    @Operation(operationId = "getPersonas",
            summary = "Acceso a los datos de todas las personas registradas",
            description = "Devuelve los datos de todas las personas registradas en el sistema"
    )
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Se devuelve los datos de las personas",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.ARRAY,
                                    implementation = Persona.class
                            )
                    )
            ),
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAllPersona() throws AppException{
        return Response.ok()
                .entity(findAllPersona.execute())
                .build();
    }


    @Operation(operationId = "getPersonaById",
            summary = "Acceso a los datos de una persona identificada por su DNI",
            description = "Devuelve los datos de la persona identificada a través de su DNI"
    )
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Se ha localizado la persona y se devuelven sus datos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.OBJECT,
                                    implementation = Persona.class
                            )
                    )
            ),
            @APIResponse(responseCode = "404",
                    description = "No hay registrada una persona con el DNI proporcionado"
            )
    })
    @GET
    @Path("/{dni}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPersonaByDni(@PathParam("dni")String dni) throws AppException{
        Persona persona1 = findPersonaByDNIUseCase.execute(dni);
        if(persona1.dni() == null){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ExceptionDto.builder()
                            .withMessage("La persona con el DNI -> " + dni + " no existe")
                            .build()
                    )
                    .build();
        }
         return Response.ok(persona1)
                 .build();

    }


    @Operation(operationId = "postPersona",
            description = "Crea un persona",
            summary = "Registra una nueva persona en el sistema"
    )
    @APIResponses({
            @APIResponse(responseCode = "201",
                    description = "Se ha creado la persona. Se devuelve sus datos entre los que se incluye su DNI",
                    headers = @Header(name = HttpHeaders.LOCATION,
                            description = "URI con la que acceder a la persona creada",
                            schema = @Schema(type = SchemaType.STRING,
                                    format = "uri"
                            )
                    ),
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.OBJECT,
                                    implementation = Persona.class
                            )
                    )
            ),

            @APIResponse(responseCode = "500",
                    description = "Se ha producido un error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.OBJECT,
                                    implementation = ExceptionDto.class)
                    )
            )
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPersona(Persona persona) throws AppException{

        if(persona.dni() == null || persona.dni().isBlank()){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ExceptionDto.builder()
                            .withMessage("El DNI no puede estar vacio")
                            .build()
                    )
                    .build();
        }else if(persona.nombre() == null || persona.nombre().isBlank()){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ExceptionDto.builder()
                            .withMessage("El nombre no puede estar vacio")
                            .build()
                    )
                    .build();
        }else if(persona.telefono() == null || persona.telefono().isBlank()){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ExceptionDto.builder()
                            .withMessage("El telefono no puede estar vacio")
                            .build()
                    )
                    .build();
        }else if(persona.fechaNacimiento() == null){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ExceptionDto.builder()
                            .withMessage("La fecha de nacimiento no puede estar vacia")
                            .build()
                    )
                    .build();
        }else if(persona.fechaNacimiento().isAfter(LocalDate.now())){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ExceptionDto.builder()
                            .withMessage("La fecha de nacimiento no puede ser mayor a la actual")
                            .build())
                    .build();
        }
        Persona newPersona = addPersonaUseCase.execute(persona);

        if(newPersona.dni()==null){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ExceptionDto.builder()
                            .withMessage("No puede haber dos personas con el mismo dni"))
                    .build();
        }
        URI uriPersona = uriInfo.getAbsolutePathBuilder()
                .path("/{dni}")
                .resolveTemplate("dni", newPersona.dni())
                .build();
        return Response.created(uriPersona)
                .entity(newPersona)
                .build();
    }


    @Operation(operationId = "putPersona",
            description = "Modifica los datos de una persona",
            summary = "Modifica los datos de la persona identificada por su DNI"
    )
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Se ha modificado los datos de la persona",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.OBJECT,
                                    implementation = Persona.class
                            )
                    )
            ),

            @APIResponse(responseCode = "404",
                    description = "No existe la persona identificada por el DNI indicado"
            ),

            @APIResponse(responseCode = "500",
                    description = "Se ha producido un error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.OBJECT,
                                    implementation = ExceptionDto.class
                            )
                    )
            )
    })
    @PUT
    @Path("/{dni}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePersona(@PathParam("dni")String dni, PersonaDto personaDto) throws AppException{
        Persona buildPersona = Persona.builder()
                .withDni(dni)
                .withNombre(personaDto.getNombre())
                .withTelefono(personaDto.getTelefono())
                .withFechaNacimiento(personaDto.getFechaNacimiento())
                .build();

        Persona persona = findPersonaByDNIUseCase.execute(dni);

        if(persona.dni()==null){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ExceptionDto.builder()
                            .withMessage("No existe la persona con el DNI -> " + dni)
                            .build()
                    )
                    .build();
        }
        if(personaDto.getNombre() == null || personaDto.getNombre().isBlank()){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ExceptionDto.builder()
                            .withMessage("El nombre no puede estar vacio")
                            .build()
                    )
                    .build();
        }else if(personaDto.getTelefono() == null || personaDto.getTelefono().isBlank()){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ExceptionDto.builder()
                            .withMessage("El telefono no puede estar vacio")
                            .build()
                    )
                    .build();
        }else if(personaDto.getFechaNacimiento() == null){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ExceptionDto.builder()
                            .withMessage("La fecha de nacimiento no puede estar vacia")
                            .build()
                    )
                    .build();
        }else if(personaDto.getFechaNacimiento().isAfter(LocalDate.now()) ){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ExceptionDto.builder()
                            .withMessage("La fecha de nacimiento no puede ser mayor a la actual")
                            .build()
                    )
                    .build();
        }

        Persona personaUpdated = updatePersonaUseCase.execute(buildPersona);

        return Response.ok()
                .entity(personaUpdated)
                .build();
    }


    @Operation(operationId = "deletePersona",
            description = "Elimina una persona",
            summary = "Elimina la persona identificada por su DNI"
    )
    @APIResponses({
            @APIResponse(responseCode = "204",
                    description = "Se ha eliminado la persona"
            ),

            @APIResponse(responseCode="404",
                    description="No existe la persona identificada por ese DNI"
            ),
            @APIResponse(responseCode="500",
                    description = "Se ha producido un error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.OBJECT,
                                    implementation = ExceptionDto.class
                            )
                    )
            )
    })
    @DELETE
    @Path("/{dni}")
    public Response deletePersona(@PathParam("dni")String dni) throws AppException{
        Persona execute = findPersonaByDNIUseCase.execute(dni);
        if(execute.dni()==null){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ExceptionDto.builder()
                            .withMessage("La persona con el dni -> " + dni + " no existe")
                            .build()
                    )
                    .build();
        }

        boolean borrado = deletePersonaByDniUseCase.execute(dni);

        if(!borrado){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ExceptionDto.builder()
                            .withMessage("La persona con el dni -> " + dni + " tiene reserva pendientes")
                            .build()
                    )
                    .build();
        }

        return Response.status(Response.Status.NO_CONTENT)
                .build();

    }

}
