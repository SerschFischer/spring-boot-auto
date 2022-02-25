package de.galvanize.autos;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AutomobileController {

    private final AutomobileService automobileService;

    public AutomobileController(AutomobileService automobileService) {
        this.automobileService = automobileService;
    }

    @GetMapping("/api/autos")
    public ResponseEntity<AutomobileList> getAutos(@RequestParam(required = false) String color,
                                                   @RequestParam(required = false) String make) {
        AutomobileList automobileList;
        if (color == null && make == null) {
             automobileList = automobileService.getAutomobiles();
        } else if (color != null && make == null){
            automobileList = automobileService.getAutomobiles(color);
        }else if (color == null && make != null){
            automobileList = automobileService.getAutomobiles(make);
        } else {
            automobileList = automobileService.getAutomobiles(color, make);
        }
        return automobileList.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(automobileList);
    }

    @GetMapping("/api/autos/{vin}")
    public Automobile getAuto(@PathVariable String vin){
        return automobileService.getAutomobile(vin);
    }

    @PatchMapping("/api/autos/{vin}")
    public Automobile updateAuto(@PathVariable String vin,
                                 @RequestBody UpdateOwnerRequest update){
        Automobile automobile = automobileService.updateAutomobile(vin, update.getColor(), update.getOwner());
        automobile.setColor(update.getColor());
        automobile.setOwner(update.getOwner());
        return automobile;
    }

    @PostMapping("/api/autos")
    public Automobile addAuto(@RequestBody Automobile automobile){
        return automobileService.addAutomobile(automobile);
    }

    @DeleteMapping("/api/autos/{vin}")
    public ResponseEntity deleteAuto(@PathVariable String vin){
        try{

        automobileService.deleteAutomobile(vin);
        } catch (AutoNotFountException autoNotFountException){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.accepted().build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void invalidAutomobileHandler(InvalidAutomobileException invalidAutomobileException){
    }


}
