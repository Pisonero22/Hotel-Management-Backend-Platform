package es.upsa.ssi.trabajo1.personas.application;

import es.upsa.ssi.trabajo1.domain.exceptions.AppException;


public interface DeletePersonaByDniUseCase {
    boolean execute(String dni) throws AppException;
}
