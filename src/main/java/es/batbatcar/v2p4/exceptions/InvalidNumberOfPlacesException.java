package es.batbatcar.v2p4.exceptions;

public class InvalidNumberOfPlacesException extends RuntimeException {

    public InvalidNumberOfPlacesException() {
        super("El número máximo de plazas por coche es 6");
    }
}
