package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;

public class FunctionalInterfacesExercises2 {
    /*
    Write a BiConsumer that takes two integers and prints their sum.
    */
    public static BiConsumer<Integer, Integer> printSum = (n1, n2) -> System.out.println(STR."The sum of \{n1} and \{n2} is \{n1+n2}");

    /*
    Implement a BiFunction that takes two strings and concatenates them.
    */
    public static BiFunction<String, String, String> concat = (a, b) -> a + b;

    /*
    Create a BiPredicate that checks if the sum of two integers is even.
    */
    public static BiPredicate<Integer, Integer> iSumEven = (n1, n2) -> (n1+n2)%2 == 0;

    /*
    Write a BinaryOperator that multiplies two integers.
    */
    public static BinaryOperator<Integer> multiply = (a, b) -> a * b;

    /*
    Write a BiConsumer that takes a string and an integer, and prints the string repeated that number of times.
    */
    public static BiConsumer<String, Integer> printX = (str, num) -> System.out.println(str.repeat(num));

    /*
    Implement a BiFunction that takes a string and an integer, and returns the string repeated that number of times.
    */
    public static BiFunction<String, Integer, String> repeatX = (a, b) -> a.repeat(b);

    /*
    Create a BiPredicate that checks if the length of a string is greater than a given integer.
    */
    public static BiPredicate<String, Integer> isLengthGreater = (str, n) -> str.length() > n;

    /*
    Write a BinaryOperator that concatenates two strings with a space in between.
    */
    public static BinaryOperator<String> concatenate = (a, b) -> STR."\{a} \{b}";

    /*
    Implement a BiFunction that takes two lists of strings and returns a list containing the element-wise concatenation of the two lists.
    Handle error in case the lists don't have the same size.
    */
    public static BiFunction<List<String>, List<String>, List<String>> concatenateLists = (List<String> list1,List<String> list2) ->
    {
        List<String> result = new ArrayList<>();
        if (list1.size() != list2.size()) {
            throw new RuntimeException(STR."Cannot concatenate a list of \{list1.size()} elements with a list of \{list2.size()} elements.");
        } else {
            for (int i = 0; i < list1.size(); i++) {
                result.add(concat.apply(list1.get(i), list2.get(i)));
            }
        }
        return result;
    };


    public static void main(String[] args) {
        printSum.accept(4,9);
        System.out.println(concat.apply("This is a ", "concatenated sentence."));
        Integer n1 = 3;
        Integer n2 = 5;
        Integer n3 = 2;
        System.out.println(STR."Is the sum of \{n1} and \{n2} even? \{iSumEven.test(n1,n2)}");
        System.out.println(STR."Is the sum of \{n1} and \{n3} even? \{iSumEven.test(n1,n3)}");
        System.out.println(STR."The product of \{n1} and \{n2} is \{multiply.apply(n1,n2)}");
        printX.accept("String", 4);
        System.out.println(repeatX.apply("String", 3));
        String str = "This is a test";
        Integer n4 = 20;
        System.out.println(STR."Is the length of '\{str}' greater than \{n4}? \{isLengthGreater.test(str,n4)}");
        System.out.println(STR."Is the length of '\{str}' greater than \{n2}? \{isLengthGreater.test(str,n2)}");
        System.out.println(concatenate.apply("parte1","parte2"));
        List<String> list1 = Arrays.asList("uno","dos","tres","cuatro");
        List<String> list2 = Arrays.asList("1","2","3","4");
        List<String> list3 = concatenateLists.apply(list1,list2);
        list3.forEach(System.out::println);
        List<String> list4 = Arrays.asList("1","2");
        List<String> list5 = concatenateLists.apply(list1,list4);
    }

}
