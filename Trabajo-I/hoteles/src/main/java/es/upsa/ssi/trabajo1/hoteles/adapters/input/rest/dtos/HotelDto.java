package es.upsa.ssi.trabajo1.hoteles.adapters.input.rest.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class HotelDto
{
    private String nombre;
    private String localizacion;
    private int estrellas;
    private double precioNoche;
}
