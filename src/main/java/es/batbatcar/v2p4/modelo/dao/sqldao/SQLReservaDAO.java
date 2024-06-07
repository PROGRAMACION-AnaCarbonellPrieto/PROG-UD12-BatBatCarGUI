package es.batbatcar.v2p4.modelo.dao.sqldao;

import es.batbatcar.v2p4.exceptions.ReservaAlreadyExistsException;
import es.batbatcar.v2p4.exceptions.ReservaNotFoundException;
import es.batbatcar.v2p4.modelo.dto.Reserva;
import es.batbatcar.v2p4.modelo.dto.viaje.Viaje;
import es.batbatcar.v2p4.modelo.services.MariaDBConnection;
import es.batbatcar.v2p4.modelo.dao.interfaces.ReservaDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class SQLReservaDAO implements ReservaDAO {
	private final String TABLE_NAME = "reservas";

	@Autowired
	private MariaDBConnection mariaDBConnection;

    @Override
    public Set<Reserva> findAll() {
    	Connection connection = mariaDBConnection.getConnection();
    	
    	String sql = "SELECT * FROM " + TABLE_NAME;
    	Set<Reserva> reservas = new HashSet<>();
    	
    	try (
    			Statement statement = connection.createStatement();
    			ResultSet rs = statement.executeQuery(sql);
    		) {
    		while(rs.next()) {
    			reservas.add(mapToReserva(rs));
    		}
    	} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
    	
    	return reservas;
    }

    @Override
    public Reserva findById(String id) {
    	Connection connection = mariaDBConnection.getConnection();
    	
    	String sql = "SELECT * FROM " + TABLE_NAME + " WHERE codigoReserva='" + id + "'";
    	
    	try (
    			Statement statement = connection.createStatement();
    			ResultSet rs = statement.executeQuery(sql);
    		) {
    	    while(rs.next()) {
        	    return mapToReserva(rs);
    		}
    	} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
    	
    	return null;
    }

    @Override
    public ArrayList<Reserva> findAllByUser(String user) {
    	Connection connection = mariaDBConnection.getConnection();
    	
    	String sql = "SELECT * FROM " + TABLE_NAME + " WHERE usuario='" + user + "'";
    	ArrayList<Reserva> reservas = new ArrayList<>();
    	
    	try (
    			Statement statement = connection.createStatement();
    			ResultSet rs = statement.executeQuery(sql);
    		) {
    		while(rs.next()) {
    			reservas.add(mapToReserva(rs));
    		}
    	} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
    	
    	return reservas;
    }

    @Override
    public ArrayList<Reserva> findAllByTravel(Viaje viaje) {
    	Connection connection = mariaDBConnection.getConnection();
    	
    	String sql = "SELECT * FROM " + TABLE_NAME;
    	ArrayList<Reserva> reservas = new ArrayList<>();
    	
    	try (
    			Statement statement = connection.createStatement();
    			ResultSet rs = statement.executeQuery(sql);
    		) {
    		while(rs.next()) {
    			Reserva reserva = mapToReserva(rs);
    			if (reserva.perteneceAlViaje(viaje.getCodViaje())) reservas.add(reserva);
    		}
    	} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
    	
    	return reservas;
    }

    @Override
    public Reserva getById(String id) throws ReservaNotFoundException {
    	Reserva reserva = findById(id);
    	if (reserva == null) {
    		throw new ReservaNotFoundException(id);
    	}
    	
    	return reserva;
    }

    @Override
    public List<Reserva> findAllBySearchParams(Viaje viaje, String searchParams) {
    	List<Reserva>reservasViaje = findAllByTravel(viaje);
		List<Reserva> reservasBuscadas = new ArrayList<>();
        for (Reserva reserva: reservasViaje) {
            if (reserva.getUsuario().toLowerCase().contains(searchParams.toLowerCase())
                    || reserva.getCodigoReserva().contains(searchParams)) {
                reservasBuscadas.add(reserva);
            }
        }
        return reservasBuscadas;
    }

    @Override
	public void add(Reserva reserva) throws ReservaAlreadyExistsException {
    	Connection connection = mariaDBConnection.getConnection();
    	
    	String sql = String.format(
    			"INSERT INTO %s (codigoReserva, usuario, plazasSolicitadas, fechaRealizacion, viaje)"
    			+ " VALUES ('%s', '%s', %d, '%s', %d)",
    			TABLE_NAME,
    			reserva.getCodigoReserva(),
    			reserva.getUsuario(),
    			reserva.getPlazasSolicitadas(),
    			reserva.getFechaRealizacionFormatted(),
    			reserva.getViaje().getCodViaje()
    			);
    	
    	try (Statement statement = connection.createStatement())
    	{
    		int filas = statement.executeUpdate(sql);
    		if (filas == 0) {
    			throw new ReservaAlreadyExistsException(reserva);
    		}
    	} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	@Override
	public void update(Reserva reserva) throws ReservaNotFoundException {
    	Connection connection = mariaDBConnection.getConnection();
    	
    	String sql = String.format(
    			"UPDATE %s SET usuario='%s', plazasSolicitadas=%d, fechaRealizacion='%s', viaje=%d"
    	    	+ " WHERE codigoReserva='%s'",
    	    	TABLE_NAME,
    			reserva.getUsuario(),
    			reserva.getPlazasSolicitadas(),
    			reserva.getFechaRealizacion(),
    			reserva.getViaje(),
    			reserva.getCodigoReserva()
    			);
    	
    	try (Statement statement = connection.createStatement())
    	{
    		int filas = statement.executeUpdate(sql);
    		if (filas == 0) {
    			throw new ReservaNotFoundException(reserva.getCodigoReserva());
    		}
    	} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
	@Override
	public void remove(Reserva reserva) throws ReservaNotFoundException {
    	Connection connection = mariaDBConnection.getConnection();
    	
    	String sql = "DELETE FROM " + TABLE_NAME + " WHERE codigoReserva='" + reserva.getCodigoReserva() + "'";
    	
    	try (Statement statement = connection.createStatement())
    	{
    		int filas = statement.executeUpdate(sql);
    		if (filas == 0) {
    			throw new ReservaNotFoundException(reserva.getCodigoReserva());
    		}
    	} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
	@Override
	public int getNumPlazasReservadasEnViaje(Viaje viaje) {
		Connection connection = mariaDBConnection.getConnection();
    	
    	String sql = "SELECT * FROM " + TABLE_NAME;
    	int numPlazasReservadas = 0;
    	
    	try (
    			Statement statement = connection.createStatement();
    			ResultSet rs = statement.executeQuery(sql);
    		) {
    		while(rs.next()) {
    			Reserva reserva = mapToReserva(rs);
    			if (reserva.perteneceAlViaje(viaje.getCodViaje())) {
    				numPlazasReservadas += reserva.getPlazasSolicitadas();
    			}
    		}
    	} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
    	
    	return numPlazasReservadas;
	}
	@Override
	public Reserva findByUserInTravel(String usuario, Viaje viaje) {
    	Connection connection = mariaDBConnection.getConnection();
    	
    	String sql = "SELECT * FROM " + TABLE_NAME;
    	
    	try (
    			Statement statement = connection.createStatement();
    			ResultSet rs = statement.executeQuery(sql);
    		) {
    		while(rs.next()) {
    			Reserva reserva = mapToReserva(rs);
    			if (reserva.perteneceAlViaje(viaje.getCodViaje()) && viaje.getPropietario().equals(usuario)) {
    				return reserva;
    			}
    		}
    	} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
    	
    	return null;
	}
	
	private Reserva mapToReserva(ResultSet rs) throws SQLException {
    	String codigoReserva = rs.getString("codigoReserva");
    	String usuario = rs.getString("usuario");
    	int plazasSolicitadas = rs.getInt("plazasSolicitadas");
    	LocalDateTime fechaRealizacion = rs.getTimestamp("fechaRealizacion").toLocalDateTime();
    	int idViaje = rs.getInt("viaje");
    	
	    Viaje viaje = new Viaje(idViaje);
	    return new Reserva(codigoReserva, usuario, plazasSolicitadas, fechaRealizacion, viaje);
    }
}
