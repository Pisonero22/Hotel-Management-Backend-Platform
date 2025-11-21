package es.upsa.ssi.trabajo1.personas.adapters.output;

import es.upsa.ssi.trabajo1.domain.entities.Persona;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;

import java.util.List;
import java.util.Optional;

public interface DataBaseDao {

    public List<Persona> findAllPersonas() throws AppException;

    Persona findPersonasByDni(String dni) throws AppException;

    Persona addPersona(Persona persona) throws AppException;

    Persona updatePersona(Persona persona) throws AppException;

    boolean deletePersonaByDni(String dni) throws AppException;
}
