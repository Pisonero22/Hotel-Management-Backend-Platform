package es.upsa.ssi.trabajo1.reserva.adapters.input.rest;

import es.upsa.ssi.trabajo1.domain.entities.Reserva;
import es.upsa.ssi.trabajo1.domain.entities.ReservasConjuntas;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import es.upsa.ssi.trabajo1.domain.exceptions.ConstraintViolationException;
import es.upsa.ssi.trabajo1.reserva.adapters.input.rest.dtos.ExceptionDto;
import es.upsa.ssi.trabajo1.reserva.adapters.input.rest.dtos.ReservaDto;
import es.upsa.ssi.trabajo1.reserva.application.usecases.*;
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
import java.util.List;
import java.util.Optional;

@Tag(name = "reservas")
@Path("/reservas")
@RequestScoped
public class ReservasResource {

    @Context
    UriInfo uriInfo;
    @Inject
    FindAllReservasUseCase findAllReservasUseCase;
    @Inject
    FindReservaByIdUseCase findReservaByIdUseCase;
    @Inject
    FindReservasHotelByIdHotelConjuntasUseCase findReservasHotelByIdHotelConjuntasUseCase;
    @Inject
    FindReservasConjuntasByIdsUseCase findReservasConjuntasByIds;
    @Inject
    AddReservaUseCase addReservaUseCase;
    @Inject
    UpdateReservaUseCase updateReservaUseCase;
    @Inject
    DeleteReservaUseCase deleteReservaUseCase;

    //http://localhost:8083/reservas
    //http://localhost:8083/reservas?ids=1,2,3,4,5,6


