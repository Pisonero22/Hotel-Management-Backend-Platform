package es.upsa.ssi.trabajo1.application.usecases.personas;

import es.upsa.ssi.trabajo1.domain.exceptions.AppException;

public interface DeletePersonaByDniUseCase {
    String execute(String dni) throws AppException;
}
