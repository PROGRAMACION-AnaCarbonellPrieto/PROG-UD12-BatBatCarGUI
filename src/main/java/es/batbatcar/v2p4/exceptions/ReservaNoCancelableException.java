package es.batbatcar.v2p4.exceptions;

public class ReservaNoCancelableException extends RuntimeException {
    public ReservaNoCancelableException(String msg) {
        super(msg);
    }

    public ReservaNoCancelableException() {
        super("El viaje no admite cancelaciones");
    }
}


