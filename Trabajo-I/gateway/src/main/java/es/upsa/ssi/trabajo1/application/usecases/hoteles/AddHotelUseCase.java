package es.upsa.ssi.trabajo1.application.usecases.hoteles;

import es.upsa.ssi.trabajo1.domain.entities.Hotel;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;

public interface AddHotelUseCase {
    Hotel execute(Hotel hotel) throws AppException;
}
