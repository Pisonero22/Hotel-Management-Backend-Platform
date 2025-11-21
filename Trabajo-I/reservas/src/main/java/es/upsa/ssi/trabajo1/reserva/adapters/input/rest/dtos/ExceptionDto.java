package es.upsa.ssi.trabajo1.reserva.adapters.input.rest.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class ExceptionDto {
    private String message;
    private String status;
}


