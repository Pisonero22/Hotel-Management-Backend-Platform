package es.upsa.ssi.trabajo1.reserva.adapters.input.rest.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservaDto {
    private String id;
    private String dni;
    private String id_hotel;
    private String DNITitular;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private double precioTotal;
}
