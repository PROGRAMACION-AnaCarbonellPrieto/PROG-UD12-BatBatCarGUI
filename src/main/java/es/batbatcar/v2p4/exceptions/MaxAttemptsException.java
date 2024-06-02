package es.batbatcar.v2p4.exceptions;

public class MaxAttemptsException extends RuntimeException {

    public MaxAttemptsException(){
        super("Ha alcanzado el máximo número de intentos");
    }
}
