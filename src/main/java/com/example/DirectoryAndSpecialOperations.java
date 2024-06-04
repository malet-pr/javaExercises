package com.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileStoreAttributeView;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirectoryAndSpecialOperations {
    static Logger log = Logger.getLogger(DirectoryAndSpecialOperations.class.getName());

    // CHECK DIRECTORY EXISTENCE AND CREATE A NEW DIRECTORY
    /*
    - Write a method to check if a directory exists at a given path and return a boolean.
    - Create a directory at a specified path.
    - Create a directory and its non-existent parent directories at a specified path.
    */
    public static boolean doesDirectoryExist(String directory) {
        return Files.exists(Paths.get(directory));
    }
    public static void createDirectory(String directory) throws IOException {
        if(directory.isBlank()) {
            System.out.println("Directory name cannot be blank");
        } else if (!Files.exists(Path.of(directory).getParent())){
            System.out.println("Parent directory does not exist");
        } else {
            try {
                Files.createDirectory(Path.of(directory));
                System.out.println(STR."Directory created: \{directory}");
            } catch (Exception e) {
                System.out.println("Failed to create directory");
                log.severe(e.getMessage());
            }
        }
    }
    public static void createDirectoryStructure(String directory) throws IOException {
        if(directory.isBlank()) System.out.println("Directory name cannot be blank");
        try {
            Files.createDirectories(Path.of(directory));
            System.out.println(STR."Directory created: \{directory}");
        } catch (Exception e) {
            System.out.println("Failed to create directory");
            log.severe(e.getMessage());
        }
    }

    // COPY, MOVE, DELETE DIRECTORIES
    /*
    - Write a method to delete a directory and all its contents.
    - Write a method to copy all contents of a directory to another directory (but not the directory itself).
    - Write a method to copy a directory to another directory with all its content.
    - Write a method to move a directory to a new location with all its content.
    */
    public static void deleteDirectoryAndContent(String directory) throws IOException {
        if(directory.isBlank()) System.out.println("Directory name cannot be blank.");
        if(Files.exists(Path.of(directory))) {
            Path dir = Path.of(directory);
            List<Path> filesToDelete = Files.list(dir).toList();
            int count = 0;
            for (Path file : filesToDelete) {
                try {
                    Files.deleteIfExists(file);
                    count++;
                } catch (IOException e) {
                    System.out.println(STR."Failed to delete file: \{file}");
                    log.severe(e.getMessage());
                }
            }
            System.out.println(STR."Deleted \{count} files.");
            if(Files.list(dir).toList().isEmpty()) {
                Files.delete(dir);
                System.out.println(STR."Directory deleted: \{dir}");
            }else{
                System.out.println("Couldn't delete directory because it's not empty");
            }
        } else {
            System.out.println("Directory does not exist.");
        }
    }
    public static int copyAllFiles(String source, String target) throws IOException {
        int count = 0;
        if(source.isBlank() || target.isBlank()) {
            System.out.println("Source and target names cannot be blank.");
        } else if(!Files.exists(Path.of(source))) {
            System.out.println("Cannot copy files because source does not exist.");
        } else if(!Files.exists(Path.of(target))) {
            System.out.println("Cannot copy files because target does not exist.");
        } else{
            Path sourcePath = Path.of(source);
            Path targetPath = Path.of(target);
            for(Path file : Files.list(sourcePath).toList()) {
                Path targetFile = new File(targetPath.toFile(), file.getFileName().toString()).toPath();
                try {
                    if(Files.isRegularFile(file) && Files.notExists(targetFile)) {
                        Files.copy(file,targetFile);
                        count++;
                    }
                } catch (IOException e) {
                    log.info(STR."Failed to copy file: \{file}");
                    e.printStackTrace();
                }
            }
            log.info(STR."Successfully copied \{count} files.");
        }
        return count;
    }
    public static int copyDirectoryAndContent(String source, String target) throws IOException {
        int count = 0;
        if(source.isBlank() || target.isBlank()) {
            System.out.println("Source and target names cannot be blank.");
        } else if(!Files.exists(Path.of(source))) {
            System.out.println("Cannot copy files because source does not exist.");
        } else if(!Files.exists(Path.of(target))) {
            try{
                Files.createDirectories(Path.of(target));
                System.out.println(STR."Created directory \{target}");
            } catch(IOException e){
                System.out.println("Cannot copy files because target does not exist and could not be created.");
                log.severe(e.getMessage());
            }
        }
        if(Files.exists(Path.of(target))) {
            Path sourcePath = Path.of(source);
            Path targetPath = Path.of(target);
            for(Path file : Files.list(sourcePath).toList()) {
                Path targetFile = new File(targetPath.toFile(), file.getFileName().toString()).toPath();
                try{
                    if(Files.isRegularFile(file) && Files.notExists(targetFile)) {
                        Files.copy(file,targetFile);
                        count++;
                    }
                } catch (IOException e) {
                    log.info(STR."Failed to copy file: \{file}");
                    e.printStackTrace();
                }
            }
        }
        return count;
    }
    public static int moveDirectoryAndContent(String source, String target) throws IOException {
        int count = 0;
        if(source.isBlank() || target.isBlank()) {
            System.out.println("Source and target names cannot be blank.");
        } else if(!Files.exists(Path.of(source))) {
            System.out.println("Cannot move files because source does not exist.");
        } else if(!Files.exists(Path.of(target))) {
            try{
                Files.createDirectories(Path.of(target));
                System.out.println(STR."Created directory \{target}");
            } catch(IOException e){
                System.out.println("Cannot move files because target does not exist and could not be created.");
                log.severe(e.getMessage());
            }
        }
        if(Files.exists(Path.of(target))) {
            Path sourcePath = Path.of(source);
            Path targetPath = Path.of(target);
            for(Path file : Files.list(sourcePath).toList()) {
                Path targetFile = new File(targetPath.toFile(), file.getFileName().toString()).toPath();
                try{
                    if(Files.isRegularFile(file)) {
                        Files.move(file,targetFile,StandardCopyOption.REPLACE_EXISTING);
                        count++;
                    }
                } catch (IOException e) {
                    log.info(STR."Failed to move file: \{file}");
                    e.printStackTrace();
                }
            }
        }
        if(Files.list(Path.of(source)).count() == 0) {
            Files.delete(Path.of(source));
        }
        return count;
    }

    // DIRECTORY STREAM

    //Files.newDirectoryStream
    /*
    - Create a new directory stream to iterate over all entries in a directory.
    */
    public static DirectoryStream<Path> createDirectoryStream(String dir) throws IOException {
        Path dirPath = Paths.get(dir);
        DirectoryStream<Path> stream = null;
        try {
            stream = Files.newDirectoryStream(dirPath);
        } catch (Exception e) {
            log.info(STR."Error: \{e.getMessage()}");
        }
        return stream;
    }

    // Files.list(Path)
    /*
    - Write a method to list all files in a directory using Files.list. Print the name of each file.
    - Given a directory path, use Files.list to find and print all files with a specific extension (e.g., ".txt").
    */
    public static List<String> getAllFiles(String dirName) throws IOException {
        Path dirPath = Path.of(dirName);
        List<String> allFiles = new ArrayList<>();
        Files.list(dirPath).forEach(p -> allFiles.add(p.getFileName().toString()));
        return allFiles;
    }
    public static List<String> getFilesWithExtension(String dirName, String ext) throws IOException {
        Path dirPath = Path.of(dirName);
        List<String> allFiles = new ArrayList<>();
        Files.list(dirPath).filter(p -> p.toString().endsWith("."+ext)).forEach(p -> allFiles.add(p.getFileName().toString()));
        return allFiles;
    }

    // Files.walk(Path)
    /*
    - Given a directory path, use Files.walk to find and print all files larger than a given size.
    */
    public static List<String> getFilesAndDirectory(String dirName) throws IOException {
        Path dirPath = Path.of(dirName);
        List<String> allFiles = new ArrayList<>();
        Files.walk(dirPath).forEach(path -> allFiles.add(path.toString()));
        return allFiles;
    }

    // TEMPORARY DIRECTORIES

    // Files.createTempDirectory()
    /*
    - Write a method to create a temporary directory using Files.createTempDirectory and print the path of the created
      directory.
    */
    public static void createTemporaryDirectory(String dirName) throws IOException {
        if(dirName.isBlank()) System.out.println("Directory name cannot be blank");
        if(!Files.exists(Path.of(dirName))) Files.createDirectories(Path.of(dirName));
        Path dirPath = Path.of(dirName);
        try {
            Files.createTempDirectory(dirPath, "tmp");
            System.out.println(STR."Temporary directory created: \{dirPath}");
            dirPath.toFile().deleteOnExit();
        } catch (Exception e) {
            System.out.println("Temporary directory creation failed.");
            log.info(e.getMessage());
        }
    }

    // SPECIFIC (LINUX) METHODS
    /*
    - Create a symbolic link from one path to another with Files.createSymbolicLink
    - Create a hard link from one path to another with Files.createLink
    - Determine and print the MIME type of a specified file with Files.probeContentType
    - Check if a specified path is a symbolic link and return a boolean using Files.isSymbolicLink
    - Retrieve and print the file store attributes for a specified path using Files.getFileStore
    */
    public static void createSymbolicLink(String source, String target) throws IOException {
        if(source.isBlank() || target.isBlank()) {
            System.out.println("Source and target names cannot be blank.");
        } else {
            Path sourcePath = Path.of(source);
            Path targetPath = Path.of(target);
            Files.createSymbolicLink(sourcePath, targetPath);
            System.out.println("Symbolic Link created");
        }
    }
    public static void createHardLink(String source, String target) throws IOException {
        if(source.isBlank() || target.isBlank()) {
            System.out.println("Source and target names cannot be blank.");
        }  else {
            Path sourcePath = Path.of(source);
            Path targetPath = Path.of(target);
            Files.createLink(sourcePath, targetPath);
            System.out.println("Link created");
        }
    }
    public static String getMimeType(String fileName) throws IOException {
        Path path = Path.of(fileName);
        return Files.probeContentType(path).toString();
    }
    public static boolean isSymbolicLink(String fileName) throws IOException {
        Path path = Path.of(fileName);
        return Files.isSymbolicLink(path);
    }
    public static void printFileStoreAttributes(String file) throws IOException {
        Path path = Path.of(file);
        FileStore fs = Files.getFileStore(path);
        System.out.println(STR."File store: \{fs.toString()}");
        System.out.println(STR."Total space: \{fs.getTotalSpace()/1024} bytes");
        System.out.println(STR."Used space: \{(fs.getTotalSpace()-fs.getUnallocatedSpace())/1024} bytes");
        System.out.println(STR."Available space: \{fs.getUsableSpace()/1024} bytes");
    }

    public static void main(String[] args) throws IOException {
        String fileWithPath = "/home/nuria/Cursos/ejerciciosJava/javaExercises/src/main/java/com/example/ex.txt";
        String oldFile = "/home/nuria/Cursos/ejerciciosJava/javaExercises/src/main/java/com/example/files/oldFile.txt";
        String temFile = "/home/nuria/Cursos/ejerciciosJava/javaExercises/src/main/java/com/example/files/tempFile.txt";
        String filePath = "/home/nuria/Cursos/ejerciciosJava/javaExercises/src/main/java/com/example/files";
        String copyPath = "/home/nuria/Cursos/ejerciciosJava/javaExercises/src/main/java/com/example/filesCopy";
        String newDir1 = "/home/nuria/Cursos/ejerciciosJava/javaExercises/src/main/java/com/example/files";
        String newDir2 = "/home/nuria/Cursos/ejerciciosJava/javaExercises/src/main/java/com/example/files/dir1/dir2";
        String newDir3 = "/home/nuria/Cursos/ejerciciosJava/javaExercises/src/main/java/com/example/filesCopy/dir1";
        String newDir4 = "/home/nuria/Cursos/ejerciciosJava/javaExercises/src/main/java/com/example/files/dir1/dir2/dir3";
        String dirForCopy = "/home/nuria/Cursos/ejerciciosJava/javaExercises/src/main/java/com/example/filesCopy/dir1/dir2";
        String dirForCopy2 = "/home/nuria/Cursos/ejerciciosJava/javaExercises/src/main/java/com/example/filesCopy/dir1/dir2/dir3";
        String binaryFile = "/home/nuria/Cursos/ejerciciosJava/javaExercises/src/main/java/com/example/files/previous-compilation-data.bin";
        Path path = Paths.get(filePath,"test2.txt");
        String link1 = "/home/nuria/Cursos/ejerciciosJava/javaExercises/src/main/java/com/example/links/symbolicLink.bin";
        String link2 = "/home/nuria/Cursos/ejerciciosJava/javaExercises/src/main/java/com/example/links/hardLink.bin";

        //System.out.println(STR."Does directory exists? \{doesDirectoryExist(copyPath)}");
        //createDirectory(newDir3);
        //createDirectoryStructure(newDir4);
        //deleteDirectoryAndContent(newDir4);
        //System.out.println(STR."Total copied files: \{copyAllFiles(newDir3, dirForCopy)}");
        //System.out.println(STR."Total copied files: \{copyDirectoryAndContent(newDir3,dirForCopy2)}");
        //System.out.println(STR."Total moved files: \{moveDirectoryAndContent(dirForCopy2,newDir2)}");
        //DirectoryStream<Path> directoryStream = createDirectoryStream(filePath);
        //directoryStream.forEach(p -> System.out.println(p.toString()));
        //System.out.println(getAllFiles(filePath));
        //System.out.println(getFilesWithExtension(filePath,"bin"));
        //List<String> files = getFilesAndDirectory(filePath);
        //files.stream().forEach(System.out::println);
        //createTemporaryDirectory(newDir1);
        //createTemporaryDirectory(newDir2);
        //createSymbolicLink(link1,binaryFile);
        //createHardLink(link2,binaryFile);
        //System.out.println(getMimeType(fileWithPath));
        //System.out.println(getMimeType(binaryFile));
        //System.out.println(isSymbolicLink(link1));
        //System.out.println(isSymbolicLink(link2));
        //printFileStoreAttributes("/home");

    }
}
