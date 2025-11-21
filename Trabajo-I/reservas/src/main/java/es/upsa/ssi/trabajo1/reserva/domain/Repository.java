package es.upsa.ssi.trabajo1.reserva.domain;

import es.upsa.ssi.trabajo1.domain.entities.Reserva;
import es.upsa.ssi.trabajo1.domain.entities.ReservasConjuntas;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;

import java.util.List;
import java.util.Optional;

public interface Repository {

    //////////
    //RESERVAS
    //////////

    List<Reserva> findAllReservas() throws AppException;
    Reserva findReservaBYId(String id) throws AppException;
    Reserva save(Reserva reserva) throws AppException;
    void deleteReserva(String id) throws AppException;

    ///////////
    //Hoteles
    ///////////
    List<ReservasConjuntas> findReservasHotelByIdHotelConjuntas(String idHotel) throws AppException;

    ////////////
    //Personas
    ////////////
    List<Reserva> findReservaByDni(String dni) throws AppException;
    List<Reserva> findReservasByDniTitular(String dniTitular) throws AppException;


    List<Reserva> findReservasConjuntasByIds(List<String> ids) throws AppException;



}
