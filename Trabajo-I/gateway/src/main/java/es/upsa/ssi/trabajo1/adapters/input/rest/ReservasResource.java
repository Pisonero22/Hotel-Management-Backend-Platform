package es.upsa.ssi.trabajo1.adapters.input.rest;


import es.upsa.ssi.trabajo1.application.dtos.ErrorDto;
import es.upsa.ssi.trabajo1.application.dtos.ExceptionDto;
import es.upsa.ssi.trabajo1.application.dtos.ReservaDto;
import es.upsa.ssi.trabajo1.application.usecases.hoteles.FindHotelByIdUseCase;
import es.upsa.ssi.trabajo1.application.usecases.personas.FindPersonasByDniUseCase;
import es.upsa.ssi.trabajo1.application.usecases.reservas.*;
import es.upsa.ssi.trabajo1.domain.entities.Hotel;
import es.upsa.ssi.trabajo1.domain.entities.HotelReservasFull;
import es.upsa.ssi.trabajo1.domain.entities.Persona;
import es.upsa.ssi.trabajo1.domain.entities.Reserva;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.net.URI;
import java.util.Optional;

@Path("/reservas")
public class ReservasResource {

    @Inject
    UriInfo uriInfo;
    @Inject
    FindReservaByIdUseCase findReservaByIdUseCase;
    @Inject
    FindReservasByIdHotelUseCase findReservasByIdHotelUseCase;
    @Inject
    AddReservaUseCase addReservaUseCase;
    @Inject
    UpdateReservaUseCase updateReservaUseCase;
    @Inject
    DeleteReservaUseCase deleteReservaUseCase;
    @Inject
    FindHotelByIdUseCase findHotelByIdUseCase;
    @Inject
    FindPersonasByDniUseCase findPersonasByDniUseCase;


