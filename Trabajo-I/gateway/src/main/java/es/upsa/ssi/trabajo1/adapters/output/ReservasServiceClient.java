package es.upsa.ssi.trabajo1.adapters.output;

import es.upsa.ssi.trabajo1.adapters.output.providers.GatewayResponseExceptionMapper;
import es.upsa.ssi.trabajo1.application.dtos.ReservaDto;
import es.upsa.ssi.trabajo1.domain.entities.Reserva;
import es.upsa.ssi.trabajo1.domain.entities.ReservasConjuntas;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;


@RegisterRestClient(configKey = "rest.client.reservas")
@RegisterProvider(GatewayResponseExceptionMapper.class)
@Path("/reservas")
public interface ReservasServiceClient {

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Reserva getReserva(@PathParam("id") String id) throws AppException;

    @GET
    @Path("/hotel/{idHotel}")
    @Produces(MediaType.APPLICATION_JSON)
    List<ReservasConjuntas> findReservasHotelByIdHotelConjuntas(@PathParam("idHotel") String idHotel) throws AppException;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Reserva addReserva(ReservaDto reservaDto) throws AppException;

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Reserva updateReserva (@PathParam("id")String id ,ReservaDto reservaDto) throws AppException;

    @DELETE
    @Path("{id}")
    String deleteReserva(@PathParam("id") String id) throws AppException;


}
