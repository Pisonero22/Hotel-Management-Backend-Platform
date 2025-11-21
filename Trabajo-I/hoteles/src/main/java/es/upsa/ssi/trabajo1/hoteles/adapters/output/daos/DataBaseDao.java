package es.upsa.ssi.trabajo1.hoteles.adapters.output.daos;

import es.upsa.ssi.trabajo1.domain.entities.Hotel;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;

import java.util.List;
import java.util.Optional;

public interface DataBaseDao
{
    List<Hotel> findAllHoteles() throws AppException;

    Hotel findHotelById(String id) throws AppException;

    Hotel insertarHotel(Hotel hotel) throws AppException;
    Hotel updateHotel(Hotel hotel) throws AppException;

    String deleteHotelById(String id) throws AppException;


}
