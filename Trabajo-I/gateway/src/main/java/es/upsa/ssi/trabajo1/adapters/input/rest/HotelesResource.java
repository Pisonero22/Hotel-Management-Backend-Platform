package es.upsa.ssi.trabajo1.adapters.input.rest;

import es.upsa.ssi.trabajo1.application.dtos.ErrorDto;
import es.upsa.ssi.trabajo1.application.dtos.ExceptionDto;
import es.upsa.ssi.trabajo1.application.dtos.HotelDto;
import es.upsa.ssi.trabajo1.application.usecases.hoteles.*;
import es.upsa.ssi.trabajo1.domain.entities.Hotel;
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
import java.util.List;
import java.util.Optional;

@Path("/hoteles")
public class HotelesResource {

    @Inject
    UriInfo uriInfo;
    @Inject
    FindAllHotelesUseCase findAllHotelesUseCase;
    @Inject
    FindHotelByIdUseCase findHotelByIdUseCase;
    @Inject
    AddHotelUseCase addHotelUseCase;
    @Inject
    UpdateHotelUseCase updateHotelUseCase;
    @Inject
    DeleteHotelUseCase deleteHotelUseCase;



    @Operation(operationId = "getHoteles",
            summary = "Acceso a los datos de todos los hoteles registrados",
            description = "Devuelve los datos de todos los hoteles registrados en el sistema"
    )
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Se devuelve los datos de los hoteles",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.ARRAY,
                                    implementation = Hotel.class
                            )
                    )
            ),
            @APIResponse(responseCode = "404",
                    description = "No hay registrado ningun hotel"
            )
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAllHoteles() throws AppException {

        List<Hotel> hoteles = findAllHotelesUseCase.execute();

        if(hoteles.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ErrorDto.builder()
                            .withMensaje("No hay ningun hotel")
                            .build()
                    )
                    .build();
        }
        return Response.ok()
                .entity(findAllHotelesUseCase.execute())
                .build();

    }


    @Operation(operationId = "getHotelById",
            summary = "Acceso a los datos de un hotel identificado por su ID",
            description = "Devuelve los datos del hotel identificado a través de su ID"
    )
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Se ha localizado el hotel y se devuelve sus datos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.OBJECT,
                                    implementation = Hotel.class
                            )
                    )
            ),
            @APIResponse(responseCode = "404",
                    description = "No hay registrado un hotel con el ID proporcionado"
            )
    })
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findHotelById(@PathParam("id") String id) throws AppException {
        Optional<Hotel> optional = findHotelByIdUseCase.execute(id);

        if ( optional.isEmpty() )
        {
            return Response.status( Response.Status.NOT_FOUND )
                    .entity(ExceptionDto.builder()
                            .withMessage("No existe el hotel con identificador " + id)
                            .build()
                    )
                    .build();
        }

        Hotel hotel = optional.get();
        return Response.ok()
                .entity( hotel )
                .build();
    }


    @Operation(operationId = "postHotel",
            description = "Crea un hotel",
            summary = "Registra un nuevo hotel en el sistema"
    )
    @APIResponses({
            @APIResponse(responseCode = "201",
                    description = "Se ha creado el hotel. Se devuelve sus datos entre los que se incluye su identificador",
                    headers = @Header(name = HttpHeaders.LOCATION,
                            description = "URI con la que acceder al hotel creado",
                            schema = @Schema(type = SchemaType.STRING,
                                    format = "uri"
                            )
                    ),
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.OBJECT,
                                    implementation = Hotel.class
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
    public Response addHotel(HotelDto hotelDto) throws AppException {
        Hotel hotel = Hotel.builder()
                .withNombre(hotelDto.getNombre())
                .withLocalizacion(hotelDto.getLocalizacion())
                .withEstrellas(hotelDto.getEstrellas())
                .withPrecioNoche(hotelDto.getPrecioNoche())
                .build();

        if(hotelDto.getNombre()==null || hotelDto.getNombre().isBlank()){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje("El nombre introducido no puede estar vacio")
                            .build()
                    )
                    .build();
        }else if(hotelDto.getLocalizacion()==null || hotelDto.getLocalizacion().isBlank()){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje("La localizacion introducida no puede estar vacia")
                            .build()
                    )
                    .build();
        } else if (hotelDto.getEstrellas() < 1 || hotelDto.getEstrellas() > 5 ) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje("El numero de estrellas debe ser entre 1 y 5")
                            .build()
                    )
                    .build();
        }else if (hotelDto.getPrecioNoche() < 1) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje("El precio no puede ser menor de 1")
                            .build()
                    )
                    .build();
        }


        Hotel newHotel = addHotelUseCase.execute(hotel);

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path("/{id}")
                .resolveTemplate("id", newHotel.id())
                .build();

        return Response.created(uri)
                .entity(newHotel)
                .build();
    }







    @Operation(operationId = "putHotel",
            description = "Modifica los datos de un hotel",
            summary = "Modifica los datos del hotel identificado por su ID"
    )
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Se ha modificado los datos del hotel",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.OBJECT,
                                    implementation = Hotel.class
                            )
                    )
            ),
            @APIResponse(responseCode = "404",
                    description = "No existe el hotel identificado por el ID indicado"
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
    public Response updateHotel(@PathParam("id")String id, HotelDto hotelDto) throws AppException {
        Hotel hotel = Hotel.builder()
                .withId(id)
                .withNombre(hotelDto.getNombre())
                .withLocalizacion(hotelDto.getLocalizacion())
                .withEstrellas(hotelDto.getEstrellas())
                .withPrecioNoche(hotelDto.getPrecioNoche())
                .build();

        Optional<Hotel> hotel1 = findHotelByIdUseCase.execute(id);

        if ( hotel1.isEmpty() ){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ErrorDto.builder()
                            .withMensaje("El hotel con ID " + id + " no existe")
                            .build()
                    )
                    .build();
        }

        if(hotelDto.getNombre()==null || hotelDto.getNombre().isBlank()){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje("El nombre introducido no puede estar vacio")
                            .build()
                    )
                    .build();
        }else if(hotelDto.getLocalizacion()==null || hotelDto.getLocalizacion().isBlank()){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje("La localizacion introducida no puede estar vacia")
                            .build()
                    )
                    .build();
        } else if (hotelDto.getEstrellas() < 1 || hotelDto.getEstrellas() > 5 ) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje("El numero de estrellas debe ser entre 1 y 5")
                            .build()
                    )
                    .build();
        }else if (hotelDto.getPrecioNoche() < 1) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDto.builder()
                            .withMensaje("El precio no puede ser menor de 1")
                            .build()
                    )
                    .build();
        }

        Hotel updatedHotel = updateHotelUseCase.execute(hotel);
        return Response.ok()
                .entity(updatedHotel)
                .build();
    }


    @Operation(operationId = "deleteHotel",
            description = "Elimina un hotel",
            summary = "Elimina el hotel identificado por su ID"
    )
    @APIResponses({
            @APIResponse(responseCode = "204",
                    description = "Se ha eliminado el hotel"
            ),

            @APIResponse(responseCode="404",
                    description="No existe el hotel identificado por ese ID"
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
    @Path("{id}")
    public Response deleteHotel(@PathParam("id")String id) throws AppException {

        Optional<Hotel> hotel1 = findHotelByIdUseCase.execute(id);

        if ( hotel1.isEmpty() ){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ErrorDto.builder()
                            .withMensaje("El hotel con ID " + id + " no existe")
                            .build()
                    )
                    .build();
        }

        String borrado = deleteHotelUseCase.execute(id);

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
