package org.example;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MageRepositoryTest {
    private String MAGE_NAME = "test_mage";
    private int MAGE_LEVEL = 3;

    @Test
    void findExists() {
        MageRepository tested = new MageRepository();
        try{
            tested.save(new Mage(MAGE_NAME, MAGE_LEVEL));
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error");
        }

        assertTrue(tested.find(MAGE_NAME).isPresent());
        if(tested.find(MAGE_NAME).isEmpty())
        {
            return;
        }
        assertEquals(MAGE_NAME, tested.find(MAGE_NAME).get().getName());
        assertEquals(MAGE_LEVEL, tested.find(MAGE_NAME).get().getLevel());
    }

    @Test
    void findNotExists(){
        MageRepository tested = new MageRepository();
        try{
            tested.save(new Mage(MAGE_NAME, MAGE_LEVEL));
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error");
        }
        assertTrue(tested.find("random other mage").isEmpty());

    }
    @Test
    void saveExists() {
        MageRepository tested = new MageRepository();
        try{
            tested.save(new Mage(MAGE_NAME, MAGE_LEVEL));
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error");
        }

        try{
            tested.save(new Mage(MAGE_NAME, MAGE_LEVEL));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Mage of this name is already saved", e.getMessage() );
        }
    }

    @Test
    void saveNotExists() {
        MageRepository tested = new MageRepository();

        try{
            tested.save(new Mage(MAGE_NAME, MAGE_LEVEL));
            Optional<Mage> mage= tested.find(MAGE_NAME);
            assertTrue(mage.isPresent());
            if(mage.isEmpty())
            {
                fail();
            }
            assertEquals(MAGE_LEVEL, mage.get().getLevel());
            assertEquals(MAGE_NAME, mage.get().getName());
        } catch (IllegalArgumentException e) {
            fail();
        }
    }
    @Test
    void removeExists() {
        MageRepository tested = new MageRepository();
        try{
            tested.save(new Mage(MAGE_NAME, MAGE_LEVEL));
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error");
        }

        try{
            tested.remove(MAGE_NAME);
            Optional<Mage> mage= tested.find(MAGE_NAME);
            assertTrue(mage.isEmpty());
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    void removeNotExists() {
        MageRepository tested = new MageRepository();
        try{
            tested.save(new Mage(MAGE_NAME, MAGE_LEVEL));
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error");
        }

        try{
            tested.remove("Non-existent name");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("The mage does not exist", e.getMessage());
        }
    }
}