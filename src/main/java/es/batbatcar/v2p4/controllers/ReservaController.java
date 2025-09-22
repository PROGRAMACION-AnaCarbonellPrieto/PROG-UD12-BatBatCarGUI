package es.batbatcar.v2p4.controllers;

import es.batbatcar.v2p4.exceptions.ReservaAlreadyExistsException;
import es.batbatcar.v2p4.exceptions.ReservaNoValidaException;
import es.batbatcar.v2p4.exceptions.ReservaNotFoundException;
import es.batbatcar.v2p4.exceptions.ViajeNotFoundException;
import es.batbatcar.v2p4.modelo.dto.Reserva;
import es.batbatcar.v2p4.modelo.dto.viaje.Viaje;
import es.batbatcar.v2p4.modelo.repositories.ViajesRepository;
import es.batbatcar.v2p4.utils.Validator;

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
public class ReservaController {

    @Autowired
    private ViajesRepository viajesRepository;

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
    			errors.put("error", "Todos los campos tienen que estar rellenados");
    			redirectAttributes.addFlashAttribute("errors", errors);
    			redirectAttributes.addAttribute("codViaje", codViaje);
    			return "redirect:/viaje/reserva/add";
    		}
    	}
    	
    	String usuario;
    	int plazasSolicitadas;
    	try {
	    	usuario = params.get("usuario");
	    	plazasSolicitadas = Integer.parseInt(params.get("plazasSolicitadas"));
		} catch (NumberFormatException e) {
			errors.put("error", "Los campos numéricos sólo puede contener números");
			redirectAttributes.addFlashAttribute("errors", errors);
			redirectAttributes.addAttribute("codViaje", codViaje);
			return "redirect:/viaje/reserva/add";
		}
    
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
		} catch (ReservaNoValidaException | ViajeNotFoundException e) {
			errors.put("error", e.getMessage());
			redirectAttributes.addFlashAttribute("errors", errors);
    		redirectAttributes.addAttribute("codViaje", codViaje);
    		return "redirect:/viaje/reserva/add";
		}
    	
    	Reserva nuevaReserva = new Reserva(viajesRepository.getNextCodReserva(viaje), usuario, plazasSolicitadas, viaje);
    	try {
			viajesRepository.save(nuevaReserva);
			redirectAttributes.addFlashAttribute("infoMessage", "Reserva insertada con éxito");
	    	return "redirect:/viajes";
		} catch (ReservaAlreadyExistsException | ReservaNotFoundException e) {
			errors.put("error", e.getMessage());
		}
    	
    	redirectAttributes.addFlashAttribute("errors", errors);
		redirectAttributes.addAttribute("codViaje", codViaje);
		return "redirect:/viaje/reserva/add";
    }
    
    @GetMapping("/viaje/reservas")
    public String getReservasAction(@RequestParam Map<String, String> params, Model model) {
    	Viaje viaje;
    	try {
    		int codViaje = Integer.parseInt(params.get("codViaje"));
        	model.addAttribute("codViaje", codViaje);
        	viaje = viajesRepository.findViajeById(codViaje);
    	} catch (NumberFormatException | ViajeNotFoundException e) {
    		return "redirect:/viajes";
		}
		
    	model.addAttribute("reservas", viajesRepository.findReservasByViaje(viaje));
    	return "reserva/listado";
    }
    
    @GetMapping("/viaje/reserva")
    public String getDetailReservaAction(@RequestParam Map<String, String> params, Model model) {
    	Reserva reserva;
    	Viaje viaje;
    	try {
    		reserva = viajesRepository.findReservaById(params.get("codReserva"));
    		viaje = viajesRepository.findViajeById(reserva.getCodigoViaje());
		} catch (ReservaNotFoundException | ViajeNotFoundException e) {
			return "redirect:/viajes";
		}
    	
    	model.addAttribute("reserva", reserva);
		model.addAttribute("viaje", viaje);
		model.addAttribute("numReservas", viajesRepository.getNumReservasEnViaje(viaje));
		model.addAttribute("plazasDisponibles", viajesRepository.getNumPlazasDisponiblesEnViaje(viaje));
		
    	return "reserva/reserva_detalle";
    }
    
    @GetMapping("/viaje/reserva/cancel")
    public String getCancelReservaAction(@RequestParam Map<String, String> params, RedirectAttributes redirectAttributes) {
    	String codReserva = params.get("codReserva");
    	try {
			viajesRepository.remove(viajesRepository.findReservaById(codReserva));
			redirectAttributes.addFlashAttribute("infoMessage", "Reserva cancelada con éxito");
		} catch (ReservaNotFoundException e) {
			redirectAttributes.addFlashAttribute("infoMessage", e.getMessage());
		}
    	return "redirect:/viajes";
    }
}
