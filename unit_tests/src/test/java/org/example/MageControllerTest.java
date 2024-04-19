package org.example;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class MageControllerTest {
    @Mock
    MageRepository mockMageRepository;

    private static final String MAGE_NAME = "magus";
    private static  String MAGE_LEVEL;
    private static int LVL = 3;

    private static String MAGE_JSON;
    private AutoCloseable closeable;

    private static boolean areMagesEqual(Mage left, Mage right)
    {
        if(left.getName()==right.getName() && left.getLevel()==right.getLevel()) {
            return true;
        }
        return false;
    }
    @Test
    void findExists() {
        Mockito.when(mockMageRepository.find(MAGE_NAME)).thenReturn(Optional.of(new Mage(MAGE_NAME, LVL)));
        MageController tested = new MageController(mockMageRepository);
        String result = tested.find(MAGE_NAME);
        assertEquals( MAGE_JSON, result);
        Mockito.verify(mockMageRepository, times(1)).find(MAGE_NAME);
    }
    @Test
    void findNotExists() {
        Mockito.when(mockMageRepository.find(MAGE_NAME)).thenReturn(Optional.empty());
        MageController tested = new MageController(mockMageRepository);
        String result = tested.find(MAGE_NAME);
        assertEquals( "not_found", result);
        Mockito.verify(mockMageRepository, times(1)).find(MAGE_NAME);
    }

    @Test
    void deleteExists() {
        MageController tested = new MageController(mockMageRepository);
        String result = tested.delete(MAGE_NAME);
        assertEquals( "done", result);
        Mockito.verify(mockMageRepository, times(1)).remove(MAGE_NAME);
    }

    @Test
    void deleteNotExists() {
        Mockito.doThrow(new IllegalArgumentException("The mage does not exist")).when(mockMageRepository).remove(MAGE_NAME);
        MageController tested = new MageController(mockMageRepository);
        String result = tested.delete(MAGE_NAME);
        assertEquals( "not_found", result);
        Mockito.verify(mockMageRepository, times(1)).remove(MAGE_NAME);
    }

    @Test
    void saveExists() {

        Mockito.doThrow(new IllegalArgumentException("Mage of this name is already saved")).when(mockMageRepository).save(any(Mage.class));
        MageController tested = new MageController(mockMageRepository);
        String result = tested.save(MAGE_NAME, MAGE_LEVEL);
        assertEquals( "bad_request", result);
        ArgumentCaptor<Mage> argument = ArgumentCaptor.forClass(Mage.class);
        Mockito.verify(mockMageRepository, times(1)).save(argument.capture());
        assertEquals(MAGE_NAME, argument.getValue().getName());
        assertEquals(LVL, argument.getValue().getLevel());
    }
    @Test
    void saveNotExists() {
        MageController tested = new MageController(mockMageRepository);
        String result = tested.save(MAGE_NAME, MAGE_LEVEL);
        assertEquals( "done", result);
        ArgumentCaptor<Mage> argument = ArgumentCaptor.forClass(Mage.class);
        Mockito.verify(mockMageRepository, times(1)).save(argument.capture());
        assertEquals(MAGE_NAME, argument.getValue().getName());
        assertEquals(LVL, argument.getValue().getLevel());
    }

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void cleanUp() throws Exception {
        closeable.close();
    }

    @BeforeAll
    static void beforeAll() {
        MAGE_LEVEL = Integer.toString(LVL);
        MAGE_JSON = "{\"name\":\""+MAGE_NAME+"\",\"level\":"+MAGE_LEVEL+"}";
    }
}