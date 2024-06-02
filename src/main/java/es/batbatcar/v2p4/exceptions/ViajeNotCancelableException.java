package es.batbatcar.v2p4.exceptions;

public class ViajeNotCancelableException extends Exception{

    public ViajeNotCancelableException(String codViaje) {
        super("El viaje " + codViaje + " no permite ser cancelado");
    }
}
