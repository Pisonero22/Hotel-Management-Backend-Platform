package es.upsa.ssi.trabajo1.personas.application.impl;

import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import es.upsa.ssi.trabajo1.personas.application.DeletePersonaByDniUseCase;
import es.upsa.ssi.trabajo1.personas.domain.Repository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DeletePersonaByDniUseCaseImpl implements DeletePersonaByDniUseCase {

    @Inject
    Repository repository;


    @Override
    public boolean execute(String dni) throws AppException {
        return repository.deletePersonaByDni(dni);
    }
}
