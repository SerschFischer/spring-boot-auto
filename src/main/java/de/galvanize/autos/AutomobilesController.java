package de.galvanize.autos;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AutomobilesController {

    private final AutomobilesService automobilesService;

    public AutomobilesController(AutomobilesService automobilesService) {
        this.automobilesService = automobilesService;
    }

    @GetMapping("/api/autos")
    public ResponseEntity<AutomobileList> getAutos(@RequestParam(required = false) String color,
                                                   @RequestParam(required = false) String make) {
        AutomobileList automobileList;
        if (color == null && make == null) {
             automobileList = automobilesService.getAutos();
        } else {
            automobileList = automobilesService.getAutos(color, make);
        }
        return automobileList.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(automobileList);
    }
}
