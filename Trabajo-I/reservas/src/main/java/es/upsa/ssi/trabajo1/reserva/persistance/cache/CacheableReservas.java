package es.upsa.ssi.trabajo1.reserva.persistance.cache;

import es.upsa.ssi.trabajo1.domain.entities.Reserva;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import es.upsa.ssi.trabajo1.reserva.domain.Repository;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.redis.runtime.RedisCache;
import jakarta.annotation.PostConstruct;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;




@Cacheable
@Decorator
@Dependent
public abstract class CacheableReservas implements Repository {

    @Inject
    @Delegate
    @Any
    Repository repository;

    @Inject
    @CacheName("reservas")
    Cache reservasCache;

    RedisCache redisCache;


    @PostConstruct
    public void init() {
        redisCache = reservasCache.as(RedisCache.class);
    }

    @Override
    public Reserva findReservaBYId(String id) throws AppException {
        Reserva reserva = redisCache.getOrNull(id, Reserva.class).await().indefinitely();
        if (reserva == null) {
            reserva = repository.findReservaBYId(id);
            redisCache.put(id, reserva).await().indefinitely();
        }
        return reserva;
    }

    @Override
    public Reserva save(Reserva reserva) throws AppException {
        if(reserva.id()==null){
            Reserva newReserva = repository.save(reserva);
            redisCache.put(newReserva.id(), newReserva).await().indefinitely();
            return newReserva;
        }else {
            Reserva reservaUpdated = repository.save(reserva);
            redisCache.put(reservaUpdated.id(), reservaUpdated).await().indefinitely();
            return reservaUpdated;
        }
    }

    @Override
    public void deleteReserva(String id) throws AppException {
        repository.deleteReserva(id);
        redisCache.invalidate(id).await().indefinitely();
    }

}
