package es.upsa.ssi.trabajo1.reserva.adapters.input.rest.providers.paramconverter;

import jakarta.ws.rs.ext.ParamConverter;

import java.util.List;
import java.util.stream.Collectors;

public class ListParamConverter implements ParamConverter<List<String>> {

    private String delimiter;

    public ListParamConverter(String delimiter) {
        this.delimiter = delimiter;
    }
    @Override
    public List<String> fromString(String data) {
        if(data.isEmpty()) return List.of();
        String[] tokens = data.split(",");
        return List.of(tokens);
    }
    @Override
    public String toString(List<String> data) {
        return data.stream().collect(Collectors.joining(delimiter));
    }
}
