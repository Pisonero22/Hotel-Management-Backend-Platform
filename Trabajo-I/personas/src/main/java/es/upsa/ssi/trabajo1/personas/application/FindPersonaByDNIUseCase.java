package es.upsa.ssi.trabajo1.personas.application;

import es.upsa.ssi.trabajo1.domain.entities.Persona;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;

import java.util.Optional;

public interface FindPersonaByDNIUseCase {
    Persona execute(String dni) throws AppException;
}
