package es.upsa.ssi.trabajo1.hoteles.application;

import es.upsa.ssi.trabajo1.domain.entities.Hotel;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;

public interface InsertarHotelUseCase {
     Hotel execute(Hotel hotel) throws AppException;
}
