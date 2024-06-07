package es.batbatcar.v2p4.modelo.repositories;

import es.batbatcar.v2p4.exceptions.ReservaAlreadyExistsException;
import es.batbatcar.v2p4.exceptions.ReservaNoValidaException;
import es.batbatcar.v2p4.exceptions.ReservaNotFoundException;
import es.batbatcar.v2p4.exceptions.ViajeAlreadyExistsException;
import es.batbatcar.v2p4.exceptions.ViajeNotCancelableException;
import es.batbatcar.v2p4.exceptions.ViajeNotFoundException;
import es.batbatcar.v2p4.modelo.dto.Reserva;
import es.batbatcar.v2p4.modelo.dto.viaje.Viaje;
import es.batbatcar.v2p4.modelo.dao.interfaces.ReservaDAO;
import es.batbatcar.v2p4.modelo.dao.interfaces.ViajeDAO;
import es.batbatcar.v2p4.modelo.dao.sqldao.SQLReservaDAO;
import es.batbatcar.v2p4.modelo.dao.sqldao.SQLViajeDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class ViajesRepository {

    private final ViajeDAO viajeDAO;
    private final ReservaDAO reservaDAO;

    public ViajesRepository(@Autowired SQLViajeDAO viajeDAO, @Autowired SQLReservaDAO reservaDAO) {
        this.viajeDAO = viajeDAO;
        this.reservaDAO = reservaDAO;
    }
    
    /** 
     * Obtiene un conjunto de todos los viajes
     * @return
     */
    public Set<Viaje> findAll() {
        
    	// Se recuperan todos los viajes del DAO de viajes
    	Set<Viaje> viajes = viajeDAO.findAll();
        
    	// Se completa la información acerca de las reservas de cada viaje a través del DAO de reservas
        for (Viaje viaje : viajes) {
        	if (this.reservaDAO.findAllByTravel(viaje).size() > 0) {
            	viaje.setSeHanRealizadoReservas(true);
            }
		}
        return viajes;
    }
    
    public Set<Viaje> findAll(String city) {
        
    	// Se recuperan todos los viajes con destino @city del DAO de viajes
    	Set<Viaje> viajes = viajeDAO.findAll(city);
        
    	// Se completa la información acerca de las reservas de cada viaje a través del DAO de reservas
        for (Viaje viaje : viajes) {
        	if (this.reservaDAO.findAllByTravel(viaje).size() > 0) {
            	viaje.setSeHanRealizadoReservas(true);
            }
		}
        return viajes;
    }
    
    public Viaje findViajeById(int codViaje) throws ViajeNotFoundException {
    	return viajeDAO.getById(codViaje);
    }
    
    public Viaje findViajeSiPermiteReserva(int codViaje, String usuario, int plazasSolicitadas) throws ReservaNoValidaException, ViajeNotFoundException {
    	Viaje viaje = viajeDAO.getById(codViaje);
    	List<Reserva> reservas = reservaDAO.findAllByTravel(viaje);
    	
    	if (viaje.getPropietario().equals(usuario)) {
    		throw new ReservaNoValidaException("Eres el propietario del viaje");
    	}
    	
    	if (viaje.isCerrado() || viaje.isCancelado()) {
    		throw new ReservaNoValidaException("El viaje está cerrado o cancelado");
    	}
    	
    	for(Reserva reserva: reservas) {
    		if (reserva.getUsuario().equals(usuario)) {
    			throw new ReservaNoValidaException("Ya has realizado una reserva");
    		}
    	}
    	
    	if (plazasSolicitadas > getNumPlazasDisponiblesEnViaje(viaje)) {
    		throw new ReservaNoValidaException("No quedan suficientes plazas");
    	}
    	
    	return viaje;
    }
    
    /**
     * Obtiene el código del siguiente viaje
     * @return
     */
    public int getNextCodViaje() {
        return this.viajeDAO.findAll().size() + 1;
    }
    
    public int getNumReservasEnViaje(Viaje viaje) {
    	return reservaDAO.findAllByTravel(viaje).size();
    }
    
    public int getNumPlazasDisponiblesEnViaje(Viaje viaje) {
    	return viaje.getPlazasOfertadas() - reservaDAO.getNumPlazasReservadasEnViaje(viaje);
    }
    
    /**
     * Guarda el viaje (actualiza si ya existe o añade si no existe)
     * @param viaje
     * @throws ViajeAlreadyExistsException
     * @throws ViajeNotFoundException
     */
    public void save(Viaje viaje) throws ViajeAlreadyExistsException, ViajeNotFoundException {
    	
    	if (viajeDAO.findById(viaje.getCodViaje()) == null) {
    		viajeDAO.add(viaje);
    	} else {
    		viajeDAO.update(viaje);
    	}
    }
    
    public void cancel(int codViaje) throws ViajeNotCancelableException, ViajeNotFoundException {
    	Viaje viaje = viajeDAO.getById(codViaje);
    	viaje.cancelar();
    	
    	viajeDAO.update(viaje);
    }
    


	public Reserva findReservaById(String codReserva) throws ReservaNotFoundException {
		return reservaDAO.getById(codReserva);
	}
	
    /**
     * Encuentra todas las reservas de @viaje
     * @param viaje
     * @return
     */
	public List<Reserva> findReservasByViaje(Viaje viaje) {
		return reservaDAO.findAllByTravel(viaje);
	}
	
	/**
	 * Guarda la reserva
	 * @param reserva
	 * @throws ReservaAlreadyExistsException
	 * @throws ReservaNotFoundException
	 */
    public void save(Reserva reserva) throws ReservaAlreadyExistsException, ReservaNotFoundException {
    	
    	if (reservaDAO.findById(reserva.getCodigoReserva()) == null) {
    		reservaDAO.add(reserva);
    	} else {
    		reservaDAO.update(reserva);
    	}
    }
    
    /**
     * Elimina la reserva
     * @param reserva
     * @throws ReservaNotFoundException
     */
	public void remove(Reserva reserva) throws ReservaNotFoundException {
		reservaDAO.remove(reserva);
	}

	public String getNextCodReserva(Viaje viaje) {
		List<Reserva> reservas = reservaDAO.findAllByTravel(viaje);
		if (reservas.isEmpty()) {
			return viaje.getCodViaje() + "-1";
		}
		
		String codigoReserva = reservas.get(reservas.size() - 1).getCodigoReserva();
		int numReserva = Integer.parseInt(codigoReserva.split("-")[1]) + 1;
		return viaje.getCodViaje() + "-" + numReserva;
	}
}
