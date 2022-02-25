package de.galvanize.autos;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AutomobileService {

    AutomobileRepository automobileRepository;

    // constructor injection
    public AutomobileService(AutomobileRepository automobileRepository) {
        this.automobileRepository = automobileRepository;
    }

    public AutomobileList getAutomobiles() {
        // Query: select * from autos;
        // Put that in a list
        // return a new AutoList with list
        return new AutomobileList(automobileRepository.findAll());
    }

    //TODO
    public AutomobileList getAutomobiles(String searchParam) {
        return null;
    }

    public AutomobileList getAutomobiles(String color, String make) {
        List<Automobile> automobileList = automobileRepository.findByColorContainsAndMakeContains(color, make);
        if (!automobileList.isEmpty()) {
            return new AutomobileList(automobileList);
        }
        return null;
    }

    public Automobile addAutomobile(Automobile automobile) {
        return automobileRepository.save(automobile);
    }

    public Automobile getAutomobile(String vin) {
        return automobileRepository.findByVin(vin).orElse(null);
    }

    public Automobile updateAutomobile(String vin, String color, String owner) {
        Optional<Automobile> optionalAutomobile = automobileRepository.findByVin(vin);
        if (optionalAutomobile.isPresent()) {
            optionalAutomobile.get().setColor(color);
            optionalAutomobile.get().setOwner(owner);
            return automobileRepository.save(optionalAutomobile.get());
        }
        return null;
    }

    public void deleteAutomobile(String vin) {
        Optional<Automobile> optionalAutomobile = automobileRepository.findByVin(vin);
        if (optionalAutomobile.isPresent()){
            automobileRepository.delete(optionalAutomobile.get());
        } else {
            throw new AutomobileNotFountException();
        }
    }
}
