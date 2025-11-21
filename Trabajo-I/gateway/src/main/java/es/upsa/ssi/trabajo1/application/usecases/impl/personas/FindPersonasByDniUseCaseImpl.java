package es.upsa.ssi.trabajo1.application.usecases.impl.personas;

import es.upsa.ssi.trabajo1.application.usecases.personas.FindPersonasByDniUseCase;
import es.upsa.ssi.trabajo1.domain.Repository;
import es.upsa.ssi.trabajo1.domain.entities.Persona;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class FindPersonasByDniUseCaseImpl implements FindPersonasByDniUseCase {

    @Inject
    Repository repository;

    @Override
    public Persona execute(String dni) throws AppException {
        return repository.findPersonasByDni(dni);

    }
}
