
package br.com.controller;

import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author migue
 */
public class PeriodoRelatorioTest {
    
    public PeriodoRelatorioTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getDataInicial method, of class PeriodoRelatorio.
     */
    @Test
    public void testGetDataInicial() {
        System.out.println("getDataInicial");
        PeriodoRelatorio instance = new PeriodoRelatorio();
        LocalDate expResult = null;
        LocalDate result = instance.getDataInicial();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setDataInicial method, of class PeriodoRelatorio.
     */
    @Test
    public void testSetDataInicial() {
        System.out.println("setDataInicial");
        LocalDate dataInicial = null;
        PeriodoRelatorio instance = new PeriodoRelatorio();
        instance.setDataInicial(dataInicial);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDataFinal method, of class PeriodoRelatorio.
     */
    @Test
    public void testGetDataFinal() {
        System.out.println("getDataFinal");
        PeriodoRelatorio instance = new PeriodoRelatorio();
        LocalDate expResult = null;
        LocalDate result = instance.getDataFinal();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setDataFinal method, of class PeriodoRelatorio.
     */
    @Test
    public void testSetDataFinal() {
        System.out.println("setDataFinal");
        LocalDate dataFinal = null;
        PeriodoRelatorio instance = new PeriodoRelatorio();
        instance.setDataFinal(dataFinal);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isTodoPeriodo method, of class PeriodoRelatorio.
     */
    @Test
    public void testIsTodoPeriodo() {
        System.out.println("isTodoPeriodo");
        PeriodoRelatorio instance = new PeriodoRelatorio();
        boolean expResult = false;
        boolean result = instance.isTodoPeriodo();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTodoPeriodo method, of class PeriodoRelatorio.
     */
    @Test
    public void testSetTodoPeriodo() {
        System.out.println("setTodoPeriodo");
        boolean todoPeriodo = false;
        PeriodoRelatorio instance = new PeriodoRelatorio();
        instance.setTodoPeriodo(todoPeriodo);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
