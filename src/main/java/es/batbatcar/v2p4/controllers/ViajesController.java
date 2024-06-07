package es.batbatcar.v2p4.controllers;

import es.batbatcar.v2p4.exceptions.ViajeAlreadyExistsException;
import es.batbatcar.v2p4.exceptions.ViajeNotCancelableException;
import es.batbatcar.v2p4.exceptions.ViajeNotFoundException;
import es.batbatcar.v2p4.modelo.dto.viaje.Viaje;
import es.batbatcar.v2p4.modelo.repositories.ViajesRepository;
import es.batbatcar.v2p4.utils.Validator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ViajesController {

    @Autowired
    private ViajesRepository viajesRepository;
    
    /**
     * Endpoint que muestra el listado de todos los viajes disponibles
     *
     * */
    @GetMapping("/viajes")
    public String getViajesAction(@RequestParam Map<String, String> params, Model model) {
    	Set<Viaje> viajes;
    	if (params.containsKey("destino")) {
    		viajes = viajesRepository.findAll(params.get("destino"));
    	} else {
    		viajes = viajesRepository.findAll();
    	}
    	
    	Map<Integer, Integer> numReservas = new HashMap<>();
    	Map<Integer, Integer> plazasDisponibles = new HashMap<>();
    	for (Viaje viaje: viajes) {
    		numReservas.put(viaje.getCodViaje(), viajesRepository.getNumReservasEnViaje(viaje));
    		plazasDisponibles.put(viaje.getCodViaje(), viajesRepository.getNumPlazasDisponiblesEnViaje(viaje));
    	}
    	
    	model.addAttribute("viajes", viajes);
    	model.addAttribute("numReservas", numReservas);
    	model.addAttribute("plazasDisponibles", plazasDisponibles);
        return "viaje/listado";
    }
    
    @GetMapping("/viaje/add")
    public String getAddViajeAction() {
    	return "viaje/viaje_form";
    }
    
    @PostMapping("/viaje/add")
    public String postAddViajeAction(@RequestParam Map<String, String> params, RedirectAttributes redirectAttributes) {
    	Map<String, String> errors = new HashMap<>();
    	
    	for (Map.Entry<String, String> param: params.entrySet()) {
    		if (param.getValue().isEmpty()) {
    			errors.put("error", "Todos los campos tienen que estar rellenados");
    			redirectAttributes.addFlashAttribute("errors", errors);
    			return "redirect:/viaje/add";
    		}
    	}
    	
    	String ruta;
    	int plazasOfertadas;
    	String propietario;
    	float precio;
    	long duracion;
    	String diaSalida;
    	String horaSalida;
    	LocalDateTime fechaSalida;
    	try {
	    	ruta = params.get("ruta");
	    	plazasOfertadas = Integer.parseInt(params.get("plazasOfertadas"));
	    	propietario = params.get("propietario");
	    	precio = Float.parseFloat(params.get("precio"));
	    	duracion = Long.parseLong(params.get("duracion"));
	    	diaSalida = params.get("diaSalida");
	    	horaSalida = params.get("horaSalida");
    	} catch (NumberFormatException e) {
    		errors.put("error", "Los campos numéricos sólo puede contener números");
			redirectAttributes.addFlashAttribute("errors", errors);
			return "redirect:/viaje/add";
		}
    	
    	
    	if (!Validator.isValidText(ruta, '-')) {
    		errors.put("ruta", "La ruta debe cumplir el formato Origen-Destino");
    	}
    	
    	if (!Validator.isValidNumber(plazasOfertadas)) {
    		errors.put("plazas", "Las plazas ofertadas deben ser un valor entre 1 y 6");
    	}
    	
    	if (!Validator.isValidText(propietario, ' ')) {
    		errors.put("propietario", "El propietario debe contener al menos dos cadenas separadas por espacio en blanco y comiencen por mayúsculas");
    	}
    	
    	if (!Validator.isValidNumber(precio)) {
    		errors.put("precio", "El precio debe ser un valor mayor a 0");
    	}
    	
    	if (!Validator.isValidNumber(duracion)) {
    		errors.put("duracion", "La duración debe ser un valor mayor a 0");
    	}
    	
    	if (!Validator.isValidDate(diaSalida)) {
    		errors.put("fecha", "La fecha indicada no es válida");
    	}
    	
    	if (!Validator.isValidTime(horaSalida)) {
    		System.out.println(horaSalida);
    		errors.put("hora", "La hora indicada no es válida");
    	}
    	
    	if (errors.size() > 0) {
    		redirectAttributes.addFlashAttribute("errors", errors);
    		return "redirect:/viaje/add";
    	}
    	
    	fechaSalida = LocalDateTime.of(LocalDate.parse(diaSalida), LocalTime.parse(horaSalida));
    	Viaje nuevoViaje = new Viaje(viajesRepository.getNextCodViaje(), propietario, ruta, fechaSalida, duracion, precio, plazasOfertadas);
    	
    	try {
			viajesRepository.save(nuevoViaje);
			redirectAttributes.addFlashAttribute("infoMessage", "Viaje insertado con éxito");
			return "redirect:/viajes";
		} catch (ViajeNotFoundException | ViajeAlreadyExistsException e) {
			errors.put("error", e.getMessage());
		}
    	redirectAttributes.addFlashAttribute("errors", errors);
    	return "redirect:/viaje/add";
    }
    
    @GetMapping("/viaje")
    public String getDetailViajeAction(@RequestParam Map<String, String> params, Model model) {
    	Viaje viaje;
		try {
			int codViaje = Integer.parseInt(params.get("codViaje"));
			viaje = viajesRepository.findViajeById(codViaje);
		} catch (NumberFormatException | ViajeNotFoundException e) {
			return "redirect:/viajes";
		}
		
    	model.addAttribute("viaje", viaje);
    	model.addAttribute("reservas", viajesRepository.findReservasByViaje(viaje));
    	return "viaje/viaje_detalle";
    }
    
    @GetMapping("/viaje/cancel")
    public String getCancelViajeAction(@RequestParam Map<String, String> params, RedirectAttributes redirectAttributes) {
    	try {
    		int codViaje = Integer.parseInt(params.get("codViaje"));
			viajesRepository.cancel(codViaje);
			redirectAttributes.addFlashAttribute("infoMessage", "Viaje cancelado con éxito");
		} catch (NumberFormatException | ViajeNotCancelableException | ViajeNotFoundException e) {
			redirectAttributes.addFlashAttribute("infoMessage", e.getMessage());
		}
    	return "redirect:/viajes";
    }
}
