package es.upsa.ssi.trabajo1.hoteles.application.impl;

import es.upsa.ssi.trabajo1.domain.entities.Hotel;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import es.upsa.ssi.trabajo1.hoteles.application.FindHotelByIdUseCase;
import es.upsa.ssi.trabajo1.hoteles.domain.Repository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class FindHotelByIdUseCaseImpl implements FindHotelByIdUseCase {

    @Inject
    Repository repository;

    @Override
    public Hotel execute(String id) throws AppException {
        return repository.findHotelById(id);
    }
}
