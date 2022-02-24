package de.galvanize.autos;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AutomobilesController {

    AutomobilesService automobilesService;

    public AutomobilesController(AutomobilesService automobilesService){
        this.automobilesService = automobilesService;
    }

    @GetMapping("/api/autos")
    public ResponseEntity<AutomobileList> getAutos(){
        AutomobileList automobileList = automobilesService.getAutos();
        return automobileList.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(automobileList);
    }
}
