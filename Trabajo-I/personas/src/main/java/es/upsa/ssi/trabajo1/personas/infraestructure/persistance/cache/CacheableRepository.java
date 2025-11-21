package es.upsa.ssi.trabajo1.personas.infraestructure.persistance.cache;

import es.upsa.ssi.trabajo1.domain.entities.Persona;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import es.upsa.ssi.trabajo1.personas.domain.Repository;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.CacheResult;
import io.quarkus.cache.redis.runtime.RedisCache;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.PostConstruct;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;


@Cacheable
@Decorator
@Dependent
public abstract class CacheableRepository implements Repository {

    @Inject
    @Any
    @Delegate
    Repository repository;

    @Inject
    @CacheName("personas")
    Cache personasCache;

    RedisCache redisCache;

    @PostConstruct
    public void init() {
        redisCache = personasCache.as(RedisCache.class);
    }


    @Override
    public Persona findPersonasByDni(String id) throws AppException {
        Persona persona = redisCache.getOrNull(id,Persona.class).await().indefinitely();
        if(persona == null) {
            persona = repository.findPersonasByDni(id);
            redisCache.put(id,persona).await().indefinitely();
        }
        return persona;
    }

    @Override
    public Persona addPersona(Persona persona) throws AppException {

        Persona  addPersona = repository.addPersona(persona);
        redisCache.put(addPersona.dni(),addPersona).await().indefinitely();
        return addPersona;
    }

    @Override
    public Persona updatePersona(Persona persona) throws AppException {
        Persona  updatePersona = repository.updatePersona(persona);
        redisCache.put(updatePersona.dni(),updatePersona).await().indefinitely();
        return repository.updatePersona(persona);
    }

    @Override
    public boolean deletePersonaByDni(String dni) throws AppException {

        repository.deletePersonaByDni(dni);
        redisCache.invalidate(dni).await().indefinitely();
        return true;
    }
}
