package com.example;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class StreamApiExercices {
    static  Logger log = Logger.getLogger(StreamApiExercices.class.getName());
    // JAVA 8
    /*
    ### flatMap()
    1. Given a list of lists of integers, create a single list containing all the integers.
    2. Given a list of sentences (strings), create a list of all words (strings).
    ### groupingBy()
    1. Given a list of strings, group them by their length.
    2. Given a list of integers, group them into even and odd numbers.
    ### counting()
    1. Given a list of strings, count the number of strings that start with a given letter.
    2. Given a list of integers, count how many of them are greater than a given value.
    */
    public static List<Integer> flatMapExample1(List<List<Integer>> input) {
        return input.stream().flatMap(Collection::stream).toList();
    }
    public static List<String> flatMapExample2(List<String> input) {
        return  input.stream().flatMap(s -> Arrays.stream(s.split(" "))).toList();
    }
    public static Map<Integer,List<String>> groupByExample1(List<String> input) {
        return input.stream().collect(groupingBy(String::length));
    }
    public static Map<String,List<Integer>> groupByExample2(List<Integer> input) {
        return input.stream().collect(groupingBy(i -> i%2==0?"even":"odd"));
    }
    public static Long countingExample1(List<String> input, Character c) {
        return input.stream().filter(str -> str.toLowerCase().charAt(0)==Character.toLowerCase(c)).collect(Collectors.counting());
    }
    public static Long countingExample2(List<Integer> input, Integer num) {
        return input.stream().filter(n -> n>num).collect(Collectors.counting());
    }

    // JAVA 9
    /*
    #### takeWhile()
    1. Given a list of integers sorted in ascending order, use takeWhile to collect all numbers less than 50.
    2. Given a list of strings sorted alphabetically, use takeWhile to collect all strings that start with a letter
       before 'M'.
    ### dropWhile()
    1. Given a list of integers sorted in ascending order, use dropWhile to skip numbers less than 50 and collect the
       remaining numbers.
    2. Given a list of strings sorted alphabetically, use dropWhile to skip all strings that start with a letter before
       'M' and collect the rest.
    ### ofNullable()
    1. Given a nullable string, use Stream.ofNullable to create a stream and process it (e.g., convert to uppercase).
    2. Given a nullable integer, use Stream.ofNullable to create a stream and process it (e.g., add 10).
    ### iterate()
    1. Use Stream.iterate to generate a stream of even numbers, starting from 0, and collect the first 10 numbers.
    2. Use Stream.iterate to create a stream of powers of 2 (e.g., 1, 2, 4, 8, ...) and collect the first 8 values.
    */
    public static List<Integer> takeWhileExample1(List<Integer> input, Integer in) {
        return input.stream().sorted().takeWhile(n -> n < in).collect(Collectors.toList());
    }
    public static List<String> takeWhileExample2(List<String> input, Character c) {
        char ch = Character.toLowerCase(c);
        return input.stream().map(String::toLowerCase).sorted().takeWhile(str -> str.charAt(0) < ch).collect(Collectors.toList());
    }
    public static List<Integer> dropWhileExample1(List<Integer> input, Integer in) {
        return input.stream().sorted().dropWhile(n -> n < in).collect(Collectors.toList());
    }
    public static List<String> dropWhileExample2(List<String> input, Character c) {
        char ch = Character.toLowerCase(c);
        return input.stream().map(String::toLowerCase).sorted().dropWhile(str -> str.charAt(0) < ch).collect(Collectors.toList());
    }
    public static String nullableExample1(String input) {
        Stream<String> str = Stream.ofNullable(input);
        return str.collect(Collectors.joining(" ")).toUpperCase();
    }
    public static Integer nullableExample2(Integer input) {
        Stream<Integer> num = Stream.ofNullable(input);
        Integer res = num.reduce(0, Integer::sum);
        return res == 0 ? res : res + 10;
    }
    public static List<Integer> first10EvenNumbers() {
        return Stream.iterate(2, n -> 2*n).limit(10).collect(Collectors.toList());
    }
    public static List<Integer> first8PowersOf2() {
        return Stream.iterate(1, n -> (int) Math.pow(2,n)).limit(8).collect(Collectors.toList());
    }

    // JAVA 11
    /*
    ### toArray(IntFunction)
    1. Given a stream of integers, use the toArray method with a provided generator to convert the stream to an array
       of integers.
    2. Given a stream of strings, use the toArray method with a provided generator to convert the stream to an array
       of strings.
    */
    public static Integer[] toArrayExample1(Stream<Integer> input) {
        return input.toArray(Integer[]::new);
    }
    public static String[] toArrayExample2(Stream<String> input) {
        return input.toArray(String[]::new);
    }

    // JAVA 12
    /*
    ### teeing()
    1. Given a list of integers, use Collectors.teeing to calculate the sum and the count of the integers in a
       single pass.
    2. Given a list of strings, use Collectors.teeing to find the shortest and longest strings in the list.
    */
    public static Map<String,Integer> teeingExample1(List<Integer> input) {
        Map<String,Integer> result = input.stream().collect(Collectors.teeing(
                Collectors.counting(),
                Collectors.summingInt(el -> el),
                (e1,e2) -> {
                    Map<String,Integer> map = new HashMap<>();
                    map.put("count",Math.toIntExact(e1));
                    map.put("sum",e2);
                    return map;
                }
        ));
        return result;
    }
    public static Map<String,String> teeingExample2(List<String> input) {
        Map<String,String> result = input.stream().collect(Collectors.teeing(
                Collectors.maxBy(Comparator.comparing(String::length)),
                Collectors.minBy(Comparator.comparing(String::length)),
                (e1,e2) -> {
                    Map<String,String> map = new HashMap<>();
                    e1.ifPresent(s -> map.put("Longest", s));
                    e2.ifPresent(s -> map.put("Shortest", s));
                    return map;
                }
        ));
        return result;
    }

    // JAVA 16
    /*
    ### toList()
    1. Given a stream of integers, use toList() to collect the integers into an immutable list.
    2. Given a stream of strings, use toList() to collect the strings into an immutable list.
    */
    // in the main class

    // JAVA 21
    /*
    ### mapMulti()
    1. Given a list of integers, use mapMulti to produce a stream where each integer is duplicated
       (e.g., [1, 2, 3] -> [1, 1, 2, 2, 3, 3]).
    2. Given a list of lists of integers, use mapMulti to flatten the list of lists into a single list
       (similar to flatMap).
    ### toListCollector()
    1. Given a stream of integers, use the new toListCollector() to collect the integers into a list.
    2. Given a stream of strings, use the new toListCollector() to collect the strings into a list.
    */
    public static List<Integer> mapMultiExample1(List<Integer> input) {
         return input.stream().mapMultiToInt((number, downstream) -> {
                        downstream.accept(number);
                        downstream.accept(number);
                    }).boxed().toList();
    }
    public static List<Object> mapMultiExample2(List<List<Integer>> input) {
        return input.stream().mapMulti(Iterable::forEach).collect(Collectors.toList());
    }
    // the last two are in the main class

    public static void main(String[] args) {
        List<List<Integer>> list1 = new ArrayList<>(Arrays.asList(Arrays.asList(1, 2, 3),Arrays.asList(4,5,6),Arrays.asList(2,4,6)));
        List<String> list2 = new ArrayList<>(Arrays.asList("Hello World","This is an example","New array List"));
        List<String> list3 = new ArrayList<>(Arrays.asList("Hello", "World","This", "is", "an", "example"));
        List<Integer> list4 = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8));
        List<String> list5 = new ArrayList<>(Arrays.asList("Hello","World","Here","we","can","Think"));
        List<Integer> list6 = new ArrayList<>(Arrays.asList(1,3,5,7,9,2,4,6,8,0));
        Stream<Integer> stream1 = Stream.iterate(1, n -> n+1).limit(10);
        Stream<String> stream2 = list3.stream();
        // JAVA 8
        /*
        System.out.println(flatMapExample1(list1));
        System.out.println(flatMapExample2(list2));
        System.out.println(groupByExample1(list3));
        System.out.println(groupByExample2(list4));
        System.out.println(STR."Number of strings that start with H = \{countingExample1(list5,'H')}");
        System.out.println(STR."Number of strings that start with h = \{countingExample1(list5,'h')}");
        System.out.println(STR."Number of integers larger than 6 = \{countingExample2(list4,6)}");
        System.out.println(STR."Number of integers larger than 12 = \{countingExample2(list4,12)}");
        */
        // JAVA 9
        /*
        System.out.println(takeWhileExample1(list6,5));
        System.out.println(takeWhileExample2(list5,'m'));
        System.out.println(takeWhileExample2(list5,'M'));
        System.out.println(dropWhileExample1(list6,5));
        System.out.println(dropWhileExample2(list5,'m'));
        System.out.println(dropWhileExample2(list5,'M'));
        System.out.println(STR."Result: \{nullableExample1("Hello World")}");
        System.out.println(STR."Result: \{nullableExample1(null)}");
        System.out.println(STR."Result: \{nullableExample1("")}");
        System.out.println(STR."Result: \{nullableExample2(6)}");
        System.out.println(STR."Result: \{nullableExample2(null)}");
        System.out.println(STR."Result: \{first10EvenNumbers()}");
        System.out.println(STR."Result: \{first8PowersOf2()}");
        */
        // JAVA 11
        /*
        for(Integer i : toArrayExample1(stream1)){
            System.out.print(STR."\{i} ");
        }
        System.out.println(" ");
        for(String str : toArrayExample2(stream2)){
            System.out.print(STR."\{str} ");
        }
        */
        // JAVA 12
        /*
        System.out.println(STR."Result: \{teeingExample1(list6)}");
        System.out.println(STR."Result: \{teeingExample2(list3)}");
        */
        // JAVA 16
        /*
        List<Integer> unmodList1 = stream1.toList();
        System.out.println(STR."stream1.toList() belongs to \{unmodList1.getClass().getName()}");
        List<String> unmodList2 = stream2.toList();
        System.out.println(STR."stream2.toList() belongs to \{unmodList2.getClass().getName()}");
        */
        // JAVA 21
        /*
        System.out.println(STR."Result: \{mapMultiExample1(list4).toString()}");
        System.out.println(STR."Class of each element: \{mapMultiExample2(list1).getFirst().getClass().getName()}");
        System.out.println(STR."Result: \{mapMultiExample2(list1).toString()}");
        List<Integer> modList1 = stream1.collect(Collectors.toList());
        System.out.println(STR."stream1.toList() belongs to \{modList1.getClass().getName()}");
        List<String> modList2 = stream2.collect(Collectors.toList());
        System.out.println(STR."stream2.toList() belongs to \{modList2.getClass().getName()}");
        */
    }
}
