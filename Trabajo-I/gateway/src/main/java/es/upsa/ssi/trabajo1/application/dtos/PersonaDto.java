package es.upsa.ssi.trabajo1.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class PersonaDto {
    private String nombre;
    private String telefono;
    private LocalDate fechaNacimiento;

}
