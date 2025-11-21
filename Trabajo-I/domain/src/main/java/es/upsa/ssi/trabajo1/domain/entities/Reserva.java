package es.upsa.ssi.trabajo1.domain.entities;

import lombok.Builder;
import lombok.With;

import java.time.LocalDate;

@With
@Builder(setterPrefix = "with")
public record Reserva(
        String id,
        String dni,
        String id_hotel,
        String DNITitular,
        LocalDate fechaEntrada,
        LocalDate fechaSalida,
        double precioTotal
)
{}
