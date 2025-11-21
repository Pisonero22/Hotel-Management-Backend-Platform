package es.upsa.ssi.trabajo1.adapters.output.providers;

import es.upsa.ssi.trabajo1.application.dtos.ExceptionDto;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import es.upsa.ssi.trabajo1.domain.exceptions.EntityNotFoundException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;



public class GatewayResponseExceptionMapper implements ResponseExceptionMapper<AppException> {

    @Override
    public AppException toThrowable(Response response) {

        return switch (response.getStatusInfo().toEnum()) {
            case NOT_FOUND -> new EntityNotFoundException(response.readEntity(ExceptionDto.class).getMessage());
            case INTERNAL_SERVER_ERROR -> new AppException(response.readEntity(ExceptionDto.class).getMessage());
            default -> {
                ExceptionDto exceptionDto2 = response.readEntity(ExceptionDto.class);
                String message2 = exceptionDto2.getMessage();
                yield new AppException(message2);
            }
        };
    }


}