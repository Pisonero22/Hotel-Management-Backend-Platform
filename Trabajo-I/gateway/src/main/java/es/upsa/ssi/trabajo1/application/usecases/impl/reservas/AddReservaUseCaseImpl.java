package es.upsa.ssi.trabajo1.application.usecases.impl.reservas;

import es.upsa.ssi.trabajo1.application.usecases.reservas.AddReservaUseCase;
import es.upsa.ssi.trabajo1.domain.Repository;
import es.upsa.ssi.trabajo1.domain.entities.Reserva;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AddReservaUseCaseImpl implements AddReservaUseCase {

    @Inject
    Repository repository;

    @Override
    public Reserva execute(Reserva reserva) throws AppException {
        return repository.addReserva(reserva);
    }
}
