package es.upsa.ssi.trabajo1.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class HotelDto {
    private String nombre;
    private int estrellas;
    private String localizacion;
    private double precioNoche;
}
