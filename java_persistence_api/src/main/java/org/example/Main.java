package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import static jakarta.persistence.Persistence.createEntityManagerFactory;
import static org.hibernate.cfg.SchemaToolingSettings.JAKARTA_HBM2DDL_DATABASE_ACTION;
import static org.hibernate.tool.schema.Action.CREATE;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class Main {

    private static void runQuery(String query, EntityManagerFactory factory)
    {
        var entityManager = factory.createEntityManager();
        var transaction = entityManager.getTransaction();
        transaction.begin();
        System.out.println(entityManager.createQuery(query).getResultList());
        transaction.commit();
        entityManager.close();
    }

    private static void addTower(Tower tower, EntityManagerFactory factory)
    {
        var entityManager = factory.createEntityManager();
        var transaction = entityManager.getTransaction();

        List<Mage>mages = tower.getMages();

        transaction.begin();
        entityManager.persist(tower);
        for(int i=0;i<mages.size();i++)
        {
            entityManager.persist(mages.get(i));
        }
        transaction.commit();



        entityManager.close();
    }

    private static void removeTower(String tower, EntityManagerFactory factory)
    {
        var entityManager = factory.createEntityManager();
        var transaction = entityManager.getTransaction();

        //List<String>mages = entityManager.createQuery("select name from Mage" +
          //      " where tower.name = \""+tower+"\"").getResultList();

        transaction.begin();
        entityManager.remove(entityManager.find(Tower.class, tower));
        /*for(int i=0;i<mages.size();i++)
        {
            entityManager.remove(entityManager.find(Mage.class, mages.get(i)));
        }*/
        transaction.commit();



        entityManager.close();
    }


    private static void removeMage(String mage, EntityManagerFactory factory)
    {
        var entityManager = factory.createEntityManager();
        var transaction = entityManager.getTransaction();

        transaction.begin();
        entityManager.remove(entityManager.find(Mage.class, mage));

        transaction.commit();



        entityManager.close();
    }


    public static void main(String[] args) {

        Tower black_tower = new Tower("Black Tower of Doom", 60);
        Tower white_tower = new Tower("White Tower of the West", 230);
        Tower red_tower = new Tower("Red Tower of the Wizard Clan", 50);

        black_tower.addMage(new Mage("Black King", 9, black_tower));
        white_tower.addMage(new Mage("White Emperor", 12, white_tower));
        red_tower.addMage(new Mage("Red Chief", 7, red_tower));

        black_tower.addMage(new Mage("Black Knight 1", 3, black_tower));
        black_tower.addMage(new Mage("Black Knight 2", 3, black_tower));

        white_tower.addMage(new Mage("White Guardian 1", 4, white_tower));
        white_tower.addMage(new Mage("White Guardian 2", 4, white_tower));

        red_tower.addMage(new Mage("Red Warrior 1", 4, red_tower));
        red_tower.addMage(new Mage("Red Warrior 2", 4, red_tower));

        Vector<Tower> user_towers = new Vector<>();

        var factory = createEntityManagerFactory("mage-kingdom");

        addTower(black_tower, factory);
        addTower(white_tower, factory);
        addTower(red_tower, factory);

        while(true)
        {
            System.out.println("");
            System.out.println("How many towers do you want to add?");
            Scanner scan = new Scanner(System.in);
            int t = Integer.parseInt(scan.nextLine());
            for(int i=0;i<t;i++)
            {
                System.out.println("Give unique name for the tower:");
                String name = scan.nextLine();
                System.out.println("What height do you want "+name+" to be?");
                int height = Integer.parseInt(scan.nextLine());
                Tower new_tower = new Tower(name, height);
                System.out.println("How many mages are supposed to live int the "+name+"?");
                int m = Integer.parseInt(scan.nextLine());
                for(int j=0;j<m;j++)
                {
                    System.out.println("Give unique name for the mage:");
                    String mage_name = scan.nextLine();
                    System.out.println("What level do you want "+mage_name+" to be?");
                    int level = Integer.parseInt(scan.nextLine());
                    new_tower.addMage(new Mage(mage_name, level, new_tower));
                    System.out.println("Mage by the name of "+mage_name+" has thereby been added to the tower know as "+name+"!");
                    System.out.println("");
                }
                user_towers.add(new_tower);
                addTower(new_tower, factory);
                System.out.println("The tower known by the name "+name+" has been persisted!");
                System.out.println("");
            }

            


            System.out.println("Mages:");
            runQuery("select name ||' of level '|| level ||' from the glorious '|| tower.name from Mage", factory);
            System.out.println("Towers:");
            runQuery("select name ||' measuring '|| height from Tower", factory);

            System.out.println("");
            System.out.println("How many mages do you want to remove?");

            int m = Integer.parseInt(scan.nextLine());


            if(m>0)
            {
                for(int i=0;i<m;i++)
                {
                    System.out.println("Give name of the mage to remove:");
                    String name = scan.nextLine();
                    removeMage(name, factory);
                    System.out.println("Mage by the name of "+name+" has been exiled!");
                }
                continue;
            }
            System.out.println("");
            System.out.println("How many towers do you want to remove?");
            int n = Integer.parseInt(scan.nextLine());

            if(n>0)
            {
                for(int i=0;i<n;i++)
                {
                    System.out.println("Give name of the tower to remove:");
                    String name = scan.nextLine();
                    removeTower(name, factory);
                    System.out.println("Tower by the name of "+name+" has been demolished!");
                }
                continue;
            }

            System.out.println("");


            System.out.println("What information do you want to retrieve?");
            System.out.println("1. Mages of level greater than X.");
            System.out.println("2. Towers lower than X.");
            System.out.println("3. Mages of level greater than  X from the tower Y.");
            System.out.println("4. None (end program).");

            n = Integer.parseInt(scan.nextLine());

            if(n==4)
            {
                break;
            }
            System.out.println("Give X:");
            int x = Integer.parseInt(scan.nextLine());

            switch (n)
            {
                case 1: {
                    System.out.println(" ");
                    System.out.println("Mages of level greater than "+x+":");
                    runQuery("select name ||': '|| level from Mage \n where level > "+x, factory);
                    break;
                }
                case 2: {
                    System.out.println(" ");
                    System.out.println("Towers lower than "+x+":");
                    runQuery("select name ||': '|| height from Tower \n where height < "+x, factory);
                    break;
                }
                case 3:
                    System.out.println("Give Y:");
                    String y = scan.nextLine();
                    System.out.println(" ");
                    System.out.println("Mages of level greater than "+x+" from the "+y+":");
                    runQuery("select name ||': '|| level from Mage \n where level > "+x+" and tower.name=\""+y+"\"", factory);
            }




        }

        System.out.println("Removing all towers and mages...");
        System.out.println("");

        var entityManager = factory.createEntityManager();
        var transaction = entityManager.getTransaction();
        transaction.begin();

        List<String>towers = entityManager.createQuery("select name from Tower").getResultList();
        transaction.commit();
        entityManager.close();

        for(String tower : towers)
        {
            removeTower(tower, factory);
        }

        System.out.println(" ");
        System.out.println("Mages after removal:");
        runQuery("select name ||' of level '|| level ||' from the glorious '|| tower.name from Mage", factory);
        System.out.println("Towers after removal:");
        runQuery("select name ||' measuring '|| height from Tower", factory);

    }
}