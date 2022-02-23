package de.galvanize.autos;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AutomobilesController {

    AutomobilesService autosService;

    public AutomobilesController(AutomobilesService autosService){
        this.autosService = autosService;
    }

    @GetMapping("/api/autos")
    public AutomobileList getAutos(){
        return autosService.getAutos();
    }
}
