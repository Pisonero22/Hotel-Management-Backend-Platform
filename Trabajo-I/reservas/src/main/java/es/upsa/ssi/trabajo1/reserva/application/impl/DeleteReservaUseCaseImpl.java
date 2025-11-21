package es.upsa.ssi.trabajo1.reserva.application.impl;

import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import es.upsa.ssi.trabajo1.reserva.application.usecases.DeleteReservaUseCase;
import es.upsa.ssi.trabajo1.reserva.domain.Repository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class DeleteReservaUseCaseImpl implements DeleteReservaUseCase {

    @Inject
    Repository repository;

    @Override
    public void execute(String id) throws AppException {
        repository.deleteReserva(id);
    }
}
