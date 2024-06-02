package es.batbatcar.v2p4.modelo.dao.inmemorydao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import es.batbatcar.v2p4.exceptions.ReservaAlreadyExistsException;
import es.batbatcar.v2p4.exceptions.ReservaNotFoundException;
import es.batbatcar.v2p4.modelo.dto.Reserva;
import es.batbatcar.v2p4.modelo.dto.viaje.EstadoViaje;
import es.batbatcar.v2p4.modelo.dto.viaje.Viaje;
import es.batbatcar.v2p4.modelo.dao.interfaces.ReservaDAO;

@Repository
public class InMemoryReservaDAO implements ReservaDAO {

	private Set<Reserva> reservas;
	
	public InMemoryReservaDAO() {
		this.reservas = new HashSet<>();
		init();
	}
	
	@Override
	public Set<Reserva> findAll() {
		return reservas;
	}

	@Override
	public Reserva findById(String id) {
		for (Reserva reserva : reservas) {
			if (reserva.getCodigoReserva().equals(id)) {
				return reserva;
			}
		}
		
		return null;
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
	public List<Reserva> findAllByUser(String user) {
		List<Reserva> reservasFiltradas = new ArrayList<>();
		for (Reserva reserva : reservas) {
			if (reserva.getUsuario().equals(user)) {
				reservasFiltradas.add(reserva);
			}
		}
		
		return reservasFiltradas;
	}

	@Override
	public List<Reserva> findAllByTravel(Viaje viaje) {
		List<Reserva> reservasViaje = new ArrayList<>();
		
		for (Reserva reserva : reservas) {
			if (reserva.perteneceAlViaje(viaje.getCodViaje())) {
				reservasViaje.add(reserva);
			}
		}
		
		return reservasViaje;
	}
	
	@Override
	public int getNumPlazasReservadasEnViaje(Viaje viaje) {
		int numPlazasReservadas = 0;
		
		for (Reserva reserva : reservas) {
			if (reserva.perteneceAlViaje(viaje.getCodViaje())) {
				numPlazasReservadas += reserva.getPlazasSolicitadas();
			}
		}
		
		return numPlazasReservadas;
	}
	
	@Override
	public Reserva findByUserInTravel(String usuario, Viaje viaje) {
        for (Reserva reserva: reservas) {
            if (reserva.perteneceAlViaje(viaje.getCodViaje()) && viaje.getPropietario().equals(usuario)){
                return reserva;
            }
        }
        
        return null;
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
		if (!this.reservas.add(reserva)) {
			throw new ReservaAlreadyExistsException(reserva);
		}
	}

	@Override
	public void update(Reserva reservaUpdate) throws ReservaNotFoundException {
		for (Reserva reserva : reservas) {
			if (reservaUpdate.equals(reserva)) {
				reserva.set(
						reservaUpdate.getUsuario(),
						reservaUpdate.getPlazasSolicitadas(),
						reservaUpdate.getFechaRealizacion(),
						reservaUpdate.getViaje());
				return;
			}
		}
		
		throw new ReservaNotFoundException(reservaUpdate.getCodigoReserva());
		
	}

	@Override
	public void remove(Reserva reserva) throws ReservaNotFoundException {
		if (!this.reservas.remove(reserva)) {
			throw new ReservaNotFoundException(reserva.getCodigoReserva());
		}
	}
	
	private void init() {
		LocalDate fecha = LocalDate.of(2109, 03, 28);
        LocalTime hora = LocalTime.of(10, 34);
        LocalDateTime fechaYHora1 = LocalDateTime.of(fecha, hora);
        Viaje viaje = new Viaje(1, "sergio123", "Madrid-Murcia-Alicante", fechaYHora1, 4, 5f, 4);
        reservas.add(new Reserva("1-1", "alex32", 2, viaje));
        reservas.add(new Reserva("1-2", "roberto1979", 1, viaje));
        
        LocalDateTime fechaYHora3 = LocalDateTime.of(2025, 1, 1, 10, 10, 30);
        Viaje viaje2 = new Viaje(3, "raul00", "Madrid-Barcelona", fechaYHora3, 180, 10f,3);
        reservas.add(new Reserva("3-1", "elena12", 2, viaje2));
        
        LocalDateTime fechaYHora4 = LocalDateTime.of(2025, 4, 28, 16, 0, 49);
        Viaje viaje3 = new Viaje(4, "alex32", "Alcoy-Cocentaina", fechaYHora4, 10, 2f, 4);
        reservas.add(new Reserva("4-1", "raul00", 2, viaje3));
        reservas.add(new Reserva("4-2", "roberto1979", 1, viaje3));

        LocalDateTime fechaYHora7 = LocalDateTime.parse("2026-12-31T07:59:00.000");
        Viaje viaje7 = new Viaje(7, "elena12", "Castellon-Gandia", fechaYHora7, 4, 3f, 3, EstadoViaje.CERRADO);
        reservas.add(new Reserva("7-1", "roberto1979", 2, viaje7));
        reservas.add(new Reserva("7-2", "sergio123", 1, viaje7));
	}
}
