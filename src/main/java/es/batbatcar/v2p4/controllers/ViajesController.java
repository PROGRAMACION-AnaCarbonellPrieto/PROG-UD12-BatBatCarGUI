package es.batbatcar.v2p4.controllers;

import es.batbatcar.v2p4.exceptions.ReservaAlreadyExistsException;
import es.batbatcar.v2p4.exceptions.ReservaNoValidaException;
import es.batbatcar.v2p4.exceptions.ReservaNotFoundException;
import es.batbatcar.v2p4.exceptions.ViajeAlreadyExistsException;
import es.batbatcar.v2p4.exceptions.ViajeNotFoundException;
import es.batbatcar.v2p4.modelo.dto.Reserva;
import es.batbatcar.v2p4.modelo.dto.viaje.Viaje;
import es.batbatcar.v2p4.modelo.repositories.ViajesRepository;
import es.batbatcar.v2p4.utils.Validator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

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
    	if (params.containsKey("destino")) {
    		model.addAttribute("viajes", viajesRepository.findAll(params.get("destino")));
    	} else {
    		model.addAttribute("viajes", viajesRepository.findAll());
    	}
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
    			errors.put("vacío", "Todos los campos tienen que estar rellenados");
    			redirectAttributes.addFlashAttribute("errors", errors);
    			return "redirect:/viaje/add";
    		}
    	}
    	
    	String ruta = params.get("ruta");
    	int plazasOfertadas = Integer.parseInt(params.get("plazasOfertadas"));
    	String propietario = params.get("propietario");
    	float precio = Float.parseFloat(params.get("precio"));
    	long duracion = Long.parseLong(params.get("duracion"));
    	String diaSalida = params.get("diaSalida");
    	String horaSalida = params.get("horaSalida");
    	LocalDateTime fechaSalida;
    	
    	
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
		} catch (ViajeAlreadyExistsException e) {
			errors.put("existe", e.getMessage());
		} catch (ViajeNotFoundException e) {
			errors.put("notFound", e.getMessage());
		}
    	redirectAttributes.addFlashAttribute("errors", errors);
    	return "redirect:/viaje/add";
    }
    
    @GetMapping("/viaje")
    public String getDetailAction(@RequestParam Map<String, String> params, Model model) {
    	if (!params.containsKey("codViaje") || params.get("codViaje").isEmpty()) {
    		return "redirect:/viajes";
    	}
    	
    	int codViaje = Integer.parseInt(params.get("codViaje"));
    	model.addAttribute("viaje", viajesRepository.findViajeById(codViaje));
    	return "viaje/viaje_detalle";
    }
    
    @GetMapping("/viaje/reserva/add")
    public String getAddReservaAction(@RequestParam Map<String, String> params, Model model) {
    	if (!params.containsKey("codViaje") || params.get("codViaje").isEmpty()) {
    		return "redirect:/viajes";
    	}
    	
    	model.addAttribute("codViaje", params.get("codViaje"));
    	return "reserva/reserva_form";
    }
    
    @PostMapping("/viaje/reserva/add")
    public String postAddReservaAction(@RequestParam Map<String, String> params, RedirectAttributes redirectAttributes) {
    	int codViaje = Integer.parseInt(params.get("codViaje"));
    	Map<String, String> errors = new HashMap<>();
    	
    	for (Map.Entry<String, String> param: params.entrySet()) {
    		if (param.getValue().isEmpty()) {
    			errors.put("vacío", "Todos los campos tienen que estar rellenados");
    			redirectAttributes.addFlashAttribute("errors", errors);
    			redirectAttributes.addAttribute("codViaje", codViaje);
    			return "redirect:/viaje/reserva/add";
    		}
    	}
    	
    	String usuario = params.get("usuario");
    	int plazasSolicitadas = Integer.parseInt(params.get("plazasSolicitadas"));
    	
    	if (!Validator.isValidText(usuario, ' ')) {
    		errors.put("usuario", "El propietario debe contener al menos dos cadenas separadas por espacio en blanco y comiencen por mayúsculas");
    	}
    	
    	if (!Validator.isValidNumber(plazasSolicitadas)) {
    		errors.put("plazas", "Las plazas solicitadas deben ser un valor entre 1 y 6");
    	}
    	
    	if (errors.size() > 0) {
    		redirectAttributes.addFlashAttribute("errors", errors);
    		redirectAttributes.addAttribute("codViaje", codViaje);
    		return "redirect:/viaje/reserva/add";
    	}
    	
    	Viaje viaje;
    	try {
			viaje = viajesRepository.findViajeSiPermiteReserva(codViaje, usuario, plazasSolicitadas);
		} catch (ReservaNoValidaException e) {
			errors.put("noValid", e.getMessage());
			redirectAttributes.addFlashAttribute("errors", errors);
    		redirectAttributes.addAttribute("codViaje", codViaje);
    		return "redirect:/viaje/reserva/add";
		}
    	
    	Reserva nuevaReserva = new Reserva(viajesRepository.getNextCodReserva(viaje), usuario, plazasSolicitadas, viaje);
    	try {
			viajesRepository.save(nuevaReserva);
			redirectAttributes.addFlashAttribute("infoMessage", "Reserva insertada con éxito");
	    	return "redirect:/viajes";
		} catch (ReservaAlreadyExistsException e) {
			errors.put("noValid", e.getMessage());
		} catch (ReservaNotFoundException e) {
			errors.put("noValid", e.getMessage());
		}
    	redirectAttributes.addFlashAttribute("errors", errors);
		redirectAttributes.addAttribute("codViaje", codViaje);
		return "redirect:/viaje/reserva/add";
    }
    
    @GetMapping("/viaje/reservas")
    public String getReservasAction(@RequestParam Map<String, String> params, Model model) {
    	if (!params.containsKey("codViaje") || params.get("codViaje").isEmpty()) {
    		return "redirect:/viajes";
    	}
    	
    	int codViaje = Integer.parseInt(params.get("codViaje"));
    	model.addAttribute("codViaje", codViaje);
    	
    	Viaje viaje = viajesRepository.findViajeById(codViaje);
    	model.addAttribute("reservas", viajesRepository.findReservasByViaje(viaje));
    	return "reserva/listado";
    }
}
