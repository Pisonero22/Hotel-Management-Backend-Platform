package es.upsa.ssi.trabajo1.application.usecases.reservas;

import es.upsa.ssi.trabajo1.domain.entities.HotelReservasFull;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;

public interface FindReservasByIdHotelUseCase {
    HotelReservasFull execute(String hotelId) throws AppException;
}
