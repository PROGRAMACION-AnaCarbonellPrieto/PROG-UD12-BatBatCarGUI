package es.batbatcar.v2p4.modelo.dao.sqldao;

import es.batbatcar.v2p4.exceptions.ReservaAlreadyExistsException;
import es.batbatcar.v2p4.exceptions.ReservaNotFoundException;
import es.batbatcar.v2p4.modelo.dto.Reserva;
import es.batbatcar.v2p4.modelo.dto.viaje.Viaje;
import es.batbatcar.v2p4.modelo.services.MariaDBConnection;
import es.batbatcar.v2p4.modelo.dao.interfaces.ReservaDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class SQLReservaDAO implements ReservaDAO {

	@Autowired
	private MariaDBConnection mySQLConnection;

    @Override
    public Set<Reserva> findAll() {
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public Reserva findById(String id) {
    	throw new RuntimeException("Not yet implemented");
    }

    @Override
    public ArrayList<Reserva> findAllByUser(String user) {
    	throw new RuntimeException("Not yet implemented");
    }

    @Override
    public ArrayList<Reserva> findAllByTravel(Viaje viaje) {
    	throw new RuntimeException("Not yet implemented");
    }

    @Override
    public Reserva getById(String id) throws ReservaNotFoundException {
    	throw new RuntimeException("Not yet implemented");
    }

    @Override
    public List<Reserva> findAllBySearchParams(Viaje viaje, String searchParams) {
    	throw new RuntimeException("Not yet implemented");
    }

    @Override
	public void add(Reserva reserva) throws ReservaAlreadyExistsException {
    	throw new RuntimeException("Not yet implemented");
		
	}
	@Override
	public void update(Reserva reserva) throws ReservaNotFoundException {
		throw new RuntimeException("Not yet implemented");
		
	}
	@Override
	public void remove(Reserva reserva) throws ReservaNotFoundException {
		throw new RuntimeException("Not yet implemented");
		
	}
	@Override
	public int getNumPlazasReservadasEnViaje(Viaje viaje) {
		throw new RuntimeException("Not yet implemented");
	}
	@Override
	public Reserva findByUserInTravel(String usuario, Viaje viaje) {
		throw new RuntimeException("Not yet implemented");	
	}
}
