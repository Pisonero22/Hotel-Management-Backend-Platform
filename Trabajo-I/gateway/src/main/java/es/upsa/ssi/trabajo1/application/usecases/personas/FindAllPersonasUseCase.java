package es.upsa.ssi.trabajo1.application.usecases.personas;

import es.upsa.ssi.trabajo1.domain.entities.Persona;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;

import java.util.List;

public interface FindAllPersonasUseCase {
    List<Persona> execute() throws AppException;
}
