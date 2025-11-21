package es.upsa.ssi.trabajo1.personas.adapters.input.rest.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class ExceptionDto {
    private String message;
}
