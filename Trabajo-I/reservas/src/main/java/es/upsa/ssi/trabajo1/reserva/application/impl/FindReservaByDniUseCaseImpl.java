package es.upsa.ssi.trabajo1.reserva.application.impl;

import es.upsa.ssi.trabajo1.domain.entities.Reserva;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import es.upsa.ssi.trabajo1.reserva.application.usecases.FindReservaByDniUseCase;
import es.upsa.ssi.trabajo1.reserva.domain.Repository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class FindReservaByDniUseCaseImpl implements FindReservaByDniUseCase {

    @Inject
    Repository repository;

    @Override
    public List<Reserva> execute(String dni) throws AppException {

        return repository.findReservaByDni(dni);

    }
}
