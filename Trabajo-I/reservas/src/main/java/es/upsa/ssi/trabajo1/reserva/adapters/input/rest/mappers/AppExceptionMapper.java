package es.upsa.ssi.trabajo1.reserva.adapters.input.rest.mappers;

import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import es.upsa.ssi.trabajo1.domain.exceptions.EntityNotFoundException;
import es.upsa.ssi.trabajo1.reserva.adapters.input.rest.dtos.ExceptionDto;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class AppExceptionMapper implements ExceptionMapper<AppException> {

    @Override
    public Response toResponse(AppException exception) {
        if(exception instanceof EntityNotFoundException enfe){

            return Response.status(Response.Status.NOT_FOUND)
                    .entity(
                            ExceptionDto.builder()
                                    .withMessage(exception.getMessage())
                                    .build()
                    )
                    .build();
        }
        return Response.serverError()
                .entity(
                        ExceptionDto.builder()
                                .withMessage(exception.getMessage())
                                .build()
                )
                .build();

    }
}
