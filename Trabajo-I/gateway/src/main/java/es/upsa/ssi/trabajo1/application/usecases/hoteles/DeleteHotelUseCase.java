package es.upsa.ssi.trabajo1.application.usecases.hoteles;

import es.upsa.ssi.trabajo1.domain.exceptions.AppException;

public interface DeleteHotelUseCase {
    String execute(String id) throws AppException;
}
