package es.upsa.ssi.trabajo1.application.usecases.impl.reservas;


import es.upsa.ssi.trabajo1.application.usecases.reservas.FindReservaByIdUseCase;
import es.upsa.ssi.trabajo1.domain.Repository;
import es.upsa.ssi.trabajo1.domain.entities.Reserva;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class FindReservaByIdUseCaseImpl implements FindReservaByIdUseCase {

    @Inject
    Repository repository;

    @Override
    public Optional<Reserva> execute(String idReserva) throws AppException {
        return repository.findReservaById(idReserva);
    }
}
