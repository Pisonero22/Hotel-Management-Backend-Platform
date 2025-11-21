package es.upsa.ssi.trabajo1.reserva.adapters.output;

import es.upsa.ssi.trabajo1.domain.entities.Reserva;
import es.upsa.ssi.trabajo1.domain.entities.ReservasConjuntas;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;

import java.util.List;
import java.util.Optional;

public interface DataBaseDao {


    ///////////
    ///PERSONAS
    ///////////

    List<Reserva> findReservaByDni(String dni) throws AppException;
    List<Reserva> findReservasByDniTitular(String dniTitular) throws AppException;

    ///////////
    ///RESERVAS
    ///////////
    List<Reserva> findAllReservas() throws AppException;
    Reserva findReservaById(String id) throws AppException;
    List<Reserva> findReservasConjuntasByIds(List<String> ids) throws AppException;
    Reserva addReserva(Reserva reserva) throws AppException;

    Reserva updateReserva(Reserva reserva) throws AppException;

    void deleteReserva(String id) throws AppException;

    //////////
    ///HOTELES
    //////////
    List<ReservasConjuntas> findReservasHotelByIdHotelConjuntas(String idHotel) throws AppException;


}
