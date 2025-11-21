package es.upsa.ssi.trabajo1.application.usecases.impl.personas;

import es.upsa.ssi.trabajo1.application.usecases.personas.FindAllPersonasUseCase;
import es.upsa.ssi.trabajo1.domain.Repository;
import es.upsa.ssi.trabajo1.domain.entities.Persona;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class FindAllPersonasUseCaseImpl implements FindAllPersonasUseCase {

    @Inject
    Repository repository;

    @Override
    public List<Persona> execute() throws AppException {
        return repository.findAllPersonas();
    }
}
