package es.upsa.ssi.trabajo1.domain.entities;

import lombok.*;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class HotelReservasFull {
    private Hotel hotel;
    @Singular("Reserva")
    private List<ReservasConjuntas> reservas;
}
