package es.upsa.ssi.trabajo1.application.usecases.impl.reservas;

import es.upsa.ssi.trabajo1.application.usecases.reservas.FindReservasByIdHotelUseCase;
import es.upsa.ssi.trabajo1.domain.Repository;
import es.upsa.ssi.trabajo1.domain.entities.HotelReservasFull;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class FindReservasByIdHotelUseCaseImpl implements FindReservasByIdHotelUseCase {

    @Inject
    Repository repository;


    @Override
    public HotelReservasFull execute(String hotelId) throws AppException {
        return repository.findReservasHotelById(hotelId);
    }
}
