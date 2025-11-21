package es.upsa.ssi.trabajo1.domain.entities;


import lombok.Builder;
import lombok.With;

@Builder(setterPrefix = "with")
@With
public record Hotel (  String id,
                       String nombre,
                       String localizacion,
                       int estrellas,
                       double precioNoche
                    )
{}
