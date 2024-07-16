import java.util.*;

// IndexStore component
class IndexStore {
    private Map<String, Map<String, Integer>> index;

    public IndexStore() {
        this.index = new HashMap<>();
    }

    // Update the index with a new document
    public void updateIndex(String documentName, Map<String, Integer> documentIndex) {
        index.put(documentName, documentIndex);
    }

    // Perform a single term lookup operation
    public Map<String, Integer> lookup(String term) {
        return index.getOrDefault(term, new HashMap<>());
    }
}

// ProcessingEngine component
class ProcessingEngine {
    private IndexStore indexStore;

    public ProcessingEngine(IndexStore indexStore) {
        this.indexStore = indexStore;
    }

    // Indexing: Extract words from document and update index
    public void indexDocument(String documentName, String[] words) {
        Map<String, Integer> documentIndex = new HashMap<>();
        for (String word : words) {
            documentIndex.put(word, documentIndex.getOrDefault(word, 0) + 1);
        }
        indexStore.updateIndex(documentName, documentIndex);
    }

    // Search: Retrieve top 10 documents containing search terms
    public List<String> search(String[] terms) {
        Map<String, Integer> combinedResults = new HashMap<>();
        for (String term : terms) {
            Map<String, Integer> termIndex = indexStore.lookup(term);
            for (Map.Entry<String, Integer> entry : termIndex.entrySet()) {
                combinedResults.put(entry.getKey(), combinedResults.getOrDefault(entry.getKey(), 0) + entry.getValue());
            }
        }

        // Sort the combined results by occurrence
        List<Map.Entry<String, Integer>> sortedResults = new ArrayList<>(combinedResults.entrySet());
        sortedResults.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        // Get top 10 files
        List<String> topFiles = new ArrayList<>();
        int count = 0;
        for (Map.Entry<String, Integer> entry : sortedResults) {
            if (count >= 10) break;
            topFiles.add(entry.getKey());
            count++;
        }
        return topFiles;
    }
}

// Main class
public class FileRetrievalEngine {
    public static void main(String[] args) {
        IndexStore indexStore = new IndexStore();
        ProcessingEngine processingEngine = new ProcessingEngine(indexStore);
        AppInterface appInterface = new AppInterface(processingEngine);

        appInterface.handleUserInput();
    }
}

// AppInterface component
class AppInterface {
    private ProcessingEngine processingEngine;

    public AppInterface(ProcessingEngine processingEngine) {
        this.processingEngine = processingEngine;
    }

    // Method to handle user input and interaction
    public void handleUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter command: (index, search, exit)");
        String command = scanner.nextLine();

        switch (command) {
            case "index":
                System.out.println("Enter document name:");
                String documentName = scanner.nextLine();
                System.out.println("Enter content:");
                String content = scanner.nextLine();
                String[] words = content.split("\\W+");
                processingEngine.indexDocument(documentName, words);
                System.out.println("Document indexed successfully.");
                break;
            case "search":
                System.out.println("Enter search query:");
                String query = scanner.nextLine();
                String[] terms = query.split("\\s+"); // Split by whitespace
                List<String> results = processingEngine.search(terms);
                System.out.println("Search results:");
                for (String result : results) {
                    System.out.println(result);
                }
                break;
            case "exit":
                System.out.println("Exiting...");
                System.exit(0);
            default:
                System.out.println("Invalid command.");
        }
    }
}