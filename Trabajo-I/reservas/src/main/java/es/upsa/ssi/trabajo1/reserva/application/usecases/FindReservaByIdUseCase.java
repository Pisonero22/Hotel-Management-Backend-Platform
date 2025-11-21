package es.upsa.ssi.trabajo1.reserva.application.usecases;

import es.upsa.ssi.trabajo1.domain.entities.Reserva;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;

import java.util.Optional;

public interface FindReservaByIdUseCase {
    Reserva execute(String id) throws AppException;
}
