package es.batbatcar.v2p4.controllers;

import es.batbatcar.v2p4.modelo.repositories.ViajesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ReservaController {

    @Autowired
    private ViajesRepository viajesRepository;

}
