package es.upsa.ssi.trabajo1.application.usecases.impl.personas;

import es.upsa.ssi.trabajo1.application.usecases.personas.AddPersonaUseCase;
import es.upsa.ssi.trabajo1.domain.Repository;
import es.upsa.ssi.trabajo1.domain.entities.Persona;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AddPersonaUseCaseImpl implements AddPersonaUseCase {

    @Inject
    Repository repository;


    @Override
    public Persona execute(Persona persona) throws AppException {
        return repository.addPersona(persona);
    }
}
