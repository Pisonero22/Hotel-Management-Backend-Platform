package es.upsa.ssi.trabajo1.personas.application;

import es.upsa.ssi.trabajo1.domain.entities.Persona;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;

import java.util.List;

public interface FindAllPersonasUseCase {
    public List<Persona> execute() throws AppException;
}
