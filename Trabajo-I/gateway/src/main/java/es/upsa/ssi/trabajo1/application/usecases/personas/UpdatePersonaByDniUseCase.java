package es.upsa.ssi.trabajo1.application.usecases.personas;

import es.upsa.ssi.trabajo1.domain.entities.Persona;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;

public interface UpdatePersonaByDniUseCase {
    Persona execute(Persona persona) throws AppException;
}
