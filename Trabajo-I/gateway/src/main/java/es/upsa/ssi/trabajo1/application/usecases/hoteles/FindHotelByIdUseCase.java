package es.upsa.ssi.trabajo1.application.usecases.hoteles;

import es.upsa.ssi.trabajo1.domain.entities.Hotel;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;

import java.util.Optional;

public interface FindHotelByIdUseCase {
    Optional<Hotel> execute(String id) throws AppException;
}
