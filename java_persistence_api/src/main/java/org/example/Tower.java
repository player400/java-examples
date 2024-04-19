package org.example;

import jakarta.persistence.*;

import java.util.List;
import java.util.Vector;

@Entity
public class Tower {

    @Id
    private String name;

    private int height;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tower")
    private List<Mage>mages;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<Mage> getMages() {
        return mages;
    }

    public void addMage(Mage new_mage)
    {
        mages.add(new_mage);
    }

    public Tower(String name, int height) {
        this.name = name;
        this.height = height;
        mages = new Vector<>();
    }

    public Tower() {
        mages = new Vector<>();
    }
}
