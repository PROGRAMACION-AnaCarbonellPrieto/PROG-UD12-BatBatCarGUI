package es.batbatcar.v2p4.modelo.dto.viaje;

import es.batbatcar.v2p4.exceptions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Viaje implements Comparable<Viaje> {

    private int codViaje;

    private String propietario;

    private String ruta;

    private LocalDateTime fechaSalida;

    private long duracion;

    private float precio;

    protected int plazasOfertadas;

    protected EstadoViaje estadoViaje;

    protected boolean seHanRealizadoReservas;

    public Viaje(int codViaje) {
        this(codViaje, "", "", LocalDateTime.now(), 0, 5f, 4);
    }

    public Viaje(int codViaje, String propietario, String ruta, LocalDateTime fechaSalida, long duracion) {
        this(codViaje, propietario, ruta, fechaSalida, duracion, 5f, 4);
    }

    public Viaje(int codViaje, String propietario, String ruta, LocalDateTime fechaSalida, long duracion,
                 float precio, int plazasOfertadas) {
        this(codViaje, propietario, ruta, fechaSalida, duracion, precio, plazasOfertadas, EstadoViaje.ABIERTO);
    }

    public Viaje(int codViaje, String propietario, String ruta, LocalDateTime fechaSalida, long duracion,
                 float precio, int plazasOfertadas, EstadoViaje estadoViaje) {
        this.codViaje = codViaje;
        set(propietario, ruta, fechaSalida, duracion, precio, plazasOfertadas, estadoViaje);
    }
    
    public void set(String propietario, String ruta, LocalDateTime fechaSalida, long duracion,
            float precio, int plazasOfertadas, EstadoViaje estadoViaje) {
		this.propietario = propietario;
        this.ruta = ruta;
        this.fechaSalida = fechaSalida;
        this.duracion = duracion;
        this.precio = precio;
        this.estadoViaje = estadoViaje;
        this.seHanRealizadoReservas = false;
        setPlazas(plazasOfertadas);
	}
    
    public boolean isSeHanRealizadoReservas() {
		return seHanRealizadoReservas;
	}

	public void setSeHanRealizadoReservas(boolean seHanRealizadoReservas) {
        this.seHanRealizadoReservas = seHanRealizadoReservas;
    }

    public String getTypoString() {
        return "Viaje";
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPlazas(int plazas) {
        if (plazasOfertadas > plazas) {
            throw new PlazasCanNotBeReducedException();
        }
        this.plazasOfertadas = plazas;
    }
    
    public boolean isCerrado() {
        return !(this.estadoViaje == EstadoViaje.ABIERTO && !haSalido());
    }

    public int getCodViaje() {
        return codViaje;
    }

    public String getRuta() {
        return ruta;
    }

    public long getDuracion() {
        return duracion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        Viaje viaje = (Viaje) o;
        return codViaje == viaje.codViaje;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codViaje);
    }

    public EstadoViaje getEstado() {
        return estadoViaje;
    }

    public LocalDateTime getFechaSalida() {
        return fechaSalida;
    }

    public String getFechaSalidaFormatted() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTimeFormatter.format(fechaSalida);
    }

    public float getPrecio() {
        return precio;
    }

    public int getPlazasOfertadas() {
        return plazasOfertadas;
    }

    @Override
    public int compareTo(Viaje o) {
        if (codViaje == o.codViaje) {
            return 0;
        } else if (codViaje > o.codViaje) {
            return 1;
        } else {
            return -1;
        }
    }

    public boolean estaDisponible() {
        return this.estadoViaje == EstadoViaje.ABIERTO && !haSalido();
    }

    protected boolean haSalido() {
        return fechaSalida.isBefore(LocalDateTime.now());
    }

    public boolean isCancelado() {
        return this.estadoViaje == EstadoViaje.CANCELADO;
    }

    public void cancelar() throws ViajeNotCancelableException {
        if (!estaDisponible()) {
            throw new ViajeNotCancelableException(String.valueOf(this.codViaje));
        }
        this.estadoViaje = EstadoViaje.CANCELADO;
    }

    public boolean tieneEsteEstado(EstadoViaje estadoViaje) {
        return this.estadoViaje == estadoViaje;
    }

    public boolean tieneEstaCiudadDestino(String ciudadDestino) {
        return this.ruta.substring(this.ruta.indexOf("-")).contains(ciudadDestino);
    }

    public void cerrarViaje() {
        estadoViaje = EstadoViaje.CERRADO;
    }
}
