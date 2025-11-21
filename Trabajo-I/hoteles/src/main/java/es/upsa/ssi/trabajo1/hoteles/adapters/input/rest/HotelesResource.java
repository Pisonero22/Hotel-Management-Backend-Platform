package es.upsa.ssi.trabajo1.hoteles.adapters.input.rest;

import es.upsa.ssi.trabajo1.domain.entities.Hotel;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import es.upsa.ssi.trabajo1.hoteles.adapters.input.rest.dtos.ExceptionDto;
import es.upsa.ssi.trabajo1.hoteles.adapters.input.rest.dtos.HotelDto;
import es.upsa.ssi.trabajo1.hoteles.application.*;
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
import java.util.Optional;


@RequestScoped
@Tag(name = "hoteles")
@Path("/hoteles")
public class HotelesResource {


    @Context
    UriInfo uriInfo;
    @Inject
    FindAllHotelesUseCase findAllHotelesUseCase;
    @Inject
    FindHotelByIdUseCase findHotelByIdUseCase;
    @Inject
    InsertarHotelUseCase insertarHotelUseCase;
    @Inject
    UpdateHotelUseCase updateHotelUseCase;
    @Inject
    DeleteHotelById deleteHotelById;


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
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAllHoteles() throws AppException {
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
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findHotelById(@PathParam("id") String id) throws AppException {
        Hotel hotel1 = findHotelByIdUseCase.execute(id);
        if(hotel1.id() == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(
                            ExceptionDto.builder()
                                    .withMessage("El hotel con id -> " + id + " no existe.")
                                    .build()
                    )
                    .build();
        }
        return Response.ok(hotel1).build();
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
    public Response addHotel(HotelDto hotelDto) throws AppException{

        Hotel hotelBuild = Hotel.builder()
                .withNombre(hotelDto.getNombre())
                .withLocalizacion(hotelDto.getLocalizacion())
                .withEstrellas(hotelDto.getEstrellas())
                .withPrecioNoche(hotelDto.getPrecioNoche())
                .build();



        if(hotelDto.getNombre()==null || hotelDto.getNombre().isBlank()){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ExceptionDto.builder()
                            .withMessage("El nombre introducido no puede estar vacio")
                            .build()
                    )
                    .build();
        }else if(hotelDto.getLocalizacion()==null || hotelDto.getLocalizacion().isBlank()){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ExceptionDto.builder()
                            .withMessage("La localizacion introducida no puede estar vacia")
                            .build()
                    )
                    .build();
        } else if (hotelDto.getEstrellas() < 1 || hotelDto.getEstrellas() > 5 ) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ExceptionDto.builder()
                            .withMessage("El numero de estrellas debe ser entre 1 y 5")
                            .build()
                    )
                    .build();
        }else if (hotelDto.getPrecioNoche() < 1) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ExceptionDto.builder()
                            .withMessage("El precio no puede ser menor de 1")
                            .build()
                    )
                    .build();
        }

        Hotel newHotel = insertarHotelUseCase.execute(hotelBuild);
        URI newHotelUri = uriInfo.getAbsolutePathBuilder()
                .path("/{id}")
                .resolveTemplate("id", newHotel.id())
                .build();

        return Response.created(newHotelUri)
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
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateHotel(@PathParam("id")String id, HotelDto hotelDto) throws AppException{
        Hotel hotel = Hotel.builder()
                .withId(id)
                .withNombre(hotelDto.getNombre())
                .withLocalizacion(hotelDto.getLocalizacion())
                .withEstrellas(hotelDto.getEstrellas())
                .withPrecioNoche(hotelDto.getPrecioNoche())
                .build();

        Hotel hotel1 = findHotelByIdUseCase.execute(id);

        if(hotel1.id()==null){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ExceptionDto.builder()
                            .withMessage("El hotel con el ID -> " + id + " no existe.")
                            .build()
                    )
                    .build();
        }

        if(hotelDto.getNombre()==null || hotelDto.getNombre().isBlank()){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ExceptionDto.builder()
                            .withMessage("El nombre introducido no puede estar vacio")
                            .build()
                    )
                    .build();
        }else if(hotelDto.getLocalizacion()==null || hotelDto.getLocalizacion().isBlank()){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ExceptionDto.builder()
                            .withMessage("La localizacion introducida no puede estar vacia")
                            .build()
                    )
                    .build();
        } else if (hotelDto.getEstrellas() < 1 || hotelDto.getEstrellas() > 5 ) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ExceptionDto.builder()
                            .withMessage("El numero de estrellas debe ser entre 1 y 5")
                            .build()
                    )
                    .build();
        }else if (hotelDto.getPrecioNoche() < 1) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ExceptionDto.builder()
                            .withMessage("El precio no puede ser menor de 1")
                            .build()
                    )
                    .build();
        }

        Hotel hotelUpdated = updateHotelUseCase.execute(hotel);

        return Response.ok()
                .entity(hotelUpdated)
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
    public Response deleteHotel(@PathParam("id")String id) throws AppException{

        Hotel hotel = findHotelByIdUseCase.execute(id);
        if(hotel.id()==null){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ExceptionDto.builder()
                            .withMessage("El hotel con el id -> " + id + " no existe")
                            .build()
                    )
                    .build();
        }

        String borrado = deleteHotelById.execute(id);

        if(borrado==null){
            return Response.noContent()
                    .build();

        }else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ExceptionDto.builder()
                            .withMessage(borrado)
                            .build()
                    )
                    .build();
        }




    }







}
