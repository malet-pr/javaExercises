package com.example;

import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorSpecies;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import static java.util.FormatProcessor.FMT;

public class Java17To21 {

    String newFeatures = "https://spring.io/blog/2023/09/20/hello-java-21";

    /*
    Exercise 1: Simple Web Server
    Use the simple web server introduced in Java 18 to create a basic HTTP server that serves static files from a
    specified directory.
    */
    // from a terminal, serve all files in a given directory, on a given url and port
    // jwebserver -b 0.0.0.0 -p 3003 -d "/home/nuria/Cursos/ejerciciosJava/webserver"
    // kill webserver
    // sudo fuser -k 3003/tcp
    /*
    Exercise 2: Code Snippets in Javadoc
    Add a code snippet to the Javadoc of a method in your project using the new @snippet tag introduced in Java 18.
    */
    // In exercise 4
    /*
    Exercise 3: UTF-8 by Default
    Verify that the default charset in your application is UTF-8 by reading and writing a text file containing
    special characters.
    */
    public static void createFile(String filePath, String name) throws IOException {
        String fileWithPath = filePath + File.separator + name;
        try {
            File fileName = new File(fileWithPath);
            if (fileName.createNewFile()) {
                System.out.println(STR."File created: \{fileName.getName()}");
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.getStackTrace();
        }
    }
    public static void writeFile(String filePath, String fileName, String content) throws IOException {
        Files.write(Paths.get(filePath,fileName), content.getBytes(), StandardOpenOption.CREATE);
    }
    public static void printFileContent(String fileName) throws IOException {
        Path filePath = Path.of(fileName);
        System.out.println(Files.readString(filePath));
    }
    /*
    Exercise 4: Pattern Matching Enhancements
    Utilize the pattern matching enhancements for switch statements introduced in Java 18 to refactor a switch
    statement that operates on an object of type Object.
    */
    public static double getDoubleUsingSwitch(Object o) {
        return switch (o) {
            case Integer i -> i.doubleValue();
            case Float f -> f.doubleValue();
            case String s -> Double.parseDouble(s);
            default -> 0d;
        };
    }
    /*
    Exercise 5: Vector API (Incubator)
    Experiment with the Vector API introduced in Java 18 (and incubated further in subsequent releases) to perform
    vector computations. Write a program that uses the Vector API to perform parallel array operations.
    */
    // old way
    public static int[] addTwoScalarArrays(int[] arr1, int[] arr2) {
        int[] result = new int[arr1.length];
        for(int i = 0; i< arr1.length; i++) {
            result[i] = arr1[i] + arr2[i];
        }
        return result;
    }
    // vector api way in main class
    /*
    Exercise 6: Foreign Function & Memory API (Second Incubator)
    Use the Foreign Function & Memory API to interact with native libraries. Create a program that calls a simple native
    function using this API.
    */
    // not here
    /*
    Exercise 7: Record Patterns
    Use record patterns (introduced in a preview in Java 19) in a pattern matching operation. Create a record and
    demonstrate how to destructure it using pattern matching.
    */
    public sealed interface Employee permits Salaried, Freelancer {
        String getName();
        String getDepartment();
    }
    public record Salaried(String name, String department, double salary) implements Employee {
        @Override
        public String getName() {
            return name;
        }
        @Override
        public String getDepartment() {
            return department;
        }
    }
    public record Freelancer(String name, String department, double hourlyRate) implements Employee {
        @Override
        public String getName() {
            return name;
        }
        @Override
        public String getDepartment() {
            return "";
        }
    }
    public static String getEmployeeDetails(Employee employee) {
        return switch (employee) {
            case Salaried(String name, _, double salary) ->
                    STR."\{name} is a salaried employee with a salary of \{salary}";
            case Freelancer(String name, _, double hourlyRate) ->
                    STR."\{name} is a freelancer with an hourly rate of \{hourlyRate}";
        };
    }
    /*
    Exercise 8: Virtual Threads (Project Loom)
    Use virtual threads introduced in Project Loom (preview in Java 19) to create a large number of lightweight threads.
    Write a program that performs concurrent tasks using virtual threads.
    */
    // NOT HERE, I CANNOT USE THE CORRESPONDING INCUBATOR MODULE
    /*
    Exercise 9: Structured Concurrency (Project Loom)
    Implement structured concurrency using the new APIs introduced in Project Loom (preview in Java 19).
    Create a program that manages multiple tasks with structured concurrency.
    */
    // NOT HERE, I CANNOT USE THE CORRESPONDING INCUBATOR MODULE
    /*
    Exercise 10: Pattern Matching for switch
    Use pattern matching for switch (introduced as a preview feature in Java 17 and enhanced in later versions) to
    refactor a complex switch statement.
    */
    // solved in exercise 4
    /*
    Exercise 11: Deprecate Finalization
    Identify and refactor code in your project that relies on object finalization, as the finalization mechanism has
    been deprecated and is planned for removal in a future release.
    */
    // not here
    /*
    Exercise 12: Enhanced String Methods
    Use the new methods in the String class, such as transform, to manipulate strings in more concise ways. Write a
    program that demonstrates these new methods.
    */
    // In main class
    /*
    Exercise 13: Enhanced Pattern and Matcher Methods
    Explore the new methods in the Pattern and Matcher classes introduced in recent releases. Write a program that
    utilizes these methods for regular expression operations.
    */
    String patterrn_matcher = "https://dzone.com/articles/how-to-get-started-with-new-pattern-matching-in-ja";
    String matcher = "https://docs.oracle.com/javase/tutorial/essential/regex/matcher.html";
    /*
    Exercise 14: Improved Random Generator
    Use the new RandomGenerator interface and its implementations to generate random values. Write a program that
    demonstrates the use of the new Random methods.
    */
    String random_generator = "https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/random/RandomGenerator.html";
    /*
    Exercise 15: Context-Specific Deserialization Filters
    Implement context-specific deserialization filters for ObjectInputStream to enhance security when deserializing
    objects.
    */
    String deserialization_filters = "https://www.baeldung.com/java-context-specific-deserialization-filters";
    /*
    Exercise 16: Sealed Interfaces
    Define a sealed interface with several permitted subclasses. Implement these subclasses and create instances of them.
    */
    // Solved in exercise 7
    /*
    Exercise 17: Deprecation of SecurityManager
    Identify and refactor code in your project that uses SecurityManager, as it has been deprecated for removal.
    Write a program that replaces SecurityManager with alternative security measures.
    */
    // not here
    /*
    Exercise 18: Enhanced Process API
    Use the enhanced Process API to start and manage operating system processes. Write a program that demonstrates the
    new methods and features of the Process API.
    */
    // not here
    /*
    Exercise 19: Foreign Memory API (Preview)
    Experiment with the Foreign Memory API introduced in Java 18 and further incubated in subsequent releases. Write a
    program that allocates and manipulates memory segments using this API.
    */
    // not here
    /*
    Exercise 20: HttpClient API Enhancements
    Use the enhanced HttpClient API to make asynchronous HTTP requests. Write a program that demonstrates the new
    features and improvements in the HttpClient API.
    */
    // In a separate project


    public static void main(String[] args) throws IOException {

        System.out.println("\nExercise 3: ");
        String filePath = "/home/nuria/Cursos/ejerciciosJava/textFiles";
        String fileName = "test1.txt";
        File f = new File(STR."\{filePath}/\{fileName}");
        String content = "Probando con la ñ-Ñ, los acentos á-Á y la diéresis ü-Ü del español";
        if (!f.exists()) {
            createFile(filePath, fileName);
        }
        writeFile(filePath, fileName,content);
        printFileContent(STR."\{filePath}/\{fileName}");

        System.out.println("\nExercise 4: ");
        System.out.println("from integer: " + getDoubleUsingSwitch(3));
        System.out.println("from float: " + getDoubleUsingSwitch(3.0f));
        System.out.println("from string: " + getDoubleUsingSwitch("3.0"));
        System.out.println("bad input: " + getDoubleUsingSwitch(3.0));

        System.out.println("\nExercise 5: ");
        int[] arr1 = new int[]{1,2,3,4};
        int[] arr2 = new int[]{5,6,7,8};
        System.out.println(STR."old way \{Arrays.toString(addTwoScalarArrays(arr1, arr2))}");
        VectorSpecies<Integer> SPECIES = IntVector.SPECIES_128;
        var v1 = IntVector.fromArray(SPECIES, arr1, 0);
        var v2 = IntVector.fromArray(SPECIES, arr2, 0);
        System.out.println(STR."vector way \{v1.add(v2)}");

        System.out.println("\nExercise 7: ");
        record Point(int x, int y) {}
        Object maybePoint = new Point(1, 2);
        if (maybePoint instanceof Point p) {
            System.out.println(STR."Point (v1) => x=\{p.x}, y=\{p.y}");
        }
        if (maybePoint instanceof Point(int x, int y)) {
            System.out.println(STR."Point (v2) => x=\{x}, y=\{y}");
        }
        ///////////////////
        Salaried sal = new Salaried("John","finance", 3300);
        Freelancer fre = new Freelancer("Paul","", 10);
        System.out.println(STR."Salaried: \{getEmployeeDetails(sal)}");
        System.out.println(STR."Freelancer: \{getEmployeeDetails(fre)}");

        System.out.println("\nExercise 12: ");
        // string template and string block are already covered
        String text = "Java\nEvolution";
        String indentedText = text.indent(4);
        String transformed = text.transform(s -> new StringBuilder(s).reverse().toString());
        System.out.println(indentedText);
        System.out.println(transformed);

        System.out.println("\nFMT Template Processor");
        record Rectangle(String name, double width, double height) {
            double area() {
                return width * height;
            }
        }
        Rectangle[] zone = new Rectangle[] {
                new Rectangle("Alfa", 17.8, 31.4),
                new Rectangle("Bravo", 9.6, 12.4),
                new Rectangle("Charlie", 7.1, 11.23),
        };
        String table = FMT."""
            Description     Width    Height     Area
            %-12s\{zone[0].name}  %7.2f\{zone[0].width}  %7.2f\{zone[0].height}     %7.2f\{zone[0].area()}
            %-12s\{zone[1].name}  %7.2f\{zone[1].width}  %7.2f\{zone[1].height}     %7.2f\{zone[1].area()}
            %-12s\{zone[2].name}  %7.2f\{zone[2].width}  %7.2f\{zone[2].height}     %7.2f\{zone[2].area()}
            \{" ".repeat(28)} Total %7.2f\{zone[0].area() + zone[1].area() + zone[2].area()}
            """;
        System.out.println(table);

        System.out.println("\nUser-Defined Template Processors");
        var INTER = StringTemplate.Processor.of((StringTemplate st) -> {
            StringBuilder sb = new StringBuilder();
            Iterator<String> fragIter = st.fragments().iterator();
            for (Object value : st.values()) {
                sb.append(fragIter.next());
                sb.append(value);
            }
            sb.append(fragIter.next());
            return sb.toString();
        });
        int x = 10, y = 20;
        System.out.println(INTER."\{x} plus \{y} equals \{x + y}");
        ///////////////////////////////////////////////////
        var CODE = StringTemplate.Processor.of((template) -> {
            List<Object> values = template.values();
            Iterator<String> fragIter = template.fragments().iterator();
            StringBuilder builder = new StringBuilder();
            for (Object value : values) {
                String next = fragIter.next();
                builder.append(next);
                builder.append(STR."`\{value}`");
            }
            builder.append(fragIter.next());
            return builder.toString();
        });
        String output = CODE."Use the \{String.class.getName()} class in Java for text manipulation.";
        System.out.println(output);
        ////////////////////////////////////////////////////////////////
        var JSON = StringTemplate.Processor.of((StringTemplate st) -> {
            JSONObject json = new JSONObject();
            Iterator<Object> valueIterator = st.values().iterator();
            for (String string : st.fragments()) {
                String key = string.trim();
                if (!key.isEmpty() && valueIterator.hasNext()) {
                    Object value = valueIterator.next();
                    json.put(key, value);
                }
            }
            return json;
        });
        String name = "Java";
        int version = 21;
        JSONObject jsonObject = JSON."name: \{name}, version: \{version}";
        System.out.println(jsonObject.toString());
        //////////////////////////////////////////////////
        System.out.println("\nPrint Emoji");
        var shockedFaceEmoji = "\uD83E\uDD2F";
        System.out.println(shockedFaceEmoji);

    }
}

