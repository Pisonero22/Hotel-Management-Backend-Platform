package es.upsa.ssi.trabajo1.application.usecases.impl.reservas;

import es.upsa.ssi.trabajo1.application.usecases.reservas.UpdateReservaUseCase;
import es.upsa.ssi.trabajo1.domain.Repository;
import es.upsa.ssi.trabajo1.domain.entities.Reserva;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class UpdateReservaUseCaseImpl implements UpdateReservaUseCase {

    @Inject
    Repository repository;

    @Override
    public Reserva execute(Reserva reserva) throws AppException {
        return repository.updateReserva(reserva);
    }
}
