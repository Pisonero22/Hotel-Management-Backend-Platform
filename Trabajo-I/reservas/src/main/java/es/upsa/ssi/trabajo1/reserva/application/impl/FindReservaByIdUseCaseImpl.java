package es.upsa.ssi.trabajo1.reserva.application.impl;

import es.upsa.ssi.trabajo1.domain.entities.Reserva;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import es.upsa.ssi.trabajo1.reserva.application.usecases.FindReservaByIdUseCase;
import es.upsa.ssi.trabajo1.reserva.domain.Repository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class FindReservaByIdUseCaseImpl implements FindReservaByIdUseCase {

    @Inject
    Repository repository;

    @Override
    public Reserva execute(String id) throws AppException {
        return repository.findReservaBYId(id);
    }
}
