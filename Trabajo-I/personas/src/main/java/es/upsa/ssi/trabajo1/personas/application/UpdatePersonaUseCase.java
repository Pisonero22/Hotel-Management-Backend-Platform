package es.upsa.ssi.trabajo1.personas.application;



import es.upsa.ssi.trabajo1.domain.entities.Persona;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;

public interface UpdatePersonaUseCase {
    public Persona execute(Persona persona) throws AppException;
}
