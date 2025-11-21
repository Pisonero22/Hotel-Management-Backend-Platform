package es.upsa.ssi.trabajo1.adapters.output;

import es.upsa.ssi.trabajo1.adapters.output.providers.GatewayResponseExceptionMapper;
import es.upsa.ssi.trabajo1.application.dtos.HotelDto;
import es.upsa.ssi.trabajo1.domain.entities.Hotel;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient(configKey = "rest.client.hoteles")
@RegisterProvider(GatewayResponseExceptionMapper.class)
@Path("/hoteles")
public interface HotelesServiceClient {


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<Hotel> requestGetHoteles() throws AppException;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Hotel requestGetHotel(@PathParam("id") String hotelId) throws AppException;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Hotel addHotel(HotelDto hotelDto) throws AppException;

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Hotel updateHotel(@PathParam("id")String id, HotelDto hotelDto) throws AppException;

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    String deleteHotel(@PathParam("id")String id) throws AppException;
}
