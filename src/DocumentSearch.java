import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DocumentSearch {
  private Document document;
  final String PLACEHOLDER_URL = "https://www.britannica.com/animal/jellyfish";
  final String PLACEHOLDER_TERMS = "algae";

  private Map<String, String> allLinks = new HashMap<>();
  private Map<String, String> bodyText = new HashMap<>();
  private Map<String, List<String>> outputMap = new HashMap<>();

  private HashMap<TextDocument, HashMap<String, Double>> tfIdfWeights;
  private HashMap<TextDocument, Double> cosineSimilarities;
  private Corpus corpus;
  private List<TextDocument> textDocuments;

  public DocumentSearch() {
    try {
      this.document = Jsoup.connect(PLACEHOLDER_URL).get(); // TODO: change to user input
    } catch (IOException e) {
      e.printStackTrace();
    }

    textDocuments = new ArrayList<>();
  }

  /**
   * This method will navigate to the base URL and collect all the links in the main body. The links are added to the
   * HashMap "allLinks" with the relevant article title. It then navigates to these links and collects all text in the
   * topic-paragraph class and adds them to a map with the article title and the relevant text.
   */
  public void getAllLinks() {
    this.allLinks = new HashMap<>();
    this.bodyText = new HashMap<>();
    Elements main = this.document.select("main");
    Element content = main.select("div.topic-content").first();
    if (content != null) {
      Elements links = content.select("a[href]");
      for (Element link : links) {
        Elements aTag = link.select("a");
        Element a = aTag.get(0);
        String artiURL = a.attr("abs:href");
        String artiTitle = a.text();
        this.allLinks.put(artiTitle, artiURL);
        //System.out.println(artiTitle + " " + artiURL);
        String contentText = getBodyText(artiURL);
        //System.out.println(contentText);
        this.bodyText.put(artiTitle, contentText);
      }
    }
  }

  /**
   * This method is a private method used in function "getAllLinks" that navigates to an input URL and retrieves all
   * text in the main body that is considered a topic-paragraph by JSoup. We use the ignoreContentType command to avoid
   * exceptions encountered when travelling to a .jpeg link.
   *
   * NOTE: The string returned CAN be empty.
   * @param relURL URL to navigate to
   * @return Text in main body that is considered a topic-paragraph
   */
  private String getBodyText(String relURL) {
    String text = "";
    try {
      this.document = Jsoup.connect(relURL).ignoreContentType(true).get();
    } catch (IOException e) {
      e.printStackTrace();
    }
    Elements main = this.document.select("main");
    Elements paragraphs = main.select("p.topic-paragraph");
    for (Element p : paragraphs) {
      String desc = p.text();
      text += desc;
    }
    return text;
  }

  /**
   * Creates the tf-idf vectors per body text of each link and stores as a HashMap of the TextDocument of the body text
   * of each link and the associated tf-idf weight.
   */
  public void createTfIdf() {
    for (String text : this.bodyText.values()) {
      this.textDocuments.add(new TextDocument(text));
    }
    this.corpus = new Corpus(this.textDocuments);

    Set<String> terms = corpus.getInvertedIndex().keySet();
    for (TextDocument textDocument : corpus.getDocuments()) {
      HashMap<String, Double> weights = new HashMap<>();

      for (String term: terms) {
        double tf = textDocument.getTermFrequency(term);
        double idf = corpus.getInverseDocumentFrequency(term);
        double weight = tf * idf;
        weights.put(term, weight);
      }
      this.tfIdfWeights.put(textDocument, weights);
    }
  }

  /**
   * Creates a HashMap of the cosine similarities of each document from each link of the user input query.
   */
  public void createCosineSimilarity() {
    TextDocument query = new TextDocument(PLACEHOLDER_TERMS); // TODO: change with actual term input
    for (int i = 1; i < this.textDocuments.size(); i++) {
      TextDocument currDoc = textDocuments.get(i);
      this.cosineSimilarities.put(currDoc, this.getCosineSimilarity(query, currDoc));
    }
  }

  /**
   * Return the magnitude of a vector.
   * @param document the document to calculate the magnitude of.
   * @return the magnitude
   */
  private double getMagnitude(TextDocument document) {
    double magnitude = 0;
    HashMap<String, Double> weights = this.tfIdfWeights.get(document);
    for (double weight : weights.values()) {
      magnitude += weight * weight;
    }

    return Math.sqrt(magnitude);
  }

  /**
   * This will take two documents and return the dot product.
   * @param d1 Document 1
   * @param d2 Document 2
   * @return the dot product of the documents
   */
  private double getDotProduct(TextDocument d1, TextDocument d2) {
    double product = 0;
    HashMap<String, Double> weights1 = tfIdfWeights.get(d1);
    HashMap<String, Double> weights2 = tfIdfWeights.get(d2);

    for (String term : weights1.keySet()) {
      product += weights1.get(term) * weights2.get(term);
    }

    return product;
  }

  /**
   * Returns the cosine similarity (ranging from 0 to 1) of two documents.
   * @param d1 Document 1
   * @param d2 Document 2
   * @return the cosine similarity
   */
  public double getCosineSimilarity(TextDocument d1, TextDocument d2) {
    return getDotProduct(d1, d2) / (getMagnitude(d1) * getMagnitude(d2));
  }

  public Map<String, List<String>> getResults(int orderPreference) {
    return this.outputMap;
  }

  //testing
}
