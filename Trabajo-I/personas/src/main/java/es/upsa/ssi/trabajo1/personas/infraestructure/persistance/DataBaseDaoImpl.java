package es.upsa.ssi.trabajo1.personas.infraestructure.persistance;

import es.upsa.ssi.trabajo1.domain.exceptions.ConstraintViolationException;
import es.upsa.ssi.trabajo1.domain.exceptions.EntityNotFoundException;
import es.upsa.ssi.trabajo1.domain.exceptions.NonControledSQLException;
import es.upsa.ssi.trabajo1.personas.adapters.output.DataBaseDao;
import es.upsa.ssi.trabajo1.domain.entities.Persona;
import es.upsa.ssi.trabajo1.domain.exceptions.AppException;
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
    public List<Persona> findAllPersonas() throws AppException {
        final String SQL = """
                           SELECT p.DNI, p.NOMBRE, p.FECHA_NACIMIENTO, p.TELEFONO
                           FROM PERSONAS p
                           """;

        List<Persona> personas = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
        ){
            while(resultSet.next()){
                personas.add(
                        Persona.builder()
                                .withDni(resultSet.getString(1))
                                .withNombre(resultSet.getString(2))
                                .withFechaNacimiento(resultSet.getDate(3).toLocalDate())
                                .withTelefono(resultSet.getString(4))
                                .build()
                );
            }
        }catch (SQLException sqlException){
            throw manager(sqlException);
        }
        return personas;
    }

    @Override
    public Persona findPersonasByDni(String dni) throws AppException {
        final String SQL = """
                           SELECT p.DNI, p.NOMBRE, p.FECHA_NACIMIENTO, p.TELEFONO
                           FROM PERSONAS p
                           WHERE p.DNI = ?
                           """;

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ){
            preparedStatement.setString(1,dni);
            try(ResultSet resultSet = preparedStatement.executeQuery();
            ){
                if(!resultSet.next()) return Persona.builder().build();

                return Persona.builder()
                                .withDni(resultSet.getString(1))
                                .withNombre(resultSet.getString(2))
                                .withTelefono(resultSet.getString(4))
                                .withFechaNacimiento(resultSet.getDate(3).toLocalDate())
                                .build();

            }


        }catch (SQLException sqlException){
            throw manager(sqlException);
        }

    }

    @Override
    public Persona addPersona(Persona persona) throws AppException {
        final String SQL= """
                          INSERT INTO PERSONAS (DNI,NOMBRE, FECHA_NACIMIENTO, TELEFONO)
                          VALUES               (?  ,?     ,?                ,?        )      
                          """;

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ){
            preparedStatement.setString(1,persona.dni());
            preparedStatement.setString(2,persona.nombre());
            preparedStatement.setDate(3,Date.valueOf(persona.fechaNacimiento()));
            preparedStatement.setString(4,persona.telefono());
            preparedStatement.executeUpdate();

            return persona;
        }catch (SQLException sqlException){
            throw manager(sqlException);
        }
    }

    @Override
    public Persona updatePersona(Persona persona) throws AppException {

        final String SQL = """
                           UPDATE PERSONAS                                                       
                             SET NOMBRE = ?,
                             TELEFONO = ?,
                             FECHA_NACIMIENTO = ?
                           WHERE DNI = ? 
                           """;
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ){
            preparedStatement.setString(1,persona.nombre());
            preparedStatement.setString(2,persona.telefono());
            preparedStatement.setDate(3,Date.valueOf(persona.fechaNacimiento()));
            preparedStatement.setString(4,persona.dni());
            int i = preparedStatement.executeUpdate();
            if(i==0) throw new EntityNotFoundException("La persona con el DNI -> " + persona.dni() + " no existe.");

            return persona;
        }catch (SQLException sqlException){
            throw manager(sqlException);
        }
    }

    @Override
    public boolean deletePersonaByDni(String dni) throws AppException {
        final String SQL = """
                           DELETE FROM PERSONAS
                           WHERE DNI = ? 
                           """;
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ){
            preparedStatement.setString(1,dni);
            int i = preparedStatement.executeUpdate();
            if (i==0) {
                return false;
            }
            return true;

        }catch (SQLException sqlException){
            throw manager(sqlException);
        }
    }


    public AppException manager(SQLException sqlException){

        String message = sqlException.getMessage();
        if(message.contains( "PK_PERSONAS")) return new ConstraintViolationException("La persona con ese DNI ya esta introducida, no puede haber dos personas con el mismpo DNI");
        else if(message.contains( "NN_PERSONAS.DNI")) return new ConstraintViolationException("El campo del dni no puede estar vacio");
        else if(message.contains( "NN_PERSONAS.NOMBRE")) return new ConstraintViolationException("El campo del nombre no puede estar vacio");
        else if(message.contains("NN_PERSONAS.FECHA_NACIMIENTO")) return new ConstraintViolationException("El campo de la fecha de nacimiento no puede estar vacio");
        else if(message.contains("NN_PERSONAS.TELEFONO")) return new ConstraintViolationException("El campo del telefono no puede estar vacio");
        else if(message.contains("FK_RESERVAS_PERSONAS")) return new ConstraintViolationException("Esta persona tiene reserva pendientes");
        else if(message.contains("CH_FECHA_NACIMIENTO")) return new ConstraintViolationException("La fecha nacimiento no puede ser mayor a la fecha actual");

        return new NonControledSQLException(sqlException);
    }
}
