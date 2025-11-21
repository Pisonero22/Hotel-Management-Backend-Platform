package es.upsa.ssi.trabajo1.application.usecases.reservas;

import es.upsa.ssi.trabajo1.domain.entities.Reserva;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;

public interface AddReservaUseCase {
    Reserva execute(Reserva reserva) throws AppException;
}
