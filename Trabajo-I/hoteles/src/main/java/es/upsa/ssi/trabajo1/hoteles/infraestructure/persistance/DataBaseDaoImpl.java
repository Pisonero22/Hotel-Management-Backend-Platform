package es.upsa.ssi.trabajo1.hoteles.infraestructure.persistance;

import es.upsa.ssi.trabajo1.domain.entities.Hotel;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
import es.upsa.ssi.trabajo1.domain.exceptions.ConstraintViolationException;
import es.upsa.ssi.trabajo1.domain.exceptions.EntityNotFoundException;
import es.upsa.ssi.trabajo1.domain.exceptions.NonControledSQLException;
import es.upsa.ssi.trabajo1.hoteles.adapters.output.daos.DataBaseDao;
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

    @Override
    public List<Hotel> findAllHoteles() throws AppException {
        final String SQL = """
                           SELECT h.ID, h.NOMBRE, h.LOCALIZACION, h.ESTRELLAS, h.PRECIO_NOCHE
                           FROM HOTELES h
                           """;
        List<Hotel> hoteles = new ArrayList<>();

        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            )
        {
            while (resultSet.next()){
                hoteles.add(
                        Hotel.builder()
                            .withId(resultSet.getString(1))
                            .withNombre(resultSet.getString(2))
                            .withLocalizacion(resultSet.getString(3))
                            .withEstrellas(resultSet.getInt(4))
                            .withPrecioNoche(resultSet.getDouble(5))
                            .build()
                );
            }


        }catch (SQLException sqlException){
            throw managerException(sqlException);
        }

        return hoteles;
    }


    @Override
    public Hotel findHotelById(String id) throws AppException {
        final String SQL = """
                           SELECT h.ID, h.NOMBRE, h.LOCALIZACION, h.ESTRELLAS,h.PRECIO_NOCHE
                           FROM HOTELES h
                           WHERE h.ID = ?
                           """;
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ){
            preparedStatement.setString(1,id);
            try(ResultSet resultSet = preparedStatement.executeQuery();
            ){
                if(!resultSet.next()) return Hotel.builder().build();
                else return Hotel.builder()
                                    .withId(resultSet.getString(1))
                                    .withNombre(resultSet.getString(2))
                                    .withLocalizacion(resultSet.getString(3))
                                    .withEstrellas(resultSet.getInt(4))
                                    .withPrecioNoche(resultSet.getDouble(5))
                                    .build();

            }
        }catch (SQLException sqlException){
            throw managerException(sqlException);
        }

    }

    @Override
    public Hotel insertarHotel(Hotel hotel) throws AppException{

        final String SQL = """
                           INSERT INTO HOTELES(ID               ,NOMBRE, LOCALIZACION,ESTRELLAS ,PRECIO_NOCHE)
                           VALUES(nextval('seq_hoteles'),             ?,            ?,         ?,             ?)                           
                           """;
        String[] columns = {"id"};
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL,columns);
        ){
            preparedStatement.setString(1,hotel.nombre());
            preparedStatement.setString(2,hotel.localizacion());
            preparedStatement.setInt(3,hotel.estrellas());
            preparedStatement.setDouble(4,hotel.precioNoche());
            preparedStatement.executeUpdate();
            try(ResultSet resultSet = preparedStatement.getGeneratedKeys();
            ){
                resultSet.next();
                return hotel.withId(resultSet.getString(1));
            }

        }catch (SQLException sqlException){
            throw managerException(sqlException);
        }

    }

    @Override
    public Hotel updateHotel(Hotel hotel) throws AppException{
        final String SQL = """
                           UPDATE HOTELES
                               SET NOMBRE = ?,
                               LOCALIZACION = ?,
                               ESTRELLAS = ?,
                               PRECIO_NOCHE = ?
                           WHERE id = ?
                           """;

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement =connection.prepareStatement(SQL);
        ){
            preparedStatement.setString(1,hotel.nombre());
            preparedStatement.setString(2,hotel.localizacion());
            preparedStatement.setInt(3,hotel.estrellas());
            preparedStatement.setDouble(4,hotel.precioNoche());
            preparedStatement.setString(5,hotel.id());
            int i = preparedStatement.executeUpdate();
            if(i==0) throw new EntityNotFoundException("No se ha encontrado el hotel con el identificador");

            return hotel;

        }catch (SQLException sqlException){
            throw managerException(sqlException);
        }
    }

    @Override
    public String deleteHotelById(String id) throws AppException {
        final String SQL = """
                           DELETE 
                               FROM HOTELES 
                               WHERE ID = ?
                           """;

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ){
            preparedStatement.setString(1,id);
            int i = preparedStatement.executeUpdate();
            if(i==0) return "El hotel con el id -> " + id + " tiene reserva pendientes";
            return null;

        }catch (SQLException sqlException){
            throw managerException(sqlException);
        }

    }

    public AppException managerException(SQLException sqlException){

        String message = sqlException.getMessage();

        if     (message.contains("NN_HOTELES.NOMBRE"))       return new ConstraintViolationException("El nombre del hotel debe tener un valor");
        else if(message.contains("NN_HOTELES.LOCALIZACION")) return new ConstraintViolationException("La localizacion del hotel debe tener un valor");
        else if(message.contains("NN_HOTELES.ESTRELLAS"))    return new ConstraintViolationException("El numero de estrellas del hotel debe tener un valor");
        else if(message.contains("CH_HOTELES.ESTRELLAS"))    return new ConstraintViolationException("El numero de estrellas del hotel debe tener un valor entre 1 y 5");
        else if(message.contains("FK_RESERVAS_HOTELES"))     return new ConstraintViolationException("El hotel tiene reserva asociadas");

        return new NonControledSQLException(sqlException);
    }



}
