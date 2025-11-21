package es.upsa.ssi.trabajo1.domain;

import es.upsa.ssi.trabajo1.domain.entities.Hotel;
import es.upsa.ssi.trabajo1.domain.entities.HotelReservasFull;
import es.upsa.ssi.trabajo1.domain.entities.Persona;
import es.upsa.ssi.trabajo1.domain.entities.Reserva;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;

import java.util.List;
import java.util.Optional;

public interface Repository {

    /////////
    //HOTELES
    /////////
    List<Hotel> findAllHoteles() throws AppException;
    Optional<Hotel> findHotelById(String id) throws AppException;
    Hotel addHotel(Hotel hotel) throws AppException;
    Hotel updateHotel(Hotel hotel) throws AppException;
    String deleteHotel(String id) throws AppException;


    //////////
    //PERSONAS
    //////////

    List<Persona> findAllPersonas()throws AppException;
    Persona findPersonasByDni(String dni) throws AppException;
    Persona updatePersonaByDni(Persona persona) throws AppException;
    Persona addPersona(Persona persona) throws AppException;
    String deletePersonaByDni(String dni) throws AppException;

    //////////
    //RESERVAS
    //////////
    Optional<Reserva> findReservaById(String idReserva) throws AppException;
    HotelReservasFull findReservasHotelById(String id) throws AppException;
    Reserva addReserva(Reserva reserva) throws AppException;
    Reserva updateReserva(Reserva reserva) throws AppException;
    String deleteReserva(String id) throws AppException;


}
