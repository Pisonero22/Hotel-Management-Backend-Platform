package es.upsa.ssi.trabajo1.application.usecases.impl.hoteles;

import es.upsa.ssi.trabajo1.application.usecases.hoteles.AddHotelUseCase;
import es.upsa.ssi.trabajo1.domain.Repository;
import es.upsa.ssi.trabajo1.domain.entities.Hotel;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AddHotelUseCaseImpl implements AddHotelUseCase {

    @Inject
    Repository repository;

    @Override
    public Hotel execute(Hotel hotel) throws AppException {
        return repository.addHotel(hotel);
    }
}
