import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class IndexFileRetrievalEngine {
    private static final Map<String, Integer> index = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("File Retrieval Engine v1.0");
        System.out.println("Type 'help' for a list of supported commands.\n");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("quit")) {
                System.out.println("Closing application...");
                break;
            } else if (input.equalsIgnoreCase("help")) {
                printHelp();
            } else if (input.startsWith("index")) {
                String[] parts = input.split("", 2);
                if (parts.length == 2) {
                    indexDirectory(parts[1]);
                    System.out.println("Indexing completed.");
                } else {
                    System.out.println("invalid command");
                }
            } else if (input.equalsIgnoreCase("list")) {
                listFiles();
            } else {
                System.out.println("Unsupported command. Type 'help' for a list of supported commands.");
            }
        }

        scanner.close();
        System.out.println("Application terminated.");
    }

    private static void indexDirectory(String path) {
        File directory = new File(path);
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Indexing completed.");
            return;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                indexFile(file);
            }
        } else {
            System.out.println("Directory is empty.");
        }
    }

    private static void indexFile(File file) {
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNext()) {
                String line = fileScanner.nextLine();
                String[] words = line.split("[^a-zA-Z0-9]+");
                for (String word : words) {
                    if (!word.isEmpty()) {
                        word = word.toLowerCase();
                        index.put(word, index.getOrDefault(word, 0) + 1);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + file.getName());
        }
    }

    private static void listFiles() {
        File directory = new File(System.getProperty("user.dir"));
        File[] files = directory.listFiles();
        if (files != null && files.length > 0) {
            System.out.println("Files in directory:");
            for (File file : files) {
                System.out.println(file.getName());
            }
        } else {
            System.out.println("Directory is empty or does not exist.");
        }
    }

    private static void printHelp() {
        System.out.println("Supported commands:");
        System.out.println("- index <dataset path>: Index all files in the given dataset path.");
        System.out.println("- list: List all files in the current directory.");
        System.out.println("- quit: Close the application.");
    }
}