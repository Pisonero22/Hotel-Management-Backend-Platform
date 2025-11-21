package es.upsa.ssi.trabajo1.reserva.adapters.input.rest;


import es.upsa.ssi.trabajo1.domain.entities.Reserva;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import es.upsa.ssi.trabajo1.reserva.adapters.input.rest.dtos.ExceptionDto;
import es.upsa.ssi.trabajo1.reserva.application.usecases.FindReservaByDniUseCase;
import es.upsa.ssi.trabajo1.reserva.application.usecases.FindReservasByDniTitularUseCase;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Tag(name = "reservas")
@Path("/personas")
@RequestScoped
public class ReservasPersonaResource {

    @Context
    UriInfo uriInfo;
    @Inject
    FindReservaByDniUseCase findReservaByDniUseCase;
    @Inject
    FindReservasByDniTitularUseCase findReservasByDniTitularUseCase;



    @Operation(operationId = "getReservaByDni",
            summary = "Acceso a los datos de las reservas identificadas por el DNI de una persona",
            description = "Devuelve los datos de las reservas identificadas a través del DNI de una persona"
    )
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Se ha nlocalizado la reservas y se devuelven sus datos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.OBJECT,
                                    implementation = Reserva.class
                            )
                    )
            ),
            @APIResponse(responseCode = "404",
                    description = "No hay registrada una reserva con el DNI proporcionado"
            )
    })
    @GET
    @Path("/{dni}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findReservaByDni(@PathParam("dni") String dni) throws AppException {

        List<Reserva> reservasPersona = findReservaByDniUseCase.execute(dni);

        if( reservasPersona.isEmpty() ) return Response.status(Response.Status.NOT_FOUND)
                                                        .entity(
                                                                ExceptionDto.builder()
                                                                        .withMessage("No hay reserva con el DNI -> " + dni)
                                                                        .build())
                                                        .build();

        return Response.ok()
                .entity(findReservaByDniUseCase.execute(dni))
                .build();


    }



    @Operation(operationId = "getReservaByDniTitular",
            summary = "Acceso a los datos de las reservas identificadas por el DNI del titular",
            description = "Devuelve los datos de las reservas identificadas a través del DNI del titular"
    )
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Se han localizado las reservas y se devuelven sus datos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.OBJECT,
                                    implementation = Reserva.class
                            )
                    )
            ),
            @APIResponse(responseCode = "404",
                    description = "No hay registrada una reserva con el DNI del titular proporcionado"
            )
    })
    @GET
    @Path("/{dniTitular}/titular")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findReservasByDniTitular(@PathParam("dniTitular") String dniTitular) throws AppException {
        List<Reserva> reservasPersona = findReservasByDniTitularUseCase.execute(dniTitular);

        if( reservasPersona.isEmpty() ) return Response.status(Response.Status.NOT_FOUND)
                .entity(
                        ExceptionDto.builder()
                                .withMessage("No hay reserva con el DNI del titular -> " + dniTitular)
                                .build())
                .build();

        return Response.ok()
                .entity(findReservasByDniTitularUseCase.execute(dniTitular))
                .build();

    }


}
