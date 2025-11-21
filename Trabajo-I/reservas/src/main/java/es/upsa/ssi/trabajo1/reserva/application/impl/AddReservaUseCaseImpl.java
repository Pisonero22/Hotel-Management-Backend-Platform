package es.upsa.ssi.trabajo1.reserva.application.impl;

import es.upsa.ssi.trabajo1.domain.entities.Reserva;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import es.upsa.ssi.trabajo1.reserva.application.usecases.AddReservaUseCase;
import es.upsa.ssi.trabajo1.reserva.domain.Repository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class AddReservaUseCaseImpl implements AddReservaUseCase {

    @Inject
    Repository repository;

    @Override
    public Reserva execute(Reserva reserva) throws AppException {
        return repository.save(reserva);
    }
}
