package es.batbatcar.v2p4.modelo.dao.interfaces;

import es.batbatcar.v2p4.exceptions.ReservaAlreadyExistsException;
import es.batbatcar.v2p4.exceptions.ReservaNotFoundException;
import es.batbatcar.v2p4.modelo.dto.Reserva;
import es.batbatcar.v2p4.modelo.dto.viaje.Viaje;

import java.util.List;
import java.util.Set;

public interface ReservaDAO {

    /**
     * Obtiene todas las reservas
     *
     * @return Set
     */
    Set<Reserva> findAll();
    
    /**
     * Obtiene todas las reservas realizadas por un @user
     *
     * @param user
     * @return Array List de reservas del usuario
     */
    List<Reserva> findAllByUser(String user);

    /**
     * Obtiene todas las reservas de un viaje
     *
     * @param viaje
     * @return Array List de reservas
     */
    List<Reserva> findAllByTravel(Viaje viaje);
    

    /**
     * Busca una reserva con el codigo @id
     *
     * @param id
     * @return Reserva o null si no existe la reserva en la bd
     */
    Reserva findById(String id);
    
    /**
     * Busca una reserva en @viaje que pertenezca a @user
     * @param user
     * @param viaje
     * @return Reserva o null si no existe 
     */
	Reserva findByUserInTravel(String user, Viaje viaje);

    /**
     * Obtiene la reserva cuyo código es @id
     * @param id
     *
     * @return Reserva
     *
     * @throws ReservaNotFoundException si la reserva con @id no se encuentra
     */
    Reserva getById(String id) throws ReservaNotFoundException;
    
    /**
     * Obtiene el número de plazas ya reservadas en @viaje
     * @param viaje
     * @return
     */
    int getNumPlazasReservadasEnViaje(Viaje viaje);
    
    
    /**
     * Inserta una reserva si no existe
     * @param reserva
     * @throws ReservaAlreadyExistsException
     * @return 
     */
    void add(Reserva reserva) throws ReservaAlreadyExistsException;
    
    /**
     * Actualiza una reserva si existe
     * @param reserva
     * @throws ReservaNotFoundException
     * @return 
     */
    void update(Reserva reserva) throws ReservaNotFoundException;

    /**
     * Elimina la reserva si existe
     *
     * @param reserva
     * @throws ReservaNotFoundException
     */
    void remove(Reserva reserva) throws ReservaNotFoundException;

    /**
     * Selecciona, de todas las reservas de un viaje, solo aquellas 
     * que coincida su usuario con la toda o parte de la información contenida en @searchParams 
     * o bien que coincida su código de reserva  con la toda o parte de la información contenida en @searchParams 
     * 	
     *
     * @param viaje
     * @param searchParams
     *
     * @return List<Reserva>
     */
    List<Reserva> findAllBySearchParams(Viaje viaje, String searchParams);

}
