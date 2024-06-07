package es.batbatcar.v2p4.modelo.dao.sqldao;


import es.batbatcar.v2p4.exceptions.ViajeAlreadyExistsException;
import es.batbatcar.v2p4.exceptions.ViajeNotFoundException;
import es.batbatcar.v2p4.modelo.services.MariaDBConnection;
import es.batbatcar.v2p4.modelo.dao.interfaces.ViajeDAO;
import es.batbatcar.v2p4.modelo.dto.viaje.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Repository
public class SQLViajeDAO implements ViajeDAO {
	private final String TABLE_NAME = "viajes";

	@Autowired
    private MariaDBConnection mariaDBConnection;

    @Override
    public Set<Viaje> findAll() {
    	Connection connection = mariaDBConnection.getConnection();
    	
    	String sql = "SELECT * FROM " + TABLE_NAME;
    	Set<Viaje> viajes = new HashSet<>();
    	
    	try (
    			Statement statement = connection.createStatement();
    			ResultSet rs = statement.executeQuery(sql);
    		) {
    		while(rs.next()) {
    			viajes.add(mapToViaje(rs));
    		}
    	} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
    	
    	return viajes;
    }

    @Override
    public Set<Viaje> findAll(String city) {
    	Connection connection = mariaDBConnection.getConnection();
    	
    	String sql = "SELECT * FROM " + TABLE_NAME;
    	Set<Viaje> viajes = new HashSet<>();
    	
    	try (
    			Statement statement = connection.createStatement();
    			ResultSet rs = statement.executeQuery(sql);
    		) {
    		while(rs.next()) {
        	    Viaje viaje = mapToViaje(rs);
        	    if (viaje.tieneEstaCiudadDestino(city)) viajes.add(viaje);
    		}
    	} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
    	
    	return viajes;
    }

    @Override
    public Set<Viaje> findAll(EstadoViaje estadoViajeEsperado) {
    	Connection connection = mariaDBConnection.getConnection();
    	
    	String sql = "SELECT * FROM " + TABLE_NAME;
    	Set<Viaje> viajes = new HashSet<>();
    	
    	try (
    			Statement statement = connection.createStatement();
    			ResultSet rs = statement.executeQuery(sql);
    		) {
    		while(rs.next()) {
    			Viaje viaje = mapToViaje(rs);
        	    if (viaje.tieneEsteEstado(estadoViajeEsperado)) viajes.add(viaje);
    		}
    	} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
    	
    	return viajes;
    }

    @Override
    public Set<Viaje> findAll(Class<? extends Viaje> viajeClass) {
    	Connection connection = mariaDBConnection.getConnection();
    	
    	String sql = "SELECT * FROM " + TABLE_NAME;
    	Set<Viaje> viajes = new HashSet<>();
    	
    	try (
    			Statement statement = connection.createStatement();
    			ResultSet rs = statement.executeQuery(sql);
    		) {
    		while(rs.next()) {
    			Viaje viaje = mapToViaje(rs);
        	    if (viaje.getClass() == viajeClass) viajes.add(viaje);
    		}
    	} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
    	
    	return viajes;
    }

    @Override
    public Viaje findById(int codViaje) {
    	Connection connection = mariaDBConnection.getConnection();
    	
    	String sql = "SELECT * FROM " + TABLE_NAME + " WHERE codViaje=" + codViaje;
    	
    	try (
    			Statement statement = connection.createStatement();
    			ResultSet rs = statement.executeQuery(sql);
    		) {
    	    if(rs.next()) {
        	    return mapToViaje(rs);
    		}
    	} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
    	
    	return null;
    }

    @Override
    public Viaje getById(int codViaje) throws ViajeNotFoundException {
    	Viaje viaje = findById(codViaje);
    	if (viaje == null) {
    		throw new ViajeNotFoundException("El viaje seleccionado no existe");
    	}
    	
    	return viaje;
    }

    @Override
    public void add(Viaje viaje) throws ViajeAlreadyExistsException {
    	Connection connection = mariaDBConnection.getConnection();
    	
    	String sql = String.format(
    			"INSERT INTO %s (codViaje, propietario, ruta, fechaSalida, duracion, precio, plazasOfertadas, estadoViaje)"
    			+ " VALUES (%d, '%s', '%s', '%s', %d, %.2f, %d, '%s')",
    			TABLE_NAME,
    			viaje.getCodViaje(),
    			viaje.getPropietario(),
    			viaje.getRuta(),
    			viaje.getFechaSalidaFormatted(),
    			viaje.getDuracion(),
    			viaje.getPrecio(),
    			viaje.getPlazasOfertadas(),
    			viaje.getEstado()
    			);
    	
    	try (Statement statement = connection.createStatement())
    	{
    		int filas = statement.executeUpdate(sql);
    		if (filas == 0) {
    			throw new ViajeAlreadyExistsException(viaje.getCodViaje());
    		}
    	} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
    }

    @Override
    public void update(Viaje viaje) throws ViajeNotFoundException {
    	Connection connection = mariaDBConnection.getConnection();
    	
    	String sql = String.format(
    			"UPDATE %s SET propietario='%s', ruta='%s', fechaSalida='%s', duracion=%d, precio=%.2f, plazasOfertadas=%d, estadoViaje='%s'"
    			+ " WHERE codViaje=%d",
    			TABLE_NAME,
    			viaje.getPropietario(),
    			viaje.getRuta(),
    			viaje.getFechaSalidaFormatted(),
    			viaje.getDuracion(),
    			viaje.getPrecio(),
    			viaje.getPlazasOfertadas(),
    			viaje.getEstado(),
    			viaje.getCodViaje()
    			);
    	
    	try (Statement statement = connection.createStatement())
    	{
    		int filas = statement.executeUpdate(sql);
    		if (filas == 0) {
    			throw new ViajeNotFoundException(viaje.getCodViaje());
    		}
    	} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
    }

    @Override
    public void remove(Viaje viaje) throws ViajeNotFoundException {
    	Connection connection = mariaDBConnection.getConnection();
    	
    	String sql = "DELETE FROM " + TABLE_NAME + " WHERE codViaje=" + viaje.getCodViaje();
    	
    	try (Statement statement = connection.createStatement())
    	{
    		int filas = statement.executeUpdate(sql);
    		if (filas == 0) {
    			throw new ViajeNotFoundException(viaje.getCodViaje());
    		}
    	} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
    }
    
    private Viaje mapToViaje(ResultSet rs) throws SQLException {
    	int codViaje = rs.getInt("codViaje");
	    String propietario = rs.getString("propietario");
	    String ruta = rs.getString("ruta");
	    LocalDateTime fechaSalida = rs.getTimestamp("fechaSalida").toLocalDateTime();
	    long duracion = rs.getInt("duracion");
	    float precio = rs.getFloat("precio");
	    int plazasOfertadas = rs.getInt("plazasOfertadas");
	    EstadoViaje estadoViaje = EstadoViaje.parse(rs.getString("estadoViaje"));
	    
	    return new Viaje(codViaje, propietario, ruta, fechaSalida, duracion, precio, plazasOfertadas, estadoViaje);
    }
}
