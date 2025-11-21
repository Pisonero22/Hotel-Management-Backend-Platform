package es.upsa.ssi.trabajo1.hoteles.application.impl;


import es.upsa.ssi.trabajo1.domain.entities.Hotel;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import es.upsa.ssi.trabajo1.hoteles.application.UpdateHotelUseCase;
import es.upsa.ssi.trabajo1.hoteles.domain.Repository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UpdateHotelUseCaseImpl implements UpdateHotelUseCase {

    @Inject
    Repository repository;


    @Override
    public Hotel execute(Hotel hotel) throws AppException {
        return repository.save(hotel);
    }
}
