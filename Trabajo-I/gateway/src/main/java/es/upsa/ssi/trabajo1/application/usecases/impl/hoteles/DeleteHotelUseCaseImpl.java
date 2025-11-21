package es.upsa.ssi.trabajo1.application.usecases.impl.hoteles;

import es.upsa.ssi.trabajo1.application.usecases.hoteles.DeleteHotelUseCase;
import es.upsa.ssi.trabajo1.domain.Repository;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DeleteHotelUseCaseImpl implements DeleteHotelUseCase {

    @Inject
    Repository repository;


    @Override
    public String execute(String id) throws AppException {
        return repository.deleteHotel(id);
    }
}
