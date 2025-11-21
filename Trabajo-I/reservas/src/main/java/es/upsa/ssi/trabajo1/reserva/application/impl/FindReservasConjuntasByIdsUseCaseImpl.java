package es.upsa.ssi.trabajo1.reserva.application.impl;

import es.upsa.ssi.trabajo1.domain.entities.Reserva;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import es.upsa.ssi.trabajo1.reserva.application.usecases.FindReservasConjuntasByIdsUseCase;
import es.upsa.ssi.trabajo1.reserva.domain.Repository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class FindReservasConjuntasByIdsUseCaseImpl implements FindReservasConjuntasByIdsUseCase {

    @Inject
    Repository repository;

    @Override
    public List<Reserva> execute(List<String> ids) throws AppException {
        return repository.findReservasConjuntasByIds(ids);
    }
}
