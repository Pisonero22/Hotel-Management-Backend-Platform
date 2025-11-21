package es.upsa.ssi.trabajo1.reserva.application.usecases;

import es.upsa.ssi.trabajo1.domain.entities.ReservasConjuntas;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;

import java.util.List;

public interface FindReservasHotelByIdHotelConjuntasUseCase {
    List<ReservasConjuntas> execute(String idHotel) throws AppException;
}
