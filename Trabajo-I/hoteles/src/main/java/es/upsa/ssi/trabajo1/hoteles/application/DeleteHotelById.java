package es.upsa.ssi.trabajo1.hoteles.application;

import es.upsa.ssi.trabajo1.domain.exceptions.AppException;

public interface DeleteHotelById {
    String execute (String id) throws AppException;
}
