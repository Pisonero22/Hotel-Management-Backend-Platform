package es.upsa.ssi.trabajo1.application.usecases.impl.reservas;

import es.upsa.ssi.trabajo1.application.usecases.reservas.DeleteReservaUseCase;
import es.upsa.ssi.trabajo1.domain.Repository;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DeleteReservaUseCaseImpl implements DeleteReservaUseCase {

    @Inject
    Repository repository;

    @Override
    public String execute(String id) throws AppException {
        return repository.deleteReserva(id);
    }
}
