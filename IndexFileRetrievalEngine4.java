import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class IndexFileRetrievalEngine4 {
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
                long startTime = System.currentTimeMillis();
                String[] parts = input.split(" ", 2);
                if (parts.length == 2) {
                    indexDirectory(parts[1]);
                    long endTime = System.currentTimeMillis();
                    double executionTime = (endTime - startTime) / 1000.0; // in seconds
                    double datasetSizeMB = getDatasetSize(parts[1]); // in MB
                    double throughput = (datasetSizeMB / executionTime) * 1000; // in MB/s
                    System.out.println("Indexing completed.");
                    System.out.printf("Indexing throughput: %.2f MB/s\n", throughput);
                } else {
                    System.out.println("Invalid command");
                }
            } else if (input.equalsIgnoreCase("list")) {
                listFiles();
            } else if (input.startsWith("search")) {
                String query = input.substring("search".length()).trim();
                searchFiles(query);
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

    private static void searchFiles(String query) {
        String[] terms = query.split("AND");
        Map<File, Integer> fileOccurrences = new HashMap<>();

        // Count occurrences of each term in each file
        for (String term : terms) {
            int termCount = index.getOrDefault(term.trim().toLowerCase(), 0);
            for (Map.Entry<String, Integer> entry : index.entrySet()) {
                if (entry.getKey().contains(term)) {
                    int totalOccurrences = fileOccurrences.getOrDefault(new File(entry.getKey()), 0);
                    fileOccurrences.put(new File(entry.getKey()), totalOccurrences + entry.getValue());
                }
            }
        }

        // Sort files by the total number of occurrences of all terms
        List<Map.Entry<File, Integer>> fileList = new ArrayList<>(fileOccurrences.entrySet());
        fileList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // Print top 10 files
        System.out.println("Top 10 files containing all terms:");
        int count = 0;
        for (Map.Entry<File, Integer> entry : fileList) {
            if (count >= 10) break;
            System.out.println(entry.getKey().getName() + " - Total occurrences: " + entry.getValue());
            count++;
        }
    }

    private static void printHelp() {
        System.out.println("Supported commands:");
        System.out.println("- index <dataset path>: Index all files in the given dataset path.");
        System.out.println("- list: List all files in the current directory.");
        System.out.println("- search <AND query>: Perform a search query. The query must be an AND query. For example, 'search cats AND dogs'.");
        System.out.println("- quit: Close the application.");
    }

    private static double getDatasetSize(String path) {
        File directory = new File(path);
        if (!directory.exists() || !directory.isDirectory()) {
            return 0;
        }
        File[] files = directory.listFiles();
        double totalSize = 0;
        if (files != null) {
            for (File file : files) {
                totalSize += file.length();
            }
        }
        // Convert bytes to MB
        return totalSize / (1024 * 1024);
    }
}