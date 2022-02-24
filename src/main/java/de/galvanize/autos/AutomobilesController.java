package de.galvanize.autos;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        } else if (color != null && make == null){
            automobileList = automobilesService.getAutos(color);
        }else if (color == null && make != null){
            automobileList = automobilesService.getAutos(make);
        } else {
            automobileList = automobilesService.getAutos(color, make);
        }
        return automobileList.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(automobileList);
    }

    @GetMapping("/api/autos/{vin}")
    public Automobile getAuto(@PathVariable String vin){
        return automobilesService.getAuto(vin);
    }

    @PostMapping("/api/autos")
    public Automobile addAuto(@RequestBody Automobile automobile){
        return automobilesService.addAuto(automobile);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void invalidAutomobileHandler(InvalidAutomobileException invalidAutomobileException){
    }
}
