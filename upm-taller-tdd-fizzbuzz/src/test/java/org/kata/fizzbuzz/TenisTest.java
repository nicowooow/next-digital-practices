package org.kata.fizzbuzz;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TenisTest {

    Tenis tenis1;
    Tenis tenis2;

    @BeforeEach()
    public void setup(){
        tenis1 = new Tenis();
        tenis2 = new Tenis();
    }

    @Test()
    public void GanoYo(){
        tenis1.SumarPunto();
        tenis1.SumarPunto();
        tenis1.SumarPunto();
        assertEquals("yo", tenis1.Ganador(tenis2.puntos));
    }
    @Test()
    public void GanasTu(){
        tenis2.SumarPunto();
        tenis2.SumarPunto();
        tenis2.SumarPunto();
        assertEquals("tu", tenis1.Ganador(tenis2.puntos));

    }

    @Test()
    public void Ventaja(){}
    @Test()
    public void Gana(){}
    @Test()
    public void Deauce40(){}
    @Test()
    public void Deauce(){}
}
