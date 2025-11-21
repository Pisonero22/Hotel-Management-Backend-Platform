package es.upsa.ssi.trabajo1.application.usecases.impl.hoteles;

import es.upsa.ssi.trabajo1.application.usecases.hoteles.FindAllHotelesUseCase;
import es.upsa.ssi.trabajo1.domain.Repository;
import es.upsa.ssi.trabajo1.domain.entities.Hotel;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
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
