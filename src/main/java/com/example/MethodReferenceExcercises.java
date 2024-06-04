package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MethodReferenceExcercises {

    /*
    Use a method reference to convert each element in a list of strings to uppercase.
     */
    public static List<String> convertToUpperCase(List<String> stringList) {
        return stringList.stream().map(String::toUpperCase).collect(Collectors.toList());
    }

    /*
    Write a method that returns the length of a string. Use a method reference to pass this method to a
    Function to calculate the length of each string in a list.
    */
    //static Function<String,Integer> stringLength = String::length;
    //static Function<List<String>, List<Integer>> lengthOfStringsInList = list -> list.stream().map(stringLength::apply).collect(Collectors.toList());
    static Function<List<String>, List<Integer>> lengthOfStringsInList = list -> list.stream().map(String::length).collect(Collectors.toList());

    /*
    Implement a class StringUtils with a static method startsWithUppercase(String str) that returns true
    if a string starts with an uppercase letter. Use a method reference to pass this method to a Predicate
    to filter a list of strings.
    */
    public static class StringUtils{
        public static boolean startsWithUppercase(String str){
            return str != null && !str.isEmpty() && Character.isUpperCase(str.charAt(0));
        }
    }
    static Predicate<String> upper = StringUtils::startsWithUppercase;

    /*
    Create a class MathUtils with a static method cube(int x) that returns the cube of a number. Use a method
    reference to pass this method to a Function to calculate the cube of each element in a list of integers.
    */
    public static class MathUtils{
        public static int cube(int number) {
            return number * number * number;
        }
    }
    static Function<List<Integer>, List<Integer>> cubesOfLists = list -> list.stream().map(MathUtils::cube).collect(Collectors.toList());

    /*
    Write a method that compares two strings ignoring case. Use a method reference to pass this method to a
    Comparator to sort a list of strings alphabetically.
     */
    public static int compareIgnoreCase(String a, String b) {
        return a.compareToIgnoreCase(b);
    }
    public static Comparator<String> comparator = String::compareToIgnoreCase;
    public static List<String> sortIgnoreCase(List<String> stringList) {
        return stringList.stream().sorted(comparator).collect(Collectors.toList());
    }

    public static List<String> sortIgnoreCase2(List<String> stringList) {
        return stringList.stream().sorted(String::compareToIgnoreCase).collect(Collectors.toList());
    }


    public static void main(String[] args) {

        List<String> stringList = new ArrayList<String>(Arrays.asList("This","iS","A","test"));
        List<Integer> intList = new ArrayList<Integer>(Arrays.asList(1,2,3,4));
        System.out.println(convertToUpperCase(stringList));
        System.out.println(lengthOfStringsInList.apply(stringList));
        System.out.println(stringList.stream().filter(upper).collect(Collectors.toList()));
        System.out.println(cubesOfLists.apply(intList));
        System.out.println(sortIgnoreCase(stringList));
        System.out.println(sortIgnoreCase2(stringList));

    }
}
