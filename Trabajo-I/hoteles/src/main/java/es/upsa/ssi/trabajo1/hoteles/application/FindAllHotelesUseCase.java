package es.upsa.ssi.trabajo1.hoteles.application;

import es.upsa.ssi.trabajo1.domain.entities.Hotel;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;

import java.util.List;

public interface FindAllHotelesUseCase {
    List<Hotel> execute() throws AppException;
}
