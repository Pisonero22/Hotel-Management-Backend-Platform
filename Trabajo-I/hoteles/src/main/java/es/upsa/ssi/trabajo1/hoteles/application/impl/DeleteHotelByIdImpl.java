package es.upsa.ssi.trabajo1.hoteles.application.impl;

import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import es.upsa.ssi.trabajo1.hoteles.application.DeleteHotelById;
import es.upsa.ssi.trabajo1.hoteles.domain.Repository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DeleteHotelByIdImpl implements DeleteHotelById {

    @Inject
    Repository repository;

    @Override
    public String execute(String id) throws AppException {
        return repository.deleteHotelById(id);
    }

}
