package es.upsa.ssi.trabajo1.application.mappers;

import es.upsa.ssi.trabajo1.application.dtos.HotelDto;
import es.upsa.ssi.trabajo1.application.dtos.PersonaDto;
import es.upsa.ssi.trabajo1.application.dtos.ReservaDto;
import es.upsa.ssi.trabajo1.domain.entities.Hotel;
import es.upsa.ssi.trabajo1.domain.entities.Persona;
import es.upsa.ssi.trabajo1.domain.entities.Reserva;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.function.Function;

@ApplicationScoped
public class Mappers {

    public Function<HotelDto, Hotel> mapToHotel = hotelDto -> Hotel.builder()
                                                                    .withNombre(hotelDto.getNombre())
                                                                    .withLocalizacion(hotelDto.getLocalizacion())
                                                                    .withEstrellas(hotelDto.getEstrellas())
                                                                    .withPrecioNoche(hotelDto.getPrecioNoche())
                                                                    .build();

    public Function<Hotel, HotelDto> mapToHotelDto = hotel -> HotelDto.builder()
                                                                    .withNombre(hotel.nombre())
                                                                    .withLocalizacion(hotel.localizacion())
                                                                    .withEstrellas(hotel.estrellas())
                                                                    .withPrecioNoche(hotel.precioNoche())
                                                                    .build();

    public Function<Reserva, ReservaDto> mapToReservaDto = reserva -> ReservaDto.builder()
                                                                                .withDni(reserva.dni())
                                                                                .withId_hotel(reserva.id_hotel())
                                                                                .withDNITitular(reserva.DNITitular())
                                                                                .withFechaEntrada(reserva.fechaEntrada())
                                                                                .withFechaSalida(reserva.fechaSalida())
                                                                                .withPrecioTotal(reserva.precioTotal())
                                                                                .build();
    public Function<ReservaDto, Reserva> mapToReserva = reservaDto -> Reserva.builder()
                                                                                .withDni(reservaDto.getDni())
                                                                                .withId_hotel(reservaDto.getId_hotel())
                                                                                .withDNITitular(reservaDto.getDNITitular())
                                                                                .withFechaEntrada(reservaDto.getFechaEntrada())
                                                                                .withFechaSalida(reservaDto.getFechaSalida())
                                                                                .withPrecioTotal(reservaDto.getPrecioTotal())
                                                                                .build();


    public Function<Persona, PersonaDto> mapToPersonaDto = persona -> PersonaDto.builder()
                                                                                .withNombre(persona.nombre())
                                                                                .withTelefono(persona.telefono())
                                                                                .withFechaNacimiento(persona.fechaNacimiento())
                                                                                .build();





}
