package org.kata.fizzbuzz;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit test for simple App. */
public class FizzBuzzTest {
  // inicializamos la variable para toda la clase
  FizzBuzz fizzBuzz;

  // el decorador @BeforeEach()
  // es para que inicie la variable antes de ejecutar cada test
  @BeforeEach()
  public void setup() {
    fizzBuzz = new FizzBuzz();
  }

  @Test()
  public void givenMultiple3_returnFizz() {
    assertEquals("Fizz", fizzBuzz.calculate(3));
  }

  @Test()
  public void givenMultiple5_returnBuzz(){ assertEquals("Buzz", fizzBuzz.calculate(5));}
  @Test()
  public void givenMultiple3and5_returnBuzz(){ assertEquals("FizzBuzz", fizzBuzz.calculate(15));}

  @Test()
  public void givenNotMultiple3and5_returnNumber(){ assertEquals("37", fizzBuzz.calculate(37));}
}
