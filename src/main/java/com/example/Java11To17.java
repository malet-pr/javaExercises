package com.example;

import lombok.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Java11To17 {

    /*
    Exercise 1: Pattern Matching for instanceof
    Refactor the following code to use pattern matching for instanceof:
    Object obj = "Hello, World!";
    if (obj instanceof String) {
        String str = (String) obj;
        System.out.println(str.toLowerCase());
    }
    */
    public static void patternMatchingInstance(Object obj){
        if(obj instanceof String str){
            System.out.println(str.toLowerCase());
        }
    }
    /*
    Exercise 2: Text Blocks
    Use text blocks to refactor the following multi-line string:
    String html = "<html>\n" +
                  "    <body>\n" +
                  "        <p>Hello, World!</p>\n" +
                  "    </body>\n" +
                  "</html>";
    */
    public static final String html = """
                <html>
                    <body>
                        <p>Hello World!</p>
                    </body>
                </html>
            """;
    /*
    Exercise 3: Switch Expressions
    Rewrite the following switch statement using the new switch expression syntax:
    int dayOfWeek = 5;
    String dayName;
    switch (dayOfWeek) {
        case 1:
            dayName = "Monday";
            break;
        case 2:
            dayName = "Tuesday";
            break;
        case 3:
            dayName = "Wednesday";
            break;
        case 4:
            dayName = "Thursday";
            break;
        case 5:
            dayName = "Friday";
            break;
        case 6:
            dayName = "Saturday";
            break;
        case 7:
            dayName = "Sunday";
            break;
        default:
            dayName = "Invalid day";
    }
    */
    public static String dayOfWeek(int day){
        String dayOfWeek = "";
        switch(day){
            case 1 -> dayOfWeek = "Monday";
            case 2 -> dayOfWeek = "Tuesday";
            case 3 -> dayOfWeek = "Wednesday";
            case 4 -> dayOfWeek = "Thursday";
            case 5 -> dayOfWeek = "Friday";
            case 6 -> dayOfWeek = "Saturday";
            case 7 -> dayOfWeek = "Sunday";
            default -> dayOfWeek = "wrong number";
        }
        return dayOfWeek;
    }
    /*
    Exercise 4: Records
    Define a record Person with fields name (String) and age (int). Create a list of Person records and
    print out each person's details.
    */
    public record Person(String name, int age){}
    /*
    Exercise 5: Stream API Enhancements
    Use the new toList() collector to convert a stream of integers to a list
    */
    // in the main class
    /*
    Exercise 6: Files API Enhancements
    Write a program to read all lines from a file using the Files.readString(Path) method and print them to the console.
    */
    // Solved in another set of exercises
    /*
    Exercise 7: Helpful NullPointerExceptions
    Create a class with a few fields. Write a method that accesses these fields, but deliberately includes a null
    reference. Run the program and observe the detailed null pointer exception message introduced in Java 14.
    */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class NullPointerClass {
        Long id;
        String name;
        String description;
        @Override
        public String toString() {
            if(description.length()>10){
                return (STR."The object with id: \{this.id} and name: \{this.name} has a long description");
            } else {
                return (STR."The object with id: \{this.id} and name: \{this.name} can be described as \{this.description}");
            }
        }
    }
    /*
    Exercise 8: Sealed Classes
    Define a sealed class Shape with permitted subclasses Circle and Rectangle. Implement these subclasses and create
    instances of them.
    */
    public sealed interface Area permits Circle, Rectangle, Square {
        double getArea();
    }
    @Data
    public static abstract sealed class Shape permits Circle, Rectangle {
        private String color;
    }
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static final class Circle extends Shape implements Area {
        private int radius;

        @Override
        public double getArea() {
            return Math.PI * radius * radius;
        }
    }
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static sealed class Rectangle extends Shape implements Area permits Square {
        private int side1;
        private int side2;

        @Override
        public double getArea() {
            return side1 * side2;
        }
    }
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static non-sealed class Square extends Rectangle implements Area {
        private int side;
        @Override
        public double getArea() {
            return side * side;
        }
    }
    /*
    Exercise 9: LocalDate and LocalDateTime
    Use the LocalDate and LocalDateTime classes to parse, format, and manipulate dates and times.
    Ensure you use the new methods introduced up to Java 17 if applicable.
    */


    /*
    Exercise 10: Instant and OffsetDateTime
    Convert an Instant to an OffsetDateTime using the atOffset method and print the result.
    */


    /*
    Exercise 11: Enhanced Random Methods
    Use the new methods in the Random class, such as doubles(), ints(), and longs(), to generate streams of
    random numbers.
    */
    // In the main class
    /*
    Exercise 12: Enhanced Optional Methods
    Utilize the new ifPresentOrElse and or methods in the Optional class to handle optional values.
    */
    // In another exercise
    /*
    Exercise 13: Foreign Function & Memory API (Preview)
    Experiment with the Foreign Function & Memory API introduced as a preview in Java 17. Write a simple program
    that uses this API to interact with native code.
    */
    // Not now (it's a preview of java 22)

    public static void main(String[] args) {
        /*
        //patternMatchingInstance("Hello World");
        //System.out.println(html);
        //System.out.println(STR."The day of the week is \{dayOfWeek(2)}");
        //List<Person> persons = Arrays.asList(new Person("juan",23), new Person("maria",22), new Person("gabriel",25));
        //persons.forEach(System.out::println);
        //Person p1 = persons.stream().filter(p -> p.name().equalsIgnoreCase("maria")).findFirst().get();
        //Person p2 = new Person("juan",23);
        //System.out.println(STR."p1 is \{p1.toString()}");
        //System.out.println(STR."p1 and p2 are the same person? \{p1.equals(p2)}");
        //Stream<Integer> numbers = Stream.of(1, 2, 3, 4, 5);
        //List<Integer> numberList = numbers.toList();
        //NullPointerClass npc = NullPointerClass.builder().id(1L).name("example").build();
        //System.out.println(npc.toString());
        */
        /*
        Circle circle = new Circle();
        circle.setRadius(10);
        circle.setColor("red");
        System.out.println(circle);
        System.out.println(STR."Circle area = \{circle.getArea()}");
        Rectangle rectangle = new Rectangle();
        rectangle.setSide1(25);
        rectangle.setSide2(5);
        rectangle.setColor("blue");
        System.out.println(rectangle);
        System.out.println(STR."Rectangle area = \{rectangle.getArea()}");
        Square square = new Square();
        square.setSide(5);
        square.setColor("green");
        System.out.println(square);
        System.out.println(STR."Square area = \{square.getArea()}");
        */
        /*
        Random random = new Random();
        Stream<Integer> ints = random.ints(1, 10).boxed();
        Stream<Long> longs = random.longs(100, 10000).boxed();
        Stream<Double> doubles = random.doubles(1, 10).boxed();
        System.out.println(ints.limit(20).map(Object::toString).collect(Collectors.joining("-", " ", " ")));
        System.out.println(longs.limit(20).map(Object::toString).collect(Collectors.joining("-", " ", " ")));
        System.out.println(doubles.limit(20).map(Object::toString).collect(Collectors.joining("-", " ", " ")));
        */


    }
}

