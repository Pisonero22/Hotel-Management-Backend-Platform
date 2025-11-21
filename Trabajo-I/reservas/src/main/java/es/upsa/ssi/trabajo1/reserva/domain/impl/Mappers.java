package es.upsa.ssi.trabajo1.reserva.domain.impl;

import es.upsa.ssi.trabajo1.domain.entities.Persona;
import es.upsa.ssi.trabajo1.domain.entities.Reserva;

import java.util.function.Function;

public class Mappers {

    static Function<Reserva, Persona> toPersona = reserva -> Persona.builder()
            .withDni(reserva.dni())
            .build();

}
