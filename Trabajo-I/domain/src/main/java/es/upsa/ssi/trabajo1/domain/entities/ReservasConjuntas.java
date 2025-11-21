package es.upsa.ssi.trabajo1.domain.entities;


import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
@With
public class ReservasConjuntas {

    private String idHotel;
    @Singular("Reserva")
    private List<Reserva> reservas;

}
