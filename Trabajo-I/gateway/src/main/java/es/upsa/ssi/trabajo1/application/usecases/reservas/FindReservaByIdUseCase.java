package es.upsa.ssi.trabajo1.application.usecases.reservas;

import es.upsa.ssi.trabajo1.domain.entities.Reserva;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;

import java.util.Optional;

public interface FindReservaByIdUseCase {
    Optional<Reserva> execute(String idReserva) throws AppException;
}
