package org.example;

import java.util.*;

public class Mage implements Comparable<Mage>{

    private String name;

    Comparator comparator = null;
    private int level;
    private double power;
    private Set<Mage> apprentices;

    int sorting_mode;

    public String getName()
    {
        return name;
    }

    public void addApprentice(Mage apprentice)
    {
        apprentices.add(apprentice);
    }

    private int add_to_count(Map<Mage, Integer> count)
    {
        Integer counter = 0 ;
        for(Mage apprentice : apprentices)
        {
            counter+=apprentice.add_to_count(count);
        }
        count.put(this, counter);
        return counter+1;
    }

    public Map<Mage, Integer> count_apprentices()
    {

        Map<Mage, Integer> my_map;
        if(sorting_mode==0)
        {
            my_map = new HashMap<Mage, Integer>();
        }
        else
        {
            if(sorting_mode==2)
            {
                my_map = new TreeMap<Mage, Integer>(comparator);
            }
            else
            {
                my_map = new TreeMap<Mage, Integer>();
            }
        }
        this.add_to_count(my_map);

        return my_map;
    }

    public int getLevel()
    {
        return level;
    }

    public double getPower()
    {
        return power;
    }

    @Override
    public String toString()
    {
        return "Mage(name='"+name+"', level="+Integer.toString(level)+", power="+Double.toString(power)+")";
    }


    public boolean equals(Mage o) {
        if(compareTo(o)==0)
        {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    Mage(String name, int level, double power, int mode, Special special)
    {
        this.name=name;
        this.level=level;
        comparator = special;
        this.power = power;
        sorting_mode = mode;
        if(mode==0)
        {
            apprentices = new HashSet<>();
        }
        else
        {
            if(mode==1)
            {
                apprentices = new TreeSet<>();
            }
            else
            {
                apprentices = new TreeSet<>(special);
            }
        }

    }

    Mage(String name, int level, double power, int mode)
    {
        this.name=name;
        this.level=level;
        this.power = power;
        if(mode==0)
        {
            apprentices = new HashSet<>();
        }
        else
        {
            if(mode==1)
            {
                apprentices = new TreeSet<>();
            }
            else
            {
                apprentices = new TreeSet<>(new Special());
            }
        }

    }

    public void print(String prefix)
    {
        System.out.print(prefix + this.toString()+"\n");


        String new_prefix = "-"+prefix;
        for(Mage mage : apprentices)
        {
            mage.print(new_prefix);
        }
    }

    @Override
    public int compareTo(Mage right) {
        Mage left = this;
        if(left.getName()==right.getName()) {
            if (left.getLevel() == right.getLevel()) {
                if (left.getPower() == right.getPower())
                {
                    return 0;
                }

                if(left.getPower() < right.getPower())
                {
                    return -1;
                }
                else
                {
                    return 1;
                }
            }

            if(left.getLevel() < right.getLevel())
            {
                return -1;
            }
            else
            {
                return 1;
            }


        }
        return left.getName().compareTo(right.getName());
    }
}

