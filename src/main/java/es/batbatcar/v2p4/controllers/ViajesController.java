package es.batbatcar.v2p4.controllers;

import es.batbatcar.v2p4.modelo.repositories.ViajesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViajesController {

    @Autowired
    private ViajesRepository viajesRepository;
    
    /**
     * Endpoint que muestra el listado de todos los viajes disponibles
     *
     * */
    @GetMapping("viajes")
    public String getViajesAction(Model model) {
        model.addAttribute("viajes", viajesRepository.findAll());
        model.addAttribute("titulo", "Listado de viajes");
        return "viaje/listado";
    }
}
