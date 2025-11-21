package es.upsa.ssi.trabajo1.reserva.application.usecases;

import es.upsa.ssi.trabajo1.domain.exceptions.AppException;

public interface DeleteReservaUseCase {
    void execute(String id) throws AppException;
}