    @Operation(operationId = "getReservaById",
            summary = "Acceso a los datos de una reserva identificada por su id",
            description = "Devuelve los datos de la reserva identificada a través de su id"
    )
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Se ha localizado la reserva y se devuelven sus datos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.OBJECT,
                                    implementation = Reserva.class
                            )
                    )
            ),
            @APIResponse(responseCode = "404",
                    description = "No hay registrada una reserva con el id proporcionado"
            )
    })
    @GET
    @Path("/{idReserva}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findReservaById(@PathParam("idReserva") String idReserva) throws AppException {

        return findReservaByIdUseCase.execute(idReserva).map(
                reserva -> Response.ok()
                        .entity(reserva)
                        .build()
        ).orElse(
                Response.status(Response.Status.NOT_FOUND)
                        .entity(ErrorDto.builder()
                                .withMensaje("La reserva con el id -> " + idReserva + " no existe.")
                                .build()
                        )
                        .build()
        );

    }

    @Operation(operationId = "getReservasHotelByIdHotelConjuntas",
            summary = "Acceso a los datos de las reservas conjuntas que tiene un hotel mediante el id",
            description = "Devuelve los datos de todas reservas de un hotel proporcionando el id del hotel"
    )
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Se han localizado las reservas y se devuelve los datos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.OBJECT,
                                    implementation = Reserva.class
                            )
                    )
            ),
            @APIResponse(responseCode = "404",
                    description = "No existe el hotel con ese ID"
            ),
            @APIResponse(responseCode = "405",
                    description = "No hay registrada una reserva con el ID del hotel"
            )
    })
    @GET
    @Path("/hotel/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findReservasByHotelId(@PathParam("id") String id) throws AppException {


        Optional<HotelReservasFull> hotelReservasFull = Optional.of(findReservasByIdHotelUseCase.execute(id));
        if( hotelReservasFull.isEmpty()){
            return Response.status(404)
                    .entity(ErrorDto.builder()
                            .withMensaje("No hay ningun hotel con el id -> " + id)
                            .build()
                    )
                    .build();
        }else if ( hotelReservasFull.get().getReservas().isEmpty() ) {
            return Response.status( 405 )
                    .entity(ErrorDto.builder()
                            .withMensaje("No hay reservas en el hotel -> " + id)
                            .build()
                    )
                    .build();
        }

        return Response.ok()
                .entity(hotelReservasFull)
                .build();
    }



    @Operation(operationId = "postReserva",
            description = "Crea un reserva",
            summary = "Registra una nueva reserva en el sistema"
    )
    @APIResponses({
            @APIResponse(responseCode = "201",
                    description = "Se ha creado la reserva. Se devuelve sus datos entre los que se incluye su id",
                    headers = @Header(name = HttpHeaders.LOCATION,
                            description = "URI con la que acceder a la reserva creada",
                            schema = @Schema(type = SchemaType.STRING,
                                    format = "uri"
                            )
                    ),
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.OBJECT,
                                    implementation = Reserva.class
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
    public Response addReserva(ReservaDto reservaDto) throws AppException {
        Reserva reserva = Reserva.builder()
                .withDni(reservaDto.getDni())
                .withId_hotel(reservaDto.getId_hotel())
                .withDNITitular(reservaDto.getDNITitular())
                .withFechaEntrada(reservaDto.getFechaEntrada())
                .withFechaSalida(reservaDto.getFechaSalida())
                .withPrecioTotal(reservaDto.getPrecioTotal())
                .build();


        if(reservaDto.getDni() == null || reservaDto.getDni().isBlank()){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje("El DNI no puede estar vacio")
                            .build()
                    )
                    .build();
        }else if(reservaDto.getDNITitular() == null || reservaDto.getDNITitular().isBlank()){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje("El DNI del titular no puede estar vacio")
                            .build()
                    )
                    .build();
        }else if(reservaDto.getFechaEntrada() == null){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje("La fecha de entrada no puede estar vacia")
                            .build()
                    )
                    .build();
        }else if(reservaDto.getFechaSalida() == null){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje("La fecha de salida no puede estar vacia")
                            .build()
                    )
                    .build();
        }else if(reservaDto.getPrecioTotal() < 1){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje("El precio total no puede ser menor de 1")
                            .build()
                    )
                    .build();
        }else if (reservaDto.getFechaEntrada().isAfter(reservaDto.getFechaSalida())) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ExceptionDto.builder()
                            .withMessage("La fecha de entrada no puede ser posterior a la de la salida")
                            .build()
                    )
                    .build();
        }

        Optional<Hotel> hotel1 = findHotelByIdUseCase.execute(reservaDto.getId_hotel());
        if ( hotel1.isEmpty() ){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ErrorDto.builder()
                            .withMensaje("El hotel con ID " + reservaDto.getId_hotel() + " no existe")
                            .build()
                    )
                    .build();
        }
        Persona persona1 = findPersonasByDniUseCase.execute(reservaDto.getDni());
        if(persona1.dni()==null){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ErrorDto.builder()
                            .withMensaje("La persona con el DNI " + reservaDto.getDni() + " no existe")
                            .build()
                    )
                    .build();
        }
        Persona persona2 = findPersonasByDniUseCase.execute(reservaDto.getDNITitular());

            if(persona2.dni()==null){
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ErrorDto.builder()
                                .withMensaje("La persona con el DNI " + reservaDto.getDNITitular() + " no existe")
                                .build()
                        )
                        .build();
            }
        Reserva newReserva = addReservaUseCase.execute(reserva);
        if(newReserva.id()==null){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje(newReserva.DNITitular())
                            .build()
                    )
                    .build();
        }
        URI uri = uriInfo.getAbsolutePathBuilder()
                .path("/{id}")
                .resolveTemplate("id", newReserva.id())
                .build();

        return Response.created(uri)
                .entity(newReserva)
                .build();

    }




    @Operation(operationId = "putReserva",
            description = "Modifica los datos de una reserva",
            summary = "Modifica los datos de la reserva identificada por su id"
    )
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Se ha modificado los datos de la reserva",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.OBJECT,
                                    implementation = Reserva.class
                            )
                    )
            ),
            @APIResponse(responseCode = "404",
                    description = "No existe la reserva identificada por el ID indicado"
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
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateReserva(@PathParam("id")String id, ReservaDto reservaDto) throws AppException {
        Reserva reserva = Reserva.builder()
                .withId(id)
                .withDni(reservaDto.getDni())
                .withId_hotel(reservaDto.getId_hotel())
                .withDNITitular(reservaDto.getDNITitular())
                .withFechaEntrada(reservaDto.getFechaEntrada())
                .withFechaSalida(reservaDto.getFechaSalida())
                .withPrecioTotal(reservaDto.getPrecioTotal())
                .build();

        Optional<Reserva> reserva1 = findReservaByIdUseCase.execute(id);

        if(reserva1.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ErrorDto.builder()
                            .withMensaje("La reserva con el ID -> " + id + " no existe")
                            .build()
                    )
                    .build();
        }

        if(reservaDto.getDni() == null || reservaDto.getDni().isBlank()){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje("El DNI no puede estar vacio")
                            .build()
                    )
                    .build();
        }else if(reservaDto.getDNITitular() == null || reservaDto.getDNITitular().isBlank()){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje("El DNI del titular no puede estar vacio")
                            .build()
                    )
                    .build();
        }else if(reservaDto.getFechaEntrada() == null){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje("La fecha de entrada no puede estar vacia")
                            .build()
                    )
                    .build();
        }else if(reservaDto.getFechaSalida() == null ){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje("La fecha de salida no puede estar vacia")
                            .build()
                    )
                    .build();
        }else if(reservaDto.getPrecioTotal() < 1){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje("El precio total no puede ser menor de 1")
                            .build()
                    )
                    .build();
        }

        Optional<Hotel> hotel1 = findHotelByIdUseCase.execute(reservaDto.getId_hotel());

        if ( hotel1.isEmpty() ){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ErrorDto.builder()
                            .withMensaje("El hotel con ID " + reservaDto.getId_hotel() + " no existe")
                            .build()
                    )
                    .build();
        }
        Persona persona1 = findPersonasByDniUseCase.execute(reservaDto.getDni());

        if(persona1.dni()==null){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ErrorDto.builder()
                            .withMensaje("La persona con el DNI " + reservaDto.getDni() + " no existe")
                            .build()
                    )
                    .build();
        }
        Persona persona2 = findPersonasByDniUseCase.execute(reservaDto.getDNITitular());

        if(persona2.dni()==null){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ErrorDto.builder()
                            .withMensaje("La persona con el DNI " + reservaDto.getDNITitular() + " no existe")
                            .build()
                    )
                    .build();
        }


        Reserva updatedReserva = updateReservaUseCase.execute(reserva);

        if(updatedReserva.id()==null){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje(updatedReserva.DNITitular())
                            .build()
                    )
                    .build();
        }

        return Response.ok()
                .entity(updatedReserva)
                .build();
    }


    @Operation(operationId = "deleteReserva",
            description = "Elimina una reserva",
            summary = "Elimina la reserva identificada por su id"
    )
    @APIResponses({
            @APIResponse(responseCode = "204",
                    description = "Se ha eliminado la reserva"
            ),
            @APIResponse(responseCode="404",
                    description="No existe la reserva identificada por ese id"
            )
    })
    @DELETE
    @Path("{id}")
    public Response deleteReserva(@PathParam("id")String id) throws AppException {
        String borrado = deleteReservaUseCase.execute(id);

        if(borrado!=null){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje(borrado)
                            .build()
                    )
                    .build();
        }
        return Response.noContent()
                .build();

    }


}
