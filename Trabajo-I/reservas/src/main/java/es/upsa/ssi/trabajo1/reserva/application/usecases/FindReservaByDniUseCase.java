package es.upsa.ssi.trabajo1.reserva.application.usecases;

import es.upsa.ssi.trabajo1.domain.entities.Reserva;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;

import java.util.List;

public interface FindReservaByDniUseCase {
    List<Reserva> execute (String dni) throws AppException;
}
