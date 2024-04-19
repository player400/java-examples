package org.example;

import java.util.Map;

public class Main {
    public static void main(String[] args) {

        int mode = Integer.parseInt(args[0]);

        Special special = new Special();

        Mage king_mage = new Mage("king of kings", 9999999, 999999999999999.999999999, mode, special);
        Mage one = new Mage("a", 2, 4.5, mode, special);
        Mage oneone = new Mage( "c", 7, 1.2, mode, special);
        one.addApprentice(oneone);
        Mage oneoneone = new Mage("c", 3, 6.4, mode, special);
        oneone.addApprentice(oneoneone);
        Mage onetwo = new Mage("d", 3, 5.1, mode, special);
        one.addApprentice(onetwo);
        Mage onetwoone = new Mage("r", 5, 3.1, mode, special);
        onetwo.addApprentice(onetwoone);
        Mage onetwotwo = new Mage("a", 5, 3.1, mode, special);
        onetwo.addApprentice(onetwotwo);
        Mage onetwothree = new Mage("b", 3, 3.5, mode, special);
        onetwo.addApprentice(onetwothree);
        king_mage.addApprentice(one);

        Mage two = new Mage("a", 1, 4.5, mode, special);
        Mage twoone = new Mage("y", 5, 13.1, mode, special);
        two.addApprentice(twoone);
        Mage twotwo = new Mage("y", 5, 8.9, mode, special);
        two.addApprentice(twotwo);

        king_mage.addApprentice(two);
        king_mage.print("");

        Map<Mage, Integer> stats = king_mage.count_apprentices();

        stats.forEach((Mage mage, Integer stat) -> System.out.println(mage +":"+stat));
    }



}