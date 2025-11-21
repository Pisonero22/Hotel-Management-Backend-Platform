package es.upsa.ssi.trabajo1.personas.application.impl;

import es.upsa.ssi.trabajo1.personas.application.FindAllPersonasUseCase;
import es.upsa.ssi.trabajo1.personas.domain.Repository;
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
