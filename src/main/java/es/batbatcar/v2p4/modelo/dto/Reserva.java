package es.batbatcar.v2p4.modelo.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import es.batbatcar.v2p4.modelo.dto.viaje.Viaje;

public class Reserva {

    private String codigoReserva;

    private String usuario;

    private int plazasSolicitadas;

    private LocalDateTime fechaRealizacion;
    
    private Viaje viaje;

    public Reserva(String codigoReserva) {
        this(codigoReserva, "xxxx", 0, LocalDateTime.now(), null);
    }

    public Reserva(String codigoReserva, String usuario, int plazasSolicitadas, Viaje viaje) {
        this(codigoReserva, usuario, plazasSolicitadas, LocalDateTime.now(), viaje);
    }

    public Reserva(String codigoReserva, String usuario, int plazasSolicitadas, LocalDateTime fechaRealizacion, Viaje viaje) {
        this.codigoReserva = codigoReserva;
        set(usuario, plazasSolicitadas, fechaRealizacion, viaje);
    }
    
    public void set(String usuario, int plazasSolicitadas, LocalDateTime fechaRealizacion, Viaje viaje) {
    	this.usuario = usuario;
        this.plazasSolicitadas = plazasSolicitadas;
        this.fechaRealizacion = fechaRealizacion;
        this.viaje = viaje;
    }

    public String getCodigoReserva() {
        return codigoReserva;
    }

    public String getUsuario() {
        return usuario;
    }

    public int getPlazasSolicitadas() {
        return plazasSolicitadas;
    }

    public LocalDateTime getFechaRealizacion() {
        return fechaRealizacion;
    }
    
    public Viaje getViaje() {
    	return viaje;
    }

    public String getFechaRealizacionFormatted() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTimeFormatter.format(this.fechaRealizacion);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reserva)) return false;
        Reserva reserva = (Reserva) o;
        return codigoReserva.equals(reserva.codigoReserva);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoReserva);
    }

    public void setPropietario(String usuario) {
        this.usuario = usuario;
    }

    public void setPlazasSolicitadas(int numPlazas) {
        this.plazasSolicitadas = numPlazas;
    }

    public boolean perteneceAlViaje(int codViaje) {
        return getCodigoViaje() == codViaje;
    }

    public int getCodigoViaje() {
        return Integer.parseInt(codigoReserva.split("-")[0]);
    }
}
