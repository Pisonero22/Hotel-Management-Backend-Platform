package es.upsa.ssi.trabajo1.hoteles.application.impl;

import es.upsa.ssi.trabajo1.domain.entities.Hotel;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import es.upsa.ssi.trabajo1.hoteles.application.FindAllHotelesUseCase;
import es.upsa.ssi.trabajo1.hoteles.domain.Repository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped

public class FindAllHotelesUseCaseImpl implements FindAllHotelesUseCase {

    @Inject
    Repository repository;

    @Override
    public List<Hotel> execute() throws AppException {
        return repository.findAllHoteles();
    }
}
