import java.util.Scanner;

public class BritannicaMain {

    public static void main (String[] args) {
        DocumentSearch test = new DocumentSearch();
        test.getAllLinks();
        test.createTfIdf();
        test.createCosineSimilarity();

//        System.out.println(test.getTfIdfWeights());
//        System.out.println(test.getCosineSimilarities());
    }
}
