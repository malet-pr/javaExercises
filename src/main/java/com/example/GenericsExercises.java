package com.example;

import java.util.Arrays;
import java.util.Collections;
import  java.util.Random;

public class GenericsExercises {

    // Write a generic method printArray that takes an array of any type and prints each element.
    public static <T> void printArray(T[] array) {
        Arrays.stream(array).forEach(System.out::println);
    }

    // Implement a generic class Pair that stores two elements of potentially different types.
    // Include methods to get and set the elements.
    public static class Pair<T1, T2> {
        private T1 first;
        private T2 second;

        public Pair() {}
        public Pair(T1 first, T2 second) {
            this.first = first;
            this.second = second;
        }

        public T1 getFirst() {
            return first;
        }
        public void setFirst(T1 first) {
            this.first = first;
        }
        public T2 getSecond() {
            return second;
        }
        public void setSecond(T2 second) {
            this.second = second;
        }
    }

    // Write a generic method findMax that takes an array of Comparable
    // elements and returns the maximum element.
    public static <T extends Comparable<T>> T getMaximum(T[] array) {
        return Collections.max(Arrays.asList(array));
    }
    // compile-time error example
    public static class TestClass {
        private String name;
        private int age;

        public TestClass(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getAge() {
            return age;
        }
        public void setAge(int age) {
            this.age = age;
        }
    }
    // run-time error example
    public static <T> void printArrayWithRuntimeError(T[] array) {
        for (T element : array) {
            try {
                String str = (String) element;
                System.out.println(str);
            } catch (Exception e) {
                System.out.println(STR."\{String.valueOf(element)}: \{e.getMessage()}");
            }
        }
    }

    /*
    Write a generic method swapElements that takes an array and two indices, and swaps the elements at those indices.
     */
    public static <T> T[] swapElements(T[] array, int i1, int i2){
        int n = array.length;
        T[] t1 = Arrays.copyOfRange(array, 0, i1);
        T[] t2 = Arrays.copyOfRange(array, i1+1, i2);
        T[] t3 = Arrays.copyOfRange(array, i2+1, n);
        T[] result = Arrays.copyOf(t1,n);
        System.arraycopy(t2,0,result,t1.length+1,t2.length);
        System.arraycopy(t3,0,result,t1.length+t2.length+2,t3.length);
        result[i1] = array[i2];
        result[i2] = array[i1];
        return result;
    }

    /*
    Implement a generic class Triple that stores three elements of potentially different types. Include methods to
    get and set the elements.
     */
    public static class Triple<T1,T2,T3> {
        private T1 first;
        private T2 second;
        private T3 third;
        public T1 getFirst() {
            return first;
        }
        public T2 getSecond() {
            return second;
        }
        public T3 getThird() {
            return third;
        }
        public void setFirst(T1 first) {
            this.first = first;
        }
        public void setSecond(T2 second) {
            this.second = second;
        }
        public void setThird(T3 third) {
            this.third = third;
        }
    }

    /*
     Write a generic method countGreaterThan that takes an array of Comparable elements and a specified element,
     and returns the count of elements that are greater than the specified element.
     */
    public static <T extends Comparable<T>> int countGreaterThan(T[] array, T element){
        int result = 0;
        for(T el : array){
            if(el.compareTo(element) > 0) result++;
        }
        return result;
    }


    public static void main(String[] args) {
        System.out.println("================================================================");
        String[] arr1 = {"one","two","three","four"};
        printArray(arr1);
        System.out.println(" ");
        Integer[] arr2 = {1,2,3,4};
        printArray(arr2);
        System.out.println("================================================================");
        Pair<String,Integer> first = new Pair("one",1);
        Pair<Long,Boolean> second = new Pair(2L,false);
        System.out.println("Class of first object: " + first.getClass().getName());
        System.out.println("Class of each property: " + first.getFirst().getClass().getName() + " and " + first.getSecond().getClass().getName());
        System.out.println(" ");
        System.out.println("Class of second object: " + second.getClass().getName());
        System.out.println("Class of each property: " + second.getFirst().getClass().getName() + " and " + second.getSecond().getClass().getName());
        System.out.println("================================================================");
        Float arr3[] = new Float[10];
        Random rand = new Random();
        for(int i = 0; i<10; i++){
            arr3[i] = rand.nextFloat();
            System.out.print(arr3[i] + "   ");
        }
        System.out.println("\nMaximum number in the array: " + getMaximum(arr3));
        System.out.println("================================================================");
        /* This code doesn't compile
        TestClass[] arr4 = {new TestClass("ann",18),new TestClass("john",25)};
        try{
            System.out.println("Maximum array of TestClass: " + getMaximum(arr4));
        } catch (Exception e){
            System.out.println("Maximum array of TestClass: " + e.getMessage());
        }
        */
        // Testing with valid array
        printArray(arr1);
        // Testing with invalid array to demonstrate runtime error
        Object[] mixedArray = {1, "two", 3.0, false,String.valueOf(5),"3.1415"};
        printArrayWithRuntimeError(mixedArray);
        System.out.println("================================================================");
        Integer[] array1 = {0,1,2,3,4,5,6,7,8,9};
        Arrays.stream(swapElements(array1,3,5)).forEach(System.out::print);
        System.out.println("");
        Character[] array2 = {'a','b','c','d','e','f','g'};
        Arrays.stream(swapElements(array2,2,5)).forEach(System.out::print);
        System.out.println("\n================================================================");
        Triple<String,Integer,Boolean> third = new Triple();
        third.setFirst("the meaning of life the universe and everything");
        third.setSecond(42);
        third.setThird(true);
        System.out.println("Class of third: " + third.getClass().getName());
        System.out.println(STR."Is \{third.getSecond()} \{third.getFirst()}? \{third.getThird()}");
        System.out.println("================================================================");
        System.out.println(STR."The number of elements greater than 5 in the array is \{countGreaterThan(array1,5)}");
        System.out.println("================================================================");
    }

}
