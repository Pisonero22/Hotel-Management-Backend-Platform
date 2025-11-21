package es.upsa.ssi.trabajo1.application.usecases.reservas;

import es.upsa.ssi.trabajo1.domain.exceptions.AppException;

public interface DeleteReservaUseCase {
    String execute(String id) throws AppException;
}
