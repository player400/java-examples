package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;
import java.util.Optional;

public class MageController {
    private MageRepository repository;
    public MageController(MageRepository repository) {
        this.repository = repository;
    }
    public String find(String name) {
        Optional<Mage> result = repository.find(name);
        if(result.isEmpty())
        {
            return "not_found";
        }
        var mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(result.get());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    public String delete(String name) {
        try{
            repository.remove(name);

        }
        catch(IllegalArgumentException e){
            if(Objects.equals(e.getMessage(), "The mage does not exist")){
                return("not_found");
            }
            throw new RuntimeException(e);
        }
        return "done";
    }
    public String save(String name, String level) {
        Mage mage = new Mage(name, Integer.parseInt(level));
        try{
            repository.save(mage);
        }
        catch (IllegalArgumentException e){
            if(Objects.equals(e.getMessage(), "Mage of this name is already saved")){
                return("bad_request");
            }
            throw new RuntimeException(e);
        }
        return "done";
    }
}
