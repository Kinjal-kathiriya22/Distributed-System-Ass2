import java.io.File;
import java.util.Scanner;

public class QuitFileRetrievalEngine {
    private static final String DIRECTORY_PATH = "/path/to/your/directory"; // Change this to your directory path

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
            } else {
                System.out.println("Unsupported command. Type 'help' for a list of supported commands.");
            }
        }

        scanner.close();
        System.out.println("Application terminated.");
    }

    private static void printHelp() {
        System.out.println("Supported commands:");
        System.out.println("- retrieve <filename>: Retrieve a file.");
        System.out.println("- delete <filename>: Delete a file.");
        System.out.println("- list: List all files in the directory.");
        System.out.println("- quit: Close the application.");
    }

    private static void listFiles() {
        File directory = new File(DIRECTORY_PATH);
        File[] files = directory.listFiles();
        if (files != null) {
            System.out.println("Files in directory:");
            for (int i = 0; i < files.length; i++) {
                System.out.println((i + 1) + ". " + files[i].getName());
            }
        } else {
            System.out.println("Directory is empty or does not exist.");
        }
    }
}