package es.upsa.ssi.trabajo1.adapters.input.rest;

import es.upsa.ssi.trabajo1.application.dtos.ErrorDto;
import es.upsa.ssi.trabajo1.application.dtos.ExceptionDto;
import es.upsa.ssi.trabajo1.application.dtos.PersonaDto;
import es.upsa.ssi.trabajo1.application.usecases.personas.*;
import es.upsa.ssi.trabajo1.domain.entities.Persona;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.time.LocalDate;
import java.util.List;

@Path("/personas")
public class PersonasResource {

    @Inject
    FindPersonasByDniUseCase findPersonasByDniUseCase;
    @Inject
    UpdatePersonaByDniUseCase updatePersonaByDniUseCase;
    @Inject
    FindAllPersonasUseCase findAllPersonasUseCase;
    @Inject
    AddPersonaUseCase addPersonaUseCase;
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
            @APIResponse(responseCode = "404",
                    description = "No hay ninguna persona registrada"
            )
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAllPersonas() throws AppException{

        List<Persona> listaPersonas = findAllPersonasUseCase.execute();
        return Response.ok()
                .entity(listaPersonas)
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
    @Path("/{dniPersona}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPersonaByDni(@PathParam("dniPersona") String dni)throws AppException {

        Persona persona = findPersonasByDniUseCase.execute(dni);

        if(persona.dni()==null){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ErrorDto.builder()
                            .withMensaje("La persona con el DNI -> " + dni + " no existe")
                            .build()
                    )
                    .build();
        }

        return Response.ok(persona)
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
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPersona(Persona persona) throws AppException {
        if(persona.dni() == null || persona.dni().isBlank()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje("El DNI no puede estar vacio")
                            .build()
                    )
                    .build();
        }else if(persona.nombre() == null || persona.nombre().isBlank()){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje("El nombre no puede estar vacio")
                            .build()
                    )
                    .build();
        }else if(persona.telefono() == null || persona.telefono().isBlank()){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje("El telefono no puede estar vacio")
                            .build()
                    ).build();
        }else if(persona.fechaNacimiento() == null){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje("La fecha de nacimiento no puede estar vacia")
                            .build()
                    )
                    .build();
        }else if(persona.fechaNacimiento().isAfter(LocalDate.now())){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje("La fecha de nacimiento no puede ser mayor a la actual")
                            .build()
                    )
                    .build();
        }
        Persona persona1 = findPersonasByDniUseCase.execute(persona.dni());
        if(persona1.dni()!=null){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ErrorDto.builder()
                            .withMensaje("No puedo haber dos personas con el mismo DNI")
                            .build()
                    )
                    .build();
        }
        Persona newPersona = addPersonaUseCase.execute(persona);
        if(newPersona.dni()==null && newPersona.nombre()!=null){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje(newPersona.nombre())
                            .build()
                    )
                    .build();
        }
        return Response.status(Response.Status.CREATED)
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
    @Path("/{dniPersona}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePersonaByDni(@PathParam("dniPersona") String dni, PersonaDto personaDto)throws AppException {
        Persona persona = Persona.builder()
                .withDni(dni)
                .withNombre(personaDto.getNombre())
                .withTelefono(personaDto.getTelefono())
                .withFechaNacimiento(personaDto.getFechaNacimiento())
                .build();

        Persona persona1 = findPersonasByDniUseCase.execute(dni);

        if(persona1.dni()==null){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ErrorDto.builder()
                            .withMensaje("La persona con el DNI " + dni + " no existe")
                            .build()
                    )
                    .build();
        }

        if(personaDto.getNombre() == null || personaDto.getNombre().isBlank()){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje("El nombre no puede estar vacio")
                            .build()
                    )
                    .build();
        }else if(personaDto.getTelefono() == null || personaDto.getTelefono().isBlank()){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje("El telefono no puede estar vacio")
                            .build()
                    )
                    .build();
        }else if(personaDto.getFechaNacimiento() == null){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje("La fecha de nacimiento no puede estar vacia")
                            .build()
                    )
                    .build();
        }else if(persona.fechaNacimiento().isAfter(LocalDate.now())){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje("La fecha de nacimiento no puede ser mayor a la actual")
                            .build()
                    )
                    .build();
        }

        Persona updatedPersona = updatePersonaByDniUseCase.execute(persona);

        return Response.ok()
                .entity(updatedPersona)
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
    public Response deletePersonaByDni(@PathParam("dni") String dni) throws AppException {

        Persona persona = findPersonasByDniUseCase.execute(dni);

        if(persona.dni()==null){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ErrorDto.builder()
                            .withMensaje("La persona con el DNI " + dni + " no existe")
                            .build()
                    )
                    .build();
        }


        String borrado = deletePersonaByDniUseCase.execute(dni);

        if(borrado!=null){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje(borrado)
                            .build()
                    )
                    .build();

        }
        return Response.status(Response.Status.NO_CONTENT)
                .build();
    }


}
