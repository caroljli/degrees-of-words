import java.util.Scanner;

public class BritannicaMain {
    public static void main (String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please input the Brittanica page link that you want to search.");
        String brittanicaUrl = scanner.nextLine();

        System.out.println("Please input the query text.");
        String query = scanner.nextLine();

        System.out.println("Please input an integer between 1-3 to rank links by:");
        System.out.println("1 - Term frequency, 2 - Cosine Similarity, 3 - Document length");
        String statQuery = scanner.nextLine();

        DocumentSearch documentSearch = new DocumentSearch(brittanicaUrl, query);

        documentSearch.getAllLinks();
        documentSearch.createTfIdf();
        documentSearch.createCosineSimilarity();
        documentSearch.createOutputMap(query);
        documentSearch.getResults(Integer.parseInt(statQuery));

        //System.out.println(documentSearch.getTfIdfWeights());
        //System.out.println(documentSearch.getCosineSimilarities());
    }
}
