package es.upsa.ssi.trabajo1.hoteles.infraestructure.persistance.cache;


import es.upsa.ssi.trabajo1.domain.entities.Hotel;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import es.upsa.ssi.trabajo1.hoteles.domain.Repository;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.redis.runtime.RedisCache;
import jakarta.annotation.PostConstruct;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;

import java.util.List;

@Dependent
@Cacheable
@Decorator
public abstract class CacheableHoteles implements Repository {

    @Delegate
    @Any
    @Inject
    Repository repository;


    @Inject
    @CacheName("hoteles")
    Cache hotelesCache;
    RedisCache redisCache;

    @PostConstruct
    public void init() {
         redisCache = hotelesCache.as(RedisCache.class);
    }

    @Override
    public Hotel findHotelById(String id) throws AppException {
        Hotel hotel = redisCache.getOrNull(id, Hotel.class).await().indefinitely();
        if (hotel == null) {
            hotel = repository.findHotelById(id);
            redisCache.put(id, hotel).await().indefinitely();

        }
        return hotel;
    }

    @Override
    public Hotel save(Hotel hotel) throws AppException {
        if(hotel.id()==null){
            Hotel newHotel = repository.save(hotel);
            redisCache.put(newHotel.id(), newHotel).await().indefinitely();
            return newHotel;

        }else{
            Hotel updatedHotel = repository.save(hotel);
            redisCache.put(updatedHotel.id(),updatedHotel).await().indefinitely();
            return updatedHotel;
        }
    }

    @Override
    public String deleteHotelById(String id) throws AppException {
        String deleted = repository.deleteHotelById(id);
        redisCache.invalidate(id).await().indefinitely();
        return deleted;
    }

}
