package org.example;

import java.util.Comparator;

public class Special implements Comparator<Mage> {



    @Override
    public int compare(Mage left, Mage right) {
        if(left.getLevel() == right.getLevel()) {
            if (left.getName()==right.getName()) {
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



            return left.getName().compareTo(right.getName());
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
}
