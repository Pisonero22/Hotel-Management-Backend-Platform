package es.upsa.ssi.trabajo1.hoteles.application;

import es.upsa.ssi.trabajo1.domain.entities.Hotel;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;

import java.util.Optional;

public interface FindHotelByIdUseCase {
     Hotel execute(String id) throws AppException;
}
