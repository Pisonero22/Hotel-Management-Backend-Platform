package es.upsa.ssi.trabajo1.personas.application.impl;


import es.upsa.ssi.trabajo1.domain.entities.Persona;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import es.upsa.ssi.trabajo1.personas.application.FindPersonaByDNIUseCase;
import es.upsa.ssi.trabajo1.personas.domain.Repository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class FindPersonaByDNIUseCaseImpl implements FindPersonaByDNIUseCase {

    @Inject
    Repository repository;


    @Override
    public Persona execute(String dni) throws AppException {
        return repository.findPersonasByDni(dni);
    }
}
