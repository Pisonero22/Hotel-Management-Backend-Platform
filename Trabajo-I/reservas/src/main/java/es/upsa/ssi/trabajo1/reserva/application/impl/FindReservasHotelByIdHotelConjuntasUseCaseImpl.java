package es.upsa.ssi.trabajo1.reserva.application.impl;

import es.upsa.ssi.trabajo1.domain.entities.ReservasConjuntas;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import es.upsa.ssi.trabajo1.reserva.application.usecases.FindReservasHotelByIdHotelConjuntasUseCase;
import es.upsa.ssi.trabajo1.reserva.domain.Repository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class FindReservasHotelByIdHotelConjuntasUseCaseImpl implements FindReservasHotelByIdHotelConjuntasUseCase {

    @Inject
    Repository repository;

    @Override
    public List<ReservasConjuntas> execute(String idHotel) throws AppException {
        return repository.findReservasHotelByIdHotelConjuntas(idHotel);
    }
}
