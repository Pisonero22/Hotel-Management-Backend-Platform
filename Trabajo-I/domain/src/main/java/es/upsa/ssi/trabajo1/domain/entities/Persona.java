package es.upsa.ssi.trabajo1.domain.entities;

import lombok.Builder;
import lombok.With;

import java.time.LocalDate;

@With
@Builder(setterPrefix = "with")
public record Persona(
        String dni,
        String nombre,
        LocalDate fechaNacimiento,
        String telefono
)
{}
