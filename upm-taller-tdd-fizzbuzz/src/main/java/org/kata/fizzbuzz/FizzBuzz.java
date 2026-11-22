package org.kata.fizzbuzz;

public class FizzBuzz {
    public String calculate(int input_number) {
        String result = input_number+"";
        if (input_number % 15== 0) result = "FizzBuzz";
        else if(input_number % 3 == 0) result = "Fizz";
        else if (input_number % 5 == 0) result= "Buzz";

        return  result;
    }
}
