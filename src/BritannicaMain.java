import java.util.Scanner;

public class BritannicaMain {

    public static void main (String[] args) {
        DocumentSearch documentSearch = new DocumentSearch();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please input the Brittanica page that you want to search.");
        String brittanicaInput = scanner.nextLine();

        System.out.println("Please intput the query text.");
        String query = scanner.nextLine();

        documentSearch.getAllLinks();
        documentSearch.createTfIdf();
        documentSearch.createCosineSimilarity();

//        System.out.println(test.getTfIdfWeights());
//        System.out.println(test.getCosineSimilarities());
    }
}
