package es.batbatcar.v2p4.exceptions;

public class ReservaNotFoundException extends Exception {

    public ReservaNotFoundException(String codReserva) {
        super("La reserva con código " + codReserva + " no ha sido encontrada");
    }

}
