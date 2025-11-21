package es.upsa.ssi.trabajo1.domain.impl;

import es.upsa.ssi.trabajo1.adapters.output.HotelesServiceClient;
import es.upsa.ssi.trabajo1.adapters.output.PersonasServiceClient;
import es.upsa.ssi.trabajo1.adapters.output.ReservasServiceClient;
import es.upsa.ssi.trabajo1.application.dtos.HotelDto;
import es.upsa.ssi.trabajo1.application.dtos.PersonaDto;
import es.upsa.ssi.trabajo1.application.dtos.ReservaDto;
import es.upsa.ssi.trabajo1.domain.Repository;
import es.upsa.ssi.trabajo1.domain.entities.*;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import es.upsa.ssi.trabajo1.domain.exceptions.EntityNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RepositoryImpl implements Repository {


    @Inject
    @RestClient
    ReservasServiceClient reservasServiceClient;

    @Inject
    @RestClient
    PersonasServiceClient personasServiceClient;

    @Inject
    @RestClient
    HotelesServiceClient hotelesServiceClient;

    @Override
    public List<Hotel> findAllHoteles() throws AppException {

        return hotelesServiceClient.requestGetHoteles();
    }

    @Override
    public Optional<Hotel> findHotelById(String id) throws AppException {
        try {
            return Optional.of(hotelesServiceClient.requestGetHotel(id));
        } catch (EntityNotFoundException e){
            return Optional.empty();
        }
    }



    @Override
    public Hotel addHotel(Hotel hotel) throws AppException {


        HotelDto hotelDto = HotelDto.builder()
                .withNombre(hotel.nombre())
                .withLocalizacion(hotel.localizacion())
                .withEstrellas(hotel.estrellas())
                .withPrecioNoche(hotel.precioNoche())
                .build();

        Hotel hotel1 = hotelesServiceClient.addHotel(hotelDto);

        return hotel1;

    }

    @Override
    public Hotel updateHotel(Hotel hotel) throws AppException {
        HotelDto hotelDto = HotelDto.builder()
                .withNombre(hotel.nombre())
                .withLocalizacion(hotel.localizacion())
                .withEstrellas(hotel.estrellas())
                .withPrecioNoche(hotel.precioNoche())
                .build();

        return hotelesServiceClient.updateHotel(hotel.id(),hotelDto);
    }


    @Override
    public String deleteHotel(String id) throws AppException {

        try {
            return hotelesServiceClient.deleteHotel(id);
        } catch (EntityNotFoundException e){
            return "No se ha encontrado el hotel con id " + id;
        } catch (AppException e){
            return "El hotel tiene reservas asociadas";
        }
    }



    @Override
    public List<Persona> findAllPersonas() throws AppException{
        return personasServiceClient.findAllPersonas();
    }

    @Override
    public Persona findPersonasByDni(String dni) throws AppException {

        try {
            return Optional.of(personasServiceClient.findPersonaByDni(dni)).get();
        } catch (EntityNotFoundException e){
            return Persona.builder().withDni(null).build();
        }
    }

    @Override
    public Optional<Reserva> findReservaById(String idReserva) throws AppException {

        try{
            return Optional.of(reservasServiceClient.getReserva(idReserva));
        }catch (EntityNotFoundException e){
            return Optional.empty();
        }
    }

    @Override
    public HotelReservasFull findReservasHotelById(String id) throws AppException {
        try {
            Optional<Hotel> hotelById = findHotelById(id);
            List<ReservasConjuntas> reservasByIdHotel = reservasServiceClient.findReservasHotelByIdHotelConjuntas(id);
            if (hotelById.isEmpty()) return HotelReservasFull.builder().withHotel(Hotel.builder().build()).build();
            if (reservasByIdHotel.get(0).getIdHotel() == null)
                return HotelReservasFull.builder().withHotel(hotelById.get()).withReservas(List.of()).build();

            HotelReservasFull hotelReservasFull = HotelReservasFull.builder()
                    .withHotel(hotelById.get())
                    .withReservas(reservasByIdHotel)
                    .build();

            return hotelReservasFull;
        }catch (EntityNotFoundException e){
            return HotelReservasFull.builder().withHotel(null).withReservas(List.of()).build();
        }
    }

    @Override
    public Reserva addReserva(Reserva reserva) throws AppException {

            ReservaDto reservaDto1 = ReservaDto.builder()
                    .withDni(reserva.dni())
                    .withId_hotel(reserva.id_hotel())
                    .withDNITitular(reserva.DNITitular())
                    .withFechaEntrada(reserva.fechaEntrada())
                    .withFechaSalida(reserva.fechaSalida())
                    .withPrecioTotal(reserva.precioTotal())
                    .build();

            return reservasServiceClient.addReserva(reservaDto1);
    }
    @Override
    public Reserva updateReserva(Reserva reserva) throws AppException {
            ReservaDto reservaDto1 = ReservaDto.builder()
                    .withDni(reserva.dni())
                    .withId_hotel(reserva.id_hotel())
                    .withDNITitular(reserva.DNITitular())
                    .withFechaEntrada(reserva.fechaEntrada())
                    .withFechaSalida(reserva.fechaSalida())
                    .withPrecioTotal(reserva.precioTotal())
                    .build();

            return reservasServiceClient.updateReserva(reserva.id(), reservaDto1);

    }

    @Override
    public String deleteReserva(String id) throws AppException {
        try{
            return reservasServiceClient.deleteReserva(id);
        }catch (EntityNotFoundException e){

            return "No se ha encontrado el reserva con id " + id;
        }
    }

    @Override
    public Persona updatePersonaByDni(Persona persona) throws AppException {

        PersonaDto peronaDto = PersonaDto.builder()
                .withNombre(persona.nombre())
                .withTelefono(persona.telefono())
                .withFechaNacimiento(persona.fechaNacimiento())
                .build();

        return personasServiceClient.updatePersona(persona.dni(),peronaDto);
    }

    @Override
    public Persona addPersona(Persona persona) throws AppException {
        return personasServiceClient.createPersona(persona);
    }

    @Override
    public String deletePersonaByDni(String dni) throws AppException {
        try {
            return personasServiceClient.deletePersona(dni);
        } catch (EntityNotFoundException e){
            return "No se encuntra la persona con el dni " + dni;
        }catch (AppException e){
            return "El persona con el dni " + dni + " tiene reservas asociadas";
        }
    }


}
