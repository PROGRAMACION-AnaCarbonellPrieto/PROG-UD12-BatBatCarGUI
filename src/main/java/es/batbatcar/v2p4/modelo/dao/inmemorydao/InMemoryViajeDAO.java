package es.batbatcar.v2p4.modelo.dao.inmemorydao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.stereotype.Repository;

import es.batbatcar.v2p4.exceptions.ViajeAlreadyExistsException;
import es.batbatcar.v2p4.exceptions.ViajeNotCancelableException;
import es.batbatcar.v2p4.exceptions.ViajeNotFoundException;
import es.batbatcar.v2p4.modelo.dao.interfaces.ViajeDAO;
import es.batbatcar.v2p4.modelo.dto.viaje.EstadoViaje;
import es.batbatcar.v2p4.modelo.dto.viaje.Viaje;

@Repository
public class InMemoryViajeDAO implements ViajeDAO {

	private Set<Viaje> viajes;
	
	public InMemoryViajeDAO() {
		this.viajes = new TreeSet<>();
		init();
	}
	
	@Override
	public Set<Viaje> findAll() {
		return viajes;
	}

	@Override
	public Set<Viaje> findAll(String city) {
		TreeSet<Viaje> viajesDePaso = new TreeSet<>();
        for (Viaje viaje:this.viajes) {
            if(viaje.tieneEstaCiudadDestino(city)){
                viajesDePaso.add(viaje);
            }
        }
        return viajesDePaso;
	}

	@Override
	public Set<Viaje> findAll(EstadoViaje estadoViaje) {
		TreeSet<Viaje> viajesDePaso = new TreeSet<>();
        for (Viaje viaje:this.viajes) {
            if(viaje.tieneEsteEstado(estadoViaje)){
                viajesDePaso.add(viaje);
            }
        }
        return viajesDePaso;
	}

	@Override
	public Set<Viaje> findAll(Class<? extends Viaje> viajeClass) {
		TreeSet<Viaje> viajesDePaso = new TreeSet<>();
        for (Viaje viaje:this.viajes) {
            if(viaje.getClass() == viajeClass){
                viajesDePaso.add(viaje);
            }
        }
        return viajesDePaso;
	}

	@Override
	public Viaje findById(int codViaje) {
		for (Viaje viaje:this.viajes) {
            if (viaje.equals(new Viaje(codViaje))){
            	return viaje;
            }
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
		if (!this.viajes.add(viaje)) {
			throw new ViajeAlreadyExistsException(viaje.getCodViaje());
		}
	}
	
	@Override
	public void update(Viaje viaje) throws ViajeNotFoundException {
		Viaje viajeAActualizar = findById(viaje.getCodViaje());
		if (viajeAActualizar != null) {
			viajeAActualizar.set(
					viaje.getPropietario(),
					viaje.getRuta(),
					viaje.getFechaSalida(),
					viaje.getDuracion(),
					viaje.getPrecio(),
					viaje.getPlazasOfertadas(),
					viaje.getEstado());
		}
	}

	@Override
	public void remove(Viaje viaje) throws ViajeNotFoundException {
		if (!this.viajes.remove(viaje)) {
			throw new ViajeNotFoundException(viaje.getCodViaje());
		}
	}

	private void init() {
        try {
	        LocalDate fecha = LocalDate.of(2109, 03, 28);
	        LocalTime hora = LocalTime.of(10, 34);
	        LocalDateTime fechaYHora1 = LocalDateTime.of(fecha, hora);
	        Viaje viaje = new Viaje(1, "sergio123", "Madrid-Murcia-Alicante", fechaYHora1, 4, 5f, 4);
	        viajes.add(viaje);
	
	        LocalDateTime fechaYHora2 = LocalDateTime.of(2027, 1, 14, 10, 34);
	        Viaje viaje1 = new Viaje(2, "roberto1979", "Alacant-Valencia",  fechaYHora2, 10, 6f,4);
	        viajes.add(viaje1);
	
	        LocalDateTime fechaYHora3 = LocalDateTime.of(2025, 1, 1, 10, 10, 30);
	        Viaje viaje2 = new Viaje(3, "raul00", "Madrid-Barcelona", fechaYHora3, 180, 10f,3);
	        viajes.add(viaje2);
	
	        LocalDateTime fechaYHora4 = LocalDateTime.of(2025, 4, 28, 16, 0, 49);
	        Viaje viaje3 = new Viaje(4, "alex32", "Alcoy-Cocentaina", fechaYHora4, 10, 2f, 4);
	        viajes.add(viaje3);
	        
	        LocalDateTime fechaYHora5 = LocalDateTime.of(2014, 4, 29, 4, 30, 49);
	        Viaje viaje4 = new Viaje(5, "sergio123", "Alcoy-Alicante", fechaYHora5, 45, 3f, 3, EstadoViaje.CERRADO);
	        viajes.add(viaje4);
	
	        LocalDateTime fechaYHora6 = LocalDateTime.parse("2026-12-31T07:59:00.000");
	        Viaje viaje5 = new Viaje(6, "maria456", "Alicante-Alcoy", fechaYHora6, 45, 3f,2 );
	        viaje5.cancelar();
	        viajes.add(viaje5);
	        
	        LocalDateTime fechaYHora7 = LocalDateTime.parse("2026-12-31T07:59:00.000");
	        Viaje viaje7 = new Viaje(7, "elena12", "Castellon-Gandía", fechaYHora7, 4, 3f, 3, EstadoViaje.CERRADO);
	        viajes.add(viaje7);
        
        } catch (ViajeNotCancelableException ex) {
        	System.out.println(ex.getMessage());
        }

        
    }
}
