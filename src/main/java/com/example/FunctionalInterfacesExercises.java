package com.example;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.*;
import java.util.stream.Collectors;

public class FunctionalInterfacesExercises {
    /*
    Write a Consumer that prints the square of each element in a list of integers.
    */
    static Consumer<List<Integer>> square = num -> num.forEach(z -> System.out.print(STR."\{z * z} "));

    /*
    Implement a Supplier that generates a random number between 1 and 100.
    */
    static Supplier<Integer> randomValue = () -> ThreadLocalRandom.current().nextInt(1, 101);

    /*
    Create a Function that converts a String to uppercase.
    */
    static Function<String,String> upper = String::toUpperCase;

    /*
    Write a Predicate to check if a given number is even.
    */
    static Predicate<Integer> isEven = i -> i % 2 == 0;

    /*
    Implement a UnaryOperator that increments each element in a list of integers by 1.
    */
    static UnaryOperator<List<Integer>> incrementByOne = list -> list.stream().map(x -> x+1).collect(Collectors.toList());

    public static void main(String[] args) {

        List<Integer> intList = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        square.accept(intList);
        System.out.printf("\nRandom number between 1 and 100: %d%n",randomValue.get());
        System.out.println(upper.apply("an entire sentence"));
        System.out.println("3 is even? " + isEven.test(3));
        System.out.println("2 is even? " + isEven.test(2));
        System.out.println(incrementByOne.apply(intList));

    }
}
