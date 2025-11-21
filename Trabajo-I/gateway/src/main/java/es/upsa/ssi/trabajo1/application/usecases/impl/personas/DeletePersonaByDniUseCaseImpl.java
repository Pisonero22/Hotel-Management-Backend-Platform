package es.upsa.ssi.trabajo1.application.usecases.impl.personas;

import es.upsa.ssi.trabajo1.application.usecases.personas.DeletePersonaByDniUseCase;
import es.upsa.ssi.trabajo1.domain.Repository;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DeletePersonaByDniUseCaseImpl implements DeletePersonaByDniUseCase {

    @Inject
    Repository repository;


    @Override
    public String execute(String dni) throws AppException {
        return repository.deletePersonaByDni(dni);
    }
}
