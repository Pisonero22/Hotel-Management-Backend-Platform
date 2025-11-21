package es.upsa.ssi.trabajo1.personas.application.impl;


import es.upsa.ssi.trabajo1.domain.entities.Persona;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import es.upsa.ssi.trabajo1.personas.application.UpdatePersonaUseCase;
import es.upsa.ssi.trabajo1.personas.domain.Repository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UpdatePersonaUseCaseImpl implements UpdatePersonaUseCase {

    @Inject
    Repository repository;

    @Override
    public Persona execute(Persona persona) throws AppException {
        return repository.updatePersona(persona);
    }
}