    @Operation(operationId = "getReservas",
            summary = "Acceso a los datos de todas las reservas registradas",
            description = "Devuelve los datos de todas las reservas registradas en el sistema"
    )
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Se devuelve los datos de las reservas",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.ARRAY,
                                    implementation = Reserva.class
                            )
                    )
            ),
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAllReservas(@DefaultValue("")@QueryParam("ids") List<String> ids) throws AppException {

        List<Reserva> reservas = ids.isEmpty() ? findAllReservasUseCase.execute() : findReservasConjuntasByIds.execute(ids);
        if(reservas.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ExceptionDto.builder()
                            .withMessage("No hay reserva con los ids -> " + ids)
                            .build()
                    )
                    .build();
        }

        return Response.ok()
                .entity(reservas)
                .build();
    }

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
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findReservaById(@PathParam("id") String id) throws AppException {
        Reserva reserva1 = findReservaByIdUseCase.execute(id);

        if(reserva1.id() == null){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ExceptionDto.builder()
                            .withMessage("La reserva con el id -> " + id + " no existe.")
                            .build()
                    )
                    .build();
        }
        return Response.ok()
                .entity(reserva1).build();
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
                    description = "No hay registrada una reserva con el ID del hotel"
            )
    })
    @GET
    @Path("/hotel/{idHotel}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findReservasHotelByIdHotelConjuntas(@PathParam("idHotel") String idHotel) throws AppException {


        List<ReservasConjuntas> reservasConjuntas = findReservasHotelByIdHotelConjuntasUseCase.execute(idHotel);

        if( reservasConjuntas.isEmpty() ) return Response.status(Response.Status.NOT_FOUND)
                .entity(
                        ExceptionDto.builder()
                                .withMessage("No hay reserva en el hotel -> " + idHotel)
                                .build())
                .build();

        return Response.ok()
                .entity(reservasConjuntas)
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
    public Response addReserva(ReservaDto reservaDto) throws AppException{
            Reserva reserva = Reserva.builder()
                    .withDni(reservaDto.getDni())
                    .withId_hotel(reservaDto.getId_hotel())
                    .withDNITitular(reservaDto.getDNITitular())
                    .withFechaEntrada(reservaDto.getFechaEntrada())
                    .withFechaSalida(reservaDto.getFechaSalida())
                    .withPrecioTotal(reservaDto.getPrecioTotal())
                    .build();

            if (reservaDto.getDni() == null || reservaDto.getDni().isBlank()) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(ExceptionDto.builder()
                                .withMessage("El DNI no puede estar vacio")
                                .build()
                        )
                        .build();
            } else if (reservaDto.getDNITitular() == null || reservaDto.getDNITitular().isBlank()) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(ExceptionDto.builder()
                                .withMessage("El DNI del titular no puede estar vacio")
                                .build()
                        )
                        .build();
            } else if (reservaDto.getFechaEntrada() == null) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(ExceptionDto.builder()
                                .withMessage("La fecha de entrada no puede estar vacia")
                                .build()
                        )
                        .build();
            } else if (reservaDto.getFechaSalida() == null) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(ExceptionDto.builder()
                                .withMessage("La fecha de salida no puede estar vacia")
                                .build()
                        )
                        .build();
            } else if (reservaDto.getPrecioTotal() < 1) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(ExceptionDto.builder()
                                .withMessage("El precio total no puede ser menor de 1")
                                .build()
                        )
                        .build();
            } else if (reservaDto.getFechaEntrada().isAfter(reservaDto.getFechaSalida())) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(ExceptionDto.builder()
                                .withMessage("La fecha de entrada no puede ser posterior a la de la salida")
                                .build()
                        )
                        .build();
            }


            Reserva newReserva = addReservaUseCase.execute(reserva);


            if (newReserva.id() == null) {

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(ExceptionDto.builder()
                                .withMessage(newReserva.DNITitular())
                                .build()
                        )
                        .build();
            }

            URI uri = uriInfo.getAbsolutePathBuilder()
                    .path("{id}")
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
                    description = "No existe la reserva identificada por el id indicado"
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
    public Response updateReserva (@PathParam("id")String id ,ReservaDto reservaDto) throws AppException {

        Reserva reserva = Reserva.builder()
                .withId(id)
                .withDni(reservaDto.getDni())
                .withId_hotel(reservaDto.getId_hotel())
                .withDNITitular(reservaDto.getDNITitular())
                .withFechaEntrada(reservaDto.getFechaEntrada())
                .withFechaSalida(reservaDto.getFechaSalida())
                .withPrecioTotal(reservaDto.getPrecioTotal())
                .build();


        Reserva execute = findReservaByIdUseCase.execute(id);
        if(execute.id()==null){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ExceptionDto.builder()
                            .withMessage("La reserva con el ID -> " + id + " no existe.")
                            .build()
                    )
                    .build();
        }
        if(reservaDto.getDni() == null || reservaDto.getDni().isBlank()){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ExceptionDto.builder()
                            .withMessage("El id no puede estar vacio")
                            .build()
                    )
                    .build();
        }else if(reservaDto.getDNITitular() == null || reservaDto.getDNITitular().isBlank()){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ExceptionDto.builder()
                            .withMessage("El id del titular no puede estar vacio")
                            .build()
                    )
                    .build();
        }else if(reservaDto.getFechaEntrada() == null){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ExceptionDto.builder()
                            .withMessage("La fecha de entrada no puede estar vacia")
                            .build()
                    )
                    .build();
        }else if(reservaDto.getFechaSalida() == null ) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ExceptionDto.builder()
                            .withMessage("La fecha de salida no puede estar vacia")
                            .build()
                    )
                    .build();
        }else if(reservaDto.getPrecioTotal() < 1){
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(ExceptionDto.builder()
                                .withMessage("El precio total no puede ser menor de 1")
                                .build()
                        )
                        .build();
        }else if(reservaDto.getFechaEntrada().isAfter(reservaDto.getFechaSalida())){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ExceptionDto.builder()
                            .withMessage("La fecha de entrada no puede ser posterior a la de la salida")
                            .build()
                    )
                    .build();
        }

        Reserva updatedReserva = updateReservaUseCase.execute(reserva);

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
    public Response deleteReserva(@PathParam("id") String id) throws AppException {

        deleteReservaUseCase.execute(id);
        return Response.noContent()
                .build();

    }


}


