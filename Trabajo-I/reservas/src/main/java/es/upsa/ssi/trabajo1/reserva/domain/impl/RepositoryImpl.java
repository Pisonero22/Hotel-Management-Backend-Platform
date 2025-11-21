package es.upsa.ssi.trabajo1.reserva.domain.impl;

import es.upsa.ssi.trabajo1.domain.entities.Reserva;
import es.upsa.ssi.trabajo1.domain.entities.ReservasConjuntas;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import es.upsa.ssi.trabajo1.reserva.adapters.output.DataBaseDao;
import es.upsa.ssi.trabajo1.reserva.domain.Repository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RepositoryImpl implements Repository {

    @Inject
    DataBaseDao dao;

    @Override
    public List<Reserva> findAllReservas() throws AppException {
        return dao.findAllReservas();
    }
    @Override
    public Reserva findReservaBYId(String id) throws AppException {
        return dao.findReservaById(id);
    }
    @Override
    public List<Reserva> findReservasConjuntasByIds(List<String> ids) throws AppException {
        return dao.findReservasConjuntasByIds(ids);
    }

    @Override
    public Reserva save(Reserva reserva) throws AppException {
        if(reserva.id()==null){
            return dao.addReserva(reserva);
        }else {
            return dao.updateReserva(reserva);
        }

    }

    @Override
    public void deleteReserva(String id) throws AppException {
        dao.deleteReserva(id);
    }


    @Override
    public List<Reserva> findReservaByDni(String dni) throws AppException {
        return dao.findReservaByDni(dni);
    }
    @Override
    public List<Reserva> findReservasByDniTitular(String dniTitular) throws AppException {
        return dao.findReservasByDniTitular(dniTitular);
    }


    @Override
    public List<ReservasConjuntas> findReservasHotelByIdHotelConjuntas(String idHotel) throws AppException {
        return dao.findReservasHotelByIdHotelConjuntas(idHotel);
    }
}
