package es.batbatcar.v2p4.modelo.dao.sqldao;


import es.batbatcar.v2p4.exceptions.ViajeNotFoundException;
import es.batbatcar.v2p4.modelo.services.MariaDBConnection;
import es.batbatcar.v2p4.modelo.dao.interfaces.ViajeDAO;
import es.batbatcar.v2p4.modelo.dto.viaje.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class SQLViajeDAO implements ViajeDAO {

	@Autowired
    private MariaDBConnection mySQLConnection;

    @Override
    public Set<Viaje> findAll() {
    	throw new RuntimeException("Not yet implemented");
    }

    @Override
    public Set<Viaje> findAll(String city) {
    	throw new RuntimeException("Not yet implemented");
    }

    @Override
    public Set<Viaje> findAll(EstadoViaje estadoViaje) {
    	throw new RuntimeException("Not yet implemented");
    }

    @Override
    public Set<Viaje> findAll(Class<? extends Viaje> viajeClass) {
    	throw new RuntimeException("Not yet implemented");
    }

    @Override
    public Viaje findById(int codViaje) {
    	throw new RuntimeException("Not yet implemented");
    }

    @Override
    public Viaje getById(int codViaje) throws ViajeNotFoundException {
    	throw new RuntimeException("Not yet implemented");
    }

    @Override
    public void add(Viaje viaje) {
    	throw new RuntimeException("Not yet implemented");
    }

    @Override
    public void update(Viaje viaje) throws ViajeNotFoundException {
    	throw new RuntimeException("Not yet implemented");
    }

    @Override
    public void remove(Viaje viaje) throws ViajeNotFoundException {
    	throw new RuntimeException("Not yet implemented");
    }
}
