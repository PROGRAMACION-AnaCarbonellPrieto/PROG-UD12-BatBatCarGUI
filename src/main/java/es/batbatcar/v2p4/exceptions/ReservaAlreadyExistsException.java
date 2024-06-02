package es.batbatcar.v2p4.exceptions;

import es.batbatcar.v2p4.modelo.dto.Reserva;

public class ReservaAlreadyExistsException extends Exception{
	public ReservaAlreadyExistsException(Reserva reserva) {
		super("La reserva con código " + reserva.getCodigoReserva() + " ya existe");
	}

}
