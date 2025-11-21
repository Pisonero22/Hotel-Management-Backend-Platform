package es.upsa.ssi.trabajo1.application.usecases.impl.hoteles;

import es.upsa.ssi.trabajo1.application.usecases.hoteles.FindHotelByIdUseCase;
import es.upsa.ssi.trabajo1.domain.Repository;
import es.upsa.ssi.trabajo1.domain.entities.Hotel;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import es.upsa.ssi.trabajo1.domain.exceptions.EntityNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;


@ApplicationScoped
public class FindHotelByIdUseCaseImpl implements FindHotelByIdUseCase {

    @Inject
    Repository repository;

    @Override
    public Optional<Hotel> execute(String id) throws AppException {
        return repository.findHotelById(id);
    }


}
