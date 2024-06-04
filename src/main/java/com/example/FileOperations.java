package com.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileOperations {
    static Logger log = Logger.getLogger(FileOperations.class.getName());
    // READING FILES

    // Files.readString(path)
    /*
    - Write a method to read the entire content of a file into a string using Files.readString. Print the content to
      the console.
    - Given a file path, use Files.readString to read and return the content of the file as a string. Then, count and
      print the number of words in the file.
    */
    public static String readFile(String fileWithPath) throws IOException {
        Path filePath = Path.of(fileWithPath);
        return Files.readString(filePath);
    }
    public static int countWords(String fileWithPath) throws IOException {
        Path filePath = Path.of(fileWithPath);
        String[] arr = Files.readString(filePath).split(" ");
        return arr.length;
    }

    // Files.readAllLines(Path)
    /*
    - Write a method to read all lines from a file into a list of strings using Files.readAllLines. Print each line to
      the console.
    - Given a file path, use Files.readAllLines to read the file and print the total number of lines. (used Files.lines
      instead)
    */
    public static List<String> readLinesFile(String fileWithPath) throws IOException {
        Path filePath = Path.of(fileWithPath);
        return Files.readAllLines(filePath);
    }
    public static int countLinesInFile(String filePath, String fileName) {
        ArrayList<String> list = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(filePath,fileName))) {
            list = stream.collect(Collectors.toCollection(ArrayList::new));
        }
        catch (IOException e) {
            log.severe(e.getMessage());
        }
        return list.size();
    }

    // WRITING FILES

    //  Files.writeString(Path, CharSequence)
    /*
    - Write a method to write a given string to a file using Files.writeString. Ensure that the file is created if it
      does not exist.
    - Given a string and a file path, use Files.writeString to write the string to the file, appending the content if
      the file already exists.
    */
    public static void writeFile(String filePath, String fileName, String content) throws IOException {
        Files.write(Paths.get(filePath,fileName), content.getBytes(), StandardOpenOption.CREATE);
    }
    public static void appendFile(String filePath, String fileName, String content) throws IOException {
        Files.write(Paths.get(filePath,fileName), content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    // Files.write(Path, Iterable<? extends CharSequence>)
    /*
    - Write a method to write a list of strings to a file, each string on a new line, using Files.write.
    - Given a list of sentences, use Files.write to write each sentence to a new line in a file.
    */
    public static void writeMultiple(Path path, List<String> content) throws IOException {
        Files.write(path, content, StandardOpenOption.CREATE);
    }
    public static void appendMultiple(Path path, List<String> content) throws IOException {
        Files.write(path, content, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    // FILE OPERATIONS

    // Files.copy(Path, Path, CopyOption...)
    /*
    - Write a method to copy a file from one location to another using Files.copy.
      Ensure the target file is overwritten if it exists.
    */
    public static void copyFile(String srcPath, String destPath) throws IOException {
        Files.copy(Paths.get(srcPath), Paths.get(destPath), StandardCopyOption.REPLACE_EXISTING);
    }

    // Files.move(Path, Path, CopyOption...)
    /*
    - Write a method to move a file from one directory to another using Files.move.
      Ensure the target file is overwritten if it exists.
    */
    public static void moveFile(String srcPath, String destPath) throws IOException {
        Files.move(Paths.get(srcPath), Paths.get(destPath), StandardCopyOption.REPLACE_EXISTING);
    }

    // Files.delete(Path)
    /*
    - Write a method to delete a file at a given path using Files.delete.(deleteIfExists(Path path))
    - Given a directory path, use Files.walk to list all files and delete them one by one.
    */
    public static void deleteFile(String path, String name) throws IOException {
        Files.deleteIfExists(Paths.get(path, name));
    }
    public static void deleteFilesInDirectory(String path) throws IOException {
        Path pathDir = Path.of(path);
        List<Path> files = Files.walk(pathDir).toList();
        for (Path file : files) {
            if(Files.isRegularFile(file)) {
                Files.delete(file);
            }
        }
    }

    // FILE ATTRIBUTES

    // Files.isRegularFile(Path) and Files.isDirectory(Path)
    /*
    - Write a method to check if a given path is a regular file or a directory using Files.isRegularFile and
      Files.isDirectory. Print the result.
    */
    public static String typeOfFile(String fileWithPath) throws IOException {
        Path pathDir = Path.of(fileWithPath);
        String result = "";
        if(Files.exists(pathDir) && Files.isRegularFile(pathDir)) result = "regular file";
        else if(Files.exists(pathDir) && Files.isDirectory(pathDir)) result = "directory";
        return result;
    }

    // Files.getLastModifiedTime(Path)
    /*
    - Change the lastModifiedTime of a file to the current time using Files.getLastModifiedTime(Path)
    - Write a method to delete a file if it has been last modified before a given number of days
    */
    public static void changeFileModificationTime(String fileWithPath, LocalDateTime date) throws IOException {
        Path pathDir = Path.of(fileWithPath);
        Instant instant = date.atZone(ZoneId.of("America/Buenos_Aires")).toInstant();
        FileTime fileTime = FileTime.from(instant);
        Files.setLastModifiedTime(pathDir, fileTime);
    }
    public static void deleteOldFile(String path, Long days) throws IOException {
        Path filePath = Paths.get(path);
        FileTime lastModifiedTime = Files.getLastModifiedTime(filePath);
        Instant fileTime = lastModifiedTime.toInstant();
        Instant currentTime = Instant.now();
        Duration fileAge = Duration.between(fileTime, currentTime);
        long fileAgeDays = fileAge.toDays();
        if (fileAgeDays > days) {
            Files.delete(filePath);
        }
    }

    // Files.size(Path)
    /*
    - Write a method to get and print the size of a file in bytes using Files.size.
    - Given a directory path, use Files.walk to list all files and print their sizes.
    */
    public static Long getSize(String path) throws IOException {
        Path filePath = Path.of(path);
        return (Long) Files.getAttribute(filePath, "size");
    }
    public static Map<String,Long> getAllSizes(String path) throws IOException {
        Path pathDir = Path.of(path);
        if (!Files.exists(pathDir) || !Files.isDirectory(pathDir)) return null;
        Map<String, Long> map = new HashMap<>();
        List<Path> files = Files.walk(pathDir).toList();
        if (!files.isEmpty()){
            for (Path file : files) {
                if(Files.isRegularFile(file)) {
                    map.put(file.getFileName().toString(), (Long) Files.getAttribute(file, "size"));
                }
            }
        }
        return map;
    }

    // Files.getOwner and Files.setOwner
    /*
    - Get and print the owner of a specified directory.
    - Change the owner of a specified directory.
    */
    // in the main class

    // file permissions
    /*
    - Write a method to print if a file is readably, writable, executable or hidden using Files.isReadable(path),
      Files.isWritable(path), Files.isExecutable(path)
    - Write a method to set permissions to a file using Files.setAttribute
    - Set POSIX file permissions for a specified file using Files.setPosixFilePermissions
    */
    public static Map<String,Boolean> getPermissions(String path) throws IOException {
        Path pathFile = Path.of(path);
        Map<String, Boolean> map = new HashMap<>();
        if (Files.exists(pathFile)) {
            map.put("readable", Files.isReadable(pathFile));
            map.put("writable", Files.isWritable(pathFile));
            map.put("executable", Files.isExecutable(pathFile));
        }
        return map;
    }
    public static void setPermissions(String path, Map<String,Boolean> permissions) throws IOException {
        Path pathFile = Path.of(path);
        if (Files.exists(pathFile) && permissions != null && !permissions.isEmpty()) {
            File file = new File(path);
            for (String key : permissions.keySet()) {
                switch (key){
                    case "read":
                        file.setReadable(permissions.get(key));
                        break;
                    case "write":
                        file.setWritable(permissions.get(key));
                        break;
                    case "executable":
                        file.setExecutable(permissions.get(key));
                        break;
                    default:
                        break;
                }
            }
        } else{
            log.info(STR."The file \{path} does not exists or the list of permisions is empty");
        }
    }
    public static void setPosixPermissions(String path, List<String> permissions) throws IOException {
        Path pathFile = Path.of(path);
        if (Files.exists(pathFile) && permissions != null && !permissions.isEmpty()) {
            Set<PosixFilePermission> permSet = new HashSet<>();
            permissions.forEach(perm -> permSet.add(PosixFilePermission.valueOf(perm)));
            Files.setPosixFilePermissions(pathFile,permSet);
        }
    }

    // read file attributes
    /*
    - Write a method to read and print all attributes of a file in one go using Files.readAttributes.
    - Using Files.getFileAttributeView, retrieve a specific file attribute view and print details for a specified
      directory.(https://docs.oracle.com/javase%2F7%2Fdocs%2Fapi%2F%2F/java/nio/file/attribute/package-summary.html)
    */
    public static void printAllAttributes(String path) throws IOException {
        Path pathFile = Path.of(path);
        Map<String, Object> map  = Files.readAttributes(pathFile, "*");
        System.out.println(STR."File Attributes: \n\{map}");
    }
    public static Map<String,Object> getPosixAttributes(String path) throws IOException {
        Path pathFile = Path.of(path);
        PosixFileAttributes attrs = Files.getFileAttributeView(pathFile, PosixFileAttributeView.class).readAttributes();
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("owner",attrs.owner());
        map.put("group",attrs.group());
        map.put("permissions",attrs.permissions());
        return map;
    }

    // FILE EXISTENCE, NEW FILE AND TEMPORARY FILES

    // Files.exists(), Files.notExists(), Files.createFile(), Files.createTempFile(), Files.isSameFile
    /*
    - Write a method using Files.exists() to check if a file exists at a given path and return a boolean.
    - Write a method to create a file if it doesn't exist using Files.notExists().
    - Write a method to create a temporary file using Files.createTempFile and print the path of the created file.
    - Check if two paths refer to the same file and return a boolean.
    */
    public static boolean doesFileExist(String path) throws IOException {
        return Files.exists(Path.of(path));
    }
    public static void createFileIfNotExist(String path) throws IOException {
        Path filePath = Path.of(path);
        if(Files.notExists(filePath)) {
            Files.createFile(filePath);
            System.out.println("File created");
        } else {
            System.out.println("File already exists");
        }
    }
    public static void createTemporaryFile(String path) throws IOException {
        Path filePath = null;
        if(path != null && !path.isBlank()){
           filePath = Paths.get(path);
           if(Files.exists(filePath) && !Files.isDirectory(filePath)) {
               System.out.println("Temporary file creation failed. The path provided is not a directory.");
               return;
           }
        } else {
            filePath = Path.of(System.getProperty("user.dir") + "/tmp/");
            if(Files.notExists(filePath)) {
                Files.createDirectories(filePath);
            }
            try{
                File tempFile = Files.createTempFile(filePath, "tmp_", ".log").toFile();
                System.out.println(STR."Temporary file created: \{tempFile}");
                tempFile.deleteOnExit();
            } catch (Exception e) {
                System.out.println(STR."Temporary file creation failed");
                log.severe(e.getMessage());
            }

        }
    }
    public static Boolean isSameFile(String path1, String path2) throws IOException {
        boolean result = false;
        try{
            result = Files.isSameFile(Paths.get(path1), Paths.get(path2));
        } catch (Exception e) {
            log.severe(e.getMessage());
            return null;
        }
        return result;
    }

    // READING AND WRITING BINARY DATA

    // Files.readAllBytes(Path) and Files.write(Path, byte[])
    /*
    - Write a method to read all bytes from a binary file using Files.readAllBytes. Print the byte array.
    - Given a byte array and a file path, use Files.write to write the byte array to the file.
    */
    public static byte[] readBinaryFile(String path) throws IOException {
        Path pathFile = Path.of(path);
        if(!Files.exists(pathFile) || Files.probeContentType(pathFile).startsWith("text")) return null;
        return Files.readAllBytes(pathFile);
    }
    public static void writeBinaryFile(String path, String fileName, byte[] content) throws IOException {
        if(path.isBlank() || fileName.isBlank() || content.length < 1) return;
        try{
            Path pathFile = Path.of(path,fileName);
            Files.write(pathFile, content);
        } catch (Exception e) {
            log.severe(e.getMessage());
        }
    }


    public static void main(String[] args) throws IOException {
        String fileWithPath = "/home/nuria/Cursos/ejerciciosJava/javaExercises/src/main/java/com/example/ex.txt";
        String oldFile = "/home/nuria/Cursos/ejerciciosJava/javaExercises/src/main/java/com/example/files/oldFile.txt";
        String temFile = "/home/nuria/Cursos/ejerciciosJava/javaExercises/src/main/java/com/example/files/tempFile.txt";
        String filePath = "/home/nuria/Cursos/ejerciciosJava/javaExercises/src/main/java/com/example/files";
        String copyPath = "/home/nuria/Cursos/ejerciciosJava/javaExercises/src/main/java/com/example/filesCopy";
        String binaryFile = "/home/nuria/Cursos/ejerciciosJava/javaExercises/src/main/java/com/example/files/previous-compilation-data.bin";
        Path path = Paths.get(filePath,"test2.txt");
        List<String> content1 = Arrays.asList("line1","line2","line3");
        List<String> content2 = Arrays.asList("line4","line5","line6");
        LocalDateTime dateTime = LocalDateTime.parse("2024-05-01T10:15:30");
        List<String> perms1 = Arrays.asList("GROUP_EXECUTE","GROUP_READ","GROUP_WRITE","OTHERS_EXECUTE", "OTHERS_READ","OTHERS_WRITE","OWNER_EXECUTE","OWNER_READ","OWNER_WRITE");
        List<String> perms2 = Arrays.asList("GROUP_READ","OTHERS_READ","OWNER_READ","OWNER_WRITE");
        //System.out.println(readFile(fileWithPath));
        //System.out.println(STR."Word Count: \{countWords(fileWithPath)}");
        /*
        List<String> lines = readLinesFile(fileWithPath);
        System.out.println(STR."Number of lines in file: \{lines.size()}");
        System.out.println("First 3 lines:");
        List<String> firstLines = lines.stream().limit(3).toList();
        firstLines.forEach(System.out::println);
        */
        //System.out.println(STR."Number of lines in file: \{countLinesInFile(filePath,"ex.txt")}");
        //writeFile(filePath,"test1.txt","this is a test.\n");
        //appendFile(filePath,"test1.txt","this is the second line in the file.\n");
        //appendFile(filePath,"test.txt","Hello World!.\n");
        //writeMultiple(path, content1);
        //appendMultiple(path,content2);
        //copyFile(String.valueOf(Paths.get(filePath,"test1.txt")),String.valueOf(Paths.get(copyPath,"test1.txt")));
        //copyFile(String.valueOf(Paths.get(filePath,"test1.txt")),String.valueOf(Paths.get(filePath,"testCopy.txt")));
        //moveFile(String.valueOf(Paths.get(filePath,"test.txt")),String.valueOf(Paths.get(copyPath,"test.txt")));
        //moveFile(String.valueOf(Paths.get(filePath,"testCopy.txt")),String.valueOf(Paths.get(filePath,"testCopy2.txt")));
        //deleteFile(copyPath,"andAnotherFile.txt");
        //deleteFilesInDirectory(copyPath);
        //System.out.println(STR." The provided path is a \{typeOfFile(fileWithPath)}");
        //System.out.println(STR." The provided path is a \{typeOfFile(copyPath)}");
        //changeFileModificationTime(oldFile,dateTime);
        //deleteOldFile(oldFile,30L);
        //deleteOldFile(fileWithPath,10L);
        //System.out.println(STR."The file requested has \{getSize(fileWithPath)} bytes.");
        //System.out.println(getAllSizes(filePath));
        //System.out.println(Files.getOwner(path));
        /* Need to start IntelliJ as root to run this code
        //Files.setOwner(path, FileSystems.getDefault().getUserPrincipalLookupService().lookupPrincipalByName("root"));
        */
        /*
        System.out.println(getPermissions(fileWithPath));
        Map<String,Boolean> map = new HashMap<>();
        map.put("read",true);
        map.put("write",true);
        map.put("execute",false);
        setPermissions(fileWithPath, map);
        System.out.println(getPermissions(fileWithPath));
        */
        //setPosixPermissions(filePath+"/test1.txt",perms1);
        //setPosixPermissions(filePath+"/test2.txt",perms2);
        //printAllAttributes(fileWithPath);
        //System.out.println(getPosixAttributes(fileWithPath));
        //System.out.println(STR."Does the file \{fileWithPath} exists? \{doesFileExist(fileWithPath)}");
        //createFileIfNotExist(oldFile);
        //createTemporaryFile(copyPath);
        //createTemporaryFile(fileWithPath);
        //createTemporaryFile("");
        //System.out.println(STR."Do the two paths point at the same file? \{isSameFile(filePath+"/test1.txt",copyPath+"/test1.txt")}");
        //System.out.println(STR."Do the two paths point at the same file? \{isSameFile(filePath+"/test1.txt",filePath+"/test1.txt")}");
        //System.out.println(STR."Do the two paths point at the same file? \{isSameFile(filePath+"test1.txt",filePath+"/test1.txt")}");
        //System.out.println(readBinaryFile(binaryFile));
        /*
        byte[] byteArray = new byte[8];
        for (int i = 0; i < 8; i++) {byteArray[i] = (byte) i;}
        System.out.println(byteArray.length);
        writeBinaryFile(copyPath,"test.txt",byteArray);
        */

    }
}
