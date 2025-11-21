package es.upsa.ssi.trabajo1.application.usecases.impl.personas;

import es.upsa.ssi.trabajo1.application.usecases.personas.UpdatePersonaByDniUseCase;
import es.upsa.ssi.trabajo1.domain.Repository;
import es.upsa.ssi.trabajo1.domain.entities.Persona;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UpdatePersonaByDniUseCaseImpl implements UpdatePersonaByDniUseCase {

    @Inject
    Repository repository;

    @Override
    public Persona execute(Persona persona) throws AppException {
        return repository.updatePersonaByDni(persona);
    }
}
