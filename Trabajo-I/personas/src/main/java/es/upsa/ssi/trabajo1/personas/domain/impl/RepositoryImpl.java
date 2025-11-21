package es.upsa.ssi.trabajo1.personas.domain.impl;

import es.upsa.ssi.trabajo1.personas.adapters.output.DataBaseDao;
import es.upsa.ssi.trabajo1.personas.domain.Repository;
import es.upsa.ssi.trabajo1.domain.entities.Persona;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RepositoryImpl implements Repository
{
    @Inject
    DataBaseDao dao;

    @Override
    public List<Persona> findAllPersonas() throws AppException {
        return dao.findAllPersonas();
    }
    @Override
    public Persona findPersonasByDni(String dni) throws AppException {
        return dao.findPersonasByDni(dni);
    }

    @Override
    public Persona addPersona(Persona persona) throws AppException {
        return dao.addPersona(persona);
    }

    @Override
    public Persona updatePersona(Persona persona) throws AppException {
        return dao.updatePersona(persona);
    }

    @Override
    public boolean deletePersonaByDni(String dni) throws AppException {
        return dao.deletePersonaByDni(dni);
    }
}
