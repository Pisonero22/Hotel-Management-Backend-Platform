package es.upsa.ssi.trabajo1.hoteles.domain;

import es.upsa.ssi.trabajo1.domain.entities.Hotel;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;

import java.util.List;
import java.util.Optional;

public interface Repository {

    public List<Hotel> findAllHoteles () throws AppException;
    public Hotel findHotelById (String id) throws AppException;
    public Hotel save(Hotel hotel) throws AppException;
    String deleteHotelById(String id) throws AppException;


}
