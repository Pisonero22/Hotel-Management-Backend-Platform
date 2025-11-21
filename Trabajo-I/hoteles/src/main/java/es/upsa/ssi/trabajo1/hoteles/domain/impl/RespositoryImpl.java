package es.upsa.ssi.trabajo1.hoteles.domain.impl;

import es.upsa.ssi.trabajo1.domain.entities.Hotel;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import es.upsa.ssi.trabajo1.hoteles.adapters.output.daos.DataBaseDao;
import es.upsa.ssi.trabajo1.hoteles.domain.Repository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class RespositoryImpl implements Repository {

    @Inject
    DataBaseDao dao;

    @Override
    public List<Hotel> findAllHoteles() throws AppException {
        return dao.findAllHoteles();
    }

    @Override
    public Hotel findHotelById(String id) throws AppException {
        return dao.findHotelById(id);
    }

    @Override
    public Hotel save(Hotel hotel) throws AppException {
        if(hotel.id() == null){
            return dao.insertarHotel(hotel);
        }else{
            return dao.updateHotel(hotel);
        }
    }

    @Override
    public String deleteHotelById(String id) throws AppException {
        return dao.deleteHotelById(id);
    }

}
