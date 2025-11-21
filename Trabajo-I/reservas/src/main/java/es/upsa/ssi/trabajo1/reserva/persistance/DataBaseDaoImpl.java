package es.upsa.ssi.trabajo1.reserva.persistance;

import es.upsa.ssi.trabajo1.domain.entities.Reserva;
import es.upsa.ssi.trabajo1.domain.entities.ReservasConjuntas;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import es.upsa.ssi.trabajo1.domain.exceptions.ConstraintViolationException;
import es.upsa.ssi.trabajo1.domain.exceptions.EntityNotFoundException;
import es.upsa.ssi.trabajo1.domain.exceptions.NonControledSQLException;
import es.upsa.ssi.trabajo1.reserva.adapters.output.DataBaseDao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class DataBaseDaoImpl implements DataBaseDao {

    @Inject
    DataSource dataSource;

    ///////////
    ///PERSONAS
    ///////////

    @Override
    public List<Reserva> findReservaByDni(String dni) throws AppException {

        final String SQL = """
                          SELECT r.ID,r.DNI,r.ID_HOTEL,r.TITULAR_DNI,r.FECHA_ENTRADA,r.FECHA_SALIDA, r.PRECIO
                            FROM RESERVAS r
                            WHERE r.DNI = ? 
                           """;
        List<Reserva> reservas = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ){
            preparedStatement.setString(1, dni);
            try(ResultSet resultSet = preparedStatement.executeQuery();
            ){

                while (resultSet.next()) {
                    reservas.add(
                            Reserva.builder()
                                    .withId(resultSet.getString(1))
                                    .withDni(resultSet.getString(2))
                                    .withId_hotel(resultSet.getString(3))
                                    .withDNITitular(resultSet.getString(4))
                                    .withFechaEntrada(resultSet.getDate(5).toLocalDate())
                                    .withFechaSalida(resultSet.getDate(6).toLocalDate())
                                    .withPrecioTotal(resultSet.getDouble(7))
                                    .build()
                    );
                }

            }

        }catch (SQLException sqlException){
            throw manager(sqlException);
        }
        return reservas;
    }
    @Override
    public List<Reserva> findReservasByDniTitular(String dniTitular) throws AppException {
        final String SQL = """
                           SELECT r.ID,r.DNI,r.ID_HOTEL,r.TITULAR_DNI,r.FECHA_ENTRADA,r.FECHA_SALIDA, r.PRECIO
                           FROM RESERVAS R
                           WHERE r.TITULAR_DNI = ?                                   
                           """;
        List<Reserva> reservas = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ){
            preparedStatement.setString(1, dniTitular);
            try(ResultSet resultSet = preparedStatement.executeQuery();
            ){
                while (resultSet.next()) {
                    reservas.add(
                            Reserva.builder()
                                    .withId(resultSet.getString(1))
                                    .withDni(resultSet.getString(2))
                                    .withId_hotel(resultSet.getString(3))
                                    .withDNITitular(resultSet.getString(4))
                                    .withFechaEntrada(resultSet.getDate(5).toLocalDate())
                                    .withFechaSalida(resultSet.getDate(6).toLocalDate())
                                    .withPrecioTotal(resultSet.getDouble(7))
                                    .build()
                    );
                }
            }

        }catch (SQLException sqlException){
            throw manager(sqlException);
        }

        return reservas;
    }

    //////////
    ///HOTELES
    //////////
    @Override
    public List<ReservasConjuntas> findReservasHotelByIdHotelConjuntas(String idHotel) throws AppException {
        final String SQL = """
                           SELECT r.ID,r.DNI,r.ID_HOTEL,r.TITULAR_DNI,r.FECHA_ENTRADA,r.FECHA_SALIDA, r.PRECIO
                           FROM RESERVAS R
                           WHERE r.ID_HOTEL = ?
                           ORDER BY r.TITULAR_DNI DESC
                           """;

        List<ReservasConjuntas> reservasConjuntas = new ArrayList<>();
        int i=0;
        int k=0;
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ){
            preparedStatement.setString(1, idHotel);
            try(ResultSet resultSet = preparedStatement.executeQuery();
            ){
                while (resultSet.next()) {
                    if(reservasConjuntas.isEmpty()){

                        reservasConjuntas.add(ReservasConjuntas.builder()
                                .withIdHotel(idHotel)
                                .withReserva(Reserva.builder()
                                        .withId(resultSet.getString(1))
                                        .withDni(resultSet.getString(2))
                                        .withId_hotel(resultSet.getString(3))
                                        .withDNITitular(resultSet.getString(4))
                                        .withFechaEntrada(resultSet.getDate(5).toLocalDate())
                                        .withFechaSalida(resultSet.getDate(6).toLocalDate())
                                        .withPrecioTotal(resultSet.getDouble(7))
                                        .build())
                                .build());

                    }else {

                        if( (resultSet.getString(4).equals(reservasConjuntas.get(i).getReservas().getLast().DNITitular()))
                                && ( resultSet.getDouble(7) == reservasConjuntas.get(i).getReservas().getLast().precioTotal() )
                                &&  (  resultSet.getDate(5).toLocalDate().equals(reservasConjuntas.get(i).getReservas().getLast().fechaEntrada()) )
                                &&  (  resultSet.getDate(6).toLocalDate().equals(reservasConjuntas.get(i).getReservas().getLast().fechaSalida()) )

                        ){
                            List<Reserva> reservas1 = new ArrayList<>(reservasConjuntas.get(k).getReservas());
                            reservas1.add(Reserva.builder()
                                    .withId(resultSet.getString(1))
                                    .withDni(resultSet.getString(2))
                                    .withId_hotel(resultSet.getString(3))
                                    .withDNITitular(resultSet.getString(4))
                                    .withFechaEntrada(resultSet.getDate(5).toLocalDate())
                                    .withFechaSalida(resultSet.getDate(6).toLocalDate())
                                    .withPrecioTotal(resultSet.getDouble(7))
                                    .build());
                            reservasConjuntas.get(k).setReservas(reservas1);

                        }else{
                            i++;
                            reservasConjuntas.add(ReservasConjuntas.builder()
                                    .withIdHotel(idHotel)
                                    .withReserva(Reserva.builder()
                                            .withId(resultSet.getString(1))
                                            .withDni(resultSet.getString(2))
                                            .withId_hotel(resultSet.getString(3))
                                            .withDNITitular(resultSet.getString(4))
                                            .withFechaEntrada(resultSet.getDate(5).toLocalDate())
                                            .withFechaSalida(resultSet.getDate(6).toLocalDate())
                                            .withPrecioTotal(resultSet.getDouble(7))
                                            .build())
                                    .build());
                            k++;
                        }


                    }


                }
            }

        }catch (SQLException sqlException){
            throw manager(sqlException);
        }
        return reservasConjuntas;
    }


    ///////////
    ///RESERVAS
    ///////////

    @Override
    public List<Reserva> findAllReservas() throws AppException {
        final String SQL = """
                           SELECT r.ID,r.DNI,r.ID_HOTEL,r.TITULAR_DNI,r.FECHA_ENTRADA,r.FECHA_SALIDA, r.PRECIO
                           FROM RESERVAS r
                           """;

        List<Reserva> reservas = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL)
        ){
            while (resultSet.next()) {
                reservas.add(
                        Reserva.builder()
                                .withId(resultSet.getString(1))
                                .withDni(resultSet.getString(2))
                                .withId_hotel(resultSet.getString(3))
                                .withDNITitular(resultSet.getString(4))
                                .withFechaEntrada(resultSet.getDate(5).toLocalDate())
                                .withFechaSalida(resultSet.getDate(6).toLocalDate())
                                .withPrecioTotal(resultSet.getDouble(7))
                                .build()
                        );

            }

        }catch (SQLException sqlException){
            throw manager(sqlException);
        }

        return reservas;

    }
    @Override
    public Reserva findReservaById(String id) throws AppException {
        final String SQL = """
                          SELECT r.ID,r.DNI,r.ID_HOTEL,r.TITULAR_DNI,r.FECHA_ENTRADA,r.FECHA_SALIDA, r.PRECIO
                          FROM RESERVAS r
                          WHERE r.ID = ?
                          """;
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ){
            preparedStatement.setString(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery();
            ){
                if(!resultSet.next()) return Reserva.builder().build();

                return Reserva.builder()
                            .withId(resultSet.getString(1))
                            .withDni(resultSet.getString(2))
                            .withId_hotel(resultSet.getString(3))
                            .withDNITitular(resultSet.getString(4))
                            .withFechaEntrada(resultSet.getDate(5).toLocalDate())
                            .withFechaSalida(resultSet.getDate(6).toLocalDate())
                            .withPrecioTotal(resultSet.getDouble(7))
                            .build();

            }

        }catch (SQLException sqlException){
            throw manager(sqlException);
        }

    }
    @Override
    public List<Reserva> findReservasConjuntasByIds(List<String> ids) throws AppException {

        final String SQL = """
                           SELECT r.ID,r.DNI,r.ID_HOTEL,r.TITULAR_DNI,r.FECHA_ENTRADA,r.FECHA_SALIDA, r.PRECIO
                           FROM RESERVAS R
                           WHERE r.ID = ?
                           """;
        List<Reserva> reservasTitulares = new ArrayList<>();

        try(Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
        ){

            for(String id : ids){
                ps.clearParameters();
                ps.setString(1, id);
                try(ResultSet resultSet = ps.executeQuery();) {
                    if(resultSet.next()) {
                        reservasTitulares.add(Reserva.builder()
                                .withId(resultSet.getString(1))
                                .withDni(resultSet.getString(2))
                                .withId_hotel(resultSet.getString(3))
                                .withDNITitular(resultSet.getString(4))
                                .withFechaEntrada(resultSet.getDate(5).toLocalDate())
                                .withFechaSalida(resultSet.getDate(6).toLocalDate())
                                .withPrecioTotal(resultSet.getDouble(7))
                                .build());
                    }
                }
            }


        }catch (SQLException sqlException){
            throw manager(sqlException);
        }

        return reservasTitulares;
    }

    @Override
    public Reserva addReserva(Reserva reserva) throws AppException {
        final String SQL = """
                           INSERT INTO RESERVAS(ID       , DNI, ID_HOTEL, TITULAR_DNI, FECHA_ENTRADA, FECHA_SALIDA, PRECIO)
                           VALUES(nextval('seq_reservas'),?   ,?        ,?           ,?             ,?            ,?      )
                           """;

        String[] tokens = {"id"};
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL,tokens);
        ){
            preparedStatement.setString(1,reserva.dni());
            preparedStatement.setString(2,reserva.id_hotel());
            preparedStatement.setString(3,reserva.DNITitular());
            preparedStatement.setDate(4,Date.valueOf(reserva.fechaEntrada()));
            preparedStatement.setDate(5,Date.valueOf(reserva.fechaSalida()));
            preparedStatement.setDouble(6,reserva.precioTotal());
            preparedStatement.executeUpdate();
            try(ResultSet resultSet = preparedStatement.getGeneratedKeys();
            ){
                resultSet.next();
                return reserva.withId(resultSet.getString(1));
            }

        }catch (SQLException sqlException){
            return Reserva.builder().withDNITitular(sqlException.getMessage()).build();

        }
    }

    @Override
    public Reserva updateReserva(Reserva reserva) throws AppException {
        final String SQL = """
                           UPDATE RESERVAS
                               SET DNI = ?,
                               ID_HOTEL = ?,
                               TITULAR_DNI = ?,
                               FECHA_ENTRADA = ?,
                               FECHA_SALIDA = ?,
                               PRECIO = ?
                           WHERE ID = ?                           
                           """;

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ){
            preparedStatement.setString(1,reserva.dni());
            preparedStatement.setString(2,reserva.id_hotel());
            preparedStatement.setString(3,reserva.DNITitular());
            preparedStatement.setDate(4,Date.valueOf(reserva.fechaEntrada()));
            preparedStatement.setDate(5,Date.valueOf(reserva.fechaSalida()));
            preparedStatement.setDouble(6,reserva.precioTotal());
            preparedStatement.setString(7,reserva.id());
            int i = preparedStatement.executeUpdate();
            if(i==0) throw new EntityNotFoundException("La reserva con el id -> " + reserva.id() + " no exite");
            return reserva;

        }catch (SQLException sqlException){
            throw manager(sqlException);
        }

    }

    @Override
    public void deleteReserva(String id) throws AppException {

        final String SQL = """
                           DELETE
                           FROM RESERVAS
                           WHERE ID = ?
                           """;


        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ){
            preparedStatement.setString(1,id);
            int i = preparedStatement.executeUpdate();
            if(i==0) throw new EntityNotFoundException("La reserva con el id -> " + id + " no existe");

        }catch (SQLException sqlException){
            throw manager(sqlException);
        }
    }


    public AppException manager(SQLException sqlException) throws AppException{
        String mensaje = sqlException.getMessage();

        if(mensaje.contains("NN_RESERVAS_PERSONAS_DNI")) return new ConstraintViolationException("No puede estar el dni vacio");
        if(mensaje.contains("NN_RESERVAS_HOTELES_ID")) return new ConstraintViolationException("No puede estar el id del hotel vacio");
        if(mensaje.contains("NN_RESERVAS_TITULAR")) return new ConstraintViolationException("No puede estar el dni del titular vacio");
        if(mensaje.contains("NN_RESERVAS_FECHA_ENTRADA")) return new ConstraintViolationException("No puede estar la fecha de entrada vacia");
        if(mensaje.contains("NN_RESERVAS_FECHA_SALIDA")) return new ConstraintViolationException("No puede estar la fecha de salida vacia");
        if(mensaje.contains("CHK_FECHA_ENTRADA_ANTES_SALIDA")) return new ConstraintViolationException("La fecha de entrada no puede ser posterior a la de salida");
        if(mensaje.contains("NN_RESERVAS_PRECIO")) return new ConstraintViolationException("El precio no puede estar vacio");
        if(mensaje.contains("FK_RESERVAS_HOTELES")) return new ConstraintViolationException("El hotel con ese id no existe");
        if(mensaje.contains("FK_RESERVAS_PERSONAS")) return new ConstraintViolationException("El DNI no esta registrado");
        if(mensaje.contains("FK_RESERVAS_TITULAR")) return new ConstraintViolationException("El DNI del titular no esta registrado");
        return new NonControledSQLException(sqlException);
    }
}
