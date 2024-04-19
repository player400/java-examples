package org.example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class MageRepository {
    private Collection<Mage> collection;
    public Optional<Mage> find(String name){
        Optional<Mage> result = Optional.empty();
        for(Mage mage : collection){
            if(mage.getName()==name)
            {
                result = Optional.of(mage);
            }
        }

        return result;
    }

    public void save(Mage mage) throws IllegalArgumentException {
        Optional<Mage> to_remove = find(mage.getName());
        if(to_remove.isPresent())
        {
            IllegalArgumentException e = new IllegalArgumentException("Mage of this name is already saved");
            throw e;

        }
        collection.add(mage);
    }

    public void remove(String name) throws IllegalArgumentException {
        Optional<Mage> to_remove = find(name);
        if(to_remove.isEmpty())
        {
            IllegalArgumentException e = new IllegalArgumentException("The mage does not exist");
            throw e;
        }
        collection.remove(to_remove.get());
    }

    public MageRepository() {
        this.collection = new ArrayList<>();
    }
}
