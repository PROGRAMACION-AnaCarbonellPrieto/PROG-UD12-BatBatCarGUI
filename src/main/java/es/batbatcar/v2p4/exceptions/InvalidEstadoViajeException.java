package es.batbatcar.v2p4.exceptions;

public class InvalidEstadoViajeException extends RuntimeException {
    public InvalidEstadoViajeException() {
        super("El estado de viaje asignado es incorrecto");
    }
}
