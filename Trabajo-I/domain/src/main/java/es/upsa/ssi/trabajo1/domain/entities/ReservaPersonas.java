package es.upsa.ssi.trabajo1.domain.entities;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
@With
public class ReservaPersonas {


    private String dniTitular;
    private String nombreTitular;
    @Singular("persona")
    private List<Persona> personas;

}
