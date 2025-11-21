package es.upsa.ssi.trabajo1.personas.adapters.input.rest.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonaDto {
    private String nombre;
    private String telefono;
    private LocalDate fechaNacimiento;
}
