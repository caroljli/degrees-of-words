import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DocumentSearch {
  private Document document;
  //  final String PLACEHOLDER_URL = "https://www.britannica.com/animal/jellyfish";
//  final String PLACEHOLDER_TERMS = "often";
  private final TextDocument query;

  private Map<String, String> allLinks = new HashMap<>();
  private Map<String, String> bodyText = new HashMap<>();
  private Map<String, List<String>> outputMap = new HashMap<>();

  private final HashMap<TextDocument, HashMap<String, Double>> tfIdfWeights;
  private final HashMap<TextDocument, Double> cosineSimilarities;
  private final HashMap<TextDocument, Double> tfs;
  private final HashMap<TextDocument, Integer> documentLengths;
  private final List<TextDocument> textDocuments;

  public DocumentSearch(String url, String queryText) {
    this.query = new TextDocument(queryText);
    try {
      this.document = Jsoup.connect(url).get();
    } catch (IOException e) {
      e.printStackTrace();
    }

    tfIdfWeights = new HashMap<>();
    cosineSimilarities = new HashMap<>();
    tfs = new HashMap<>();
    documentLengths = new HashMap<>();
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
    textDocuments.add(query); // TODO: change with actual term input
    for (String text : this.bodyText.values()) {
      this.textDocuments.add(new TextDocument(text));
    }
    Corpus corpus = new Corpus(this.textDocuments);

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
    for (TextDocument textDocument : this.textDocuments) {
      try {
      if (textDocument.toString().length() > 0) this.cosineSimilarities.put(textDocument,
          this.getCosineSimilarity(query, textDocument));
      } catch (NullPointerException e) {
        System.out.println("NullPointerException: invalid query");
        e.printStackTrace();
      }

    }
  }

  public HashMap<TextDocument, HashMap<String, Double>> getTfIdfWeights() {
    return this.tfIdfWeights;
  }

  public HashMap<TextDocument, Double> getCosineSimilarities() {
    return this.cosineSimilarities;
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
    System.out.println(d1);
    System.out.println(d2);
    HashMap<String, Double> weights1 = this.tfIdfWeights.get(d1);
    HashMap<String, Double> weights2 = this.tfIdfWeights.get(d2);

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

  public int getDocumentLength(TextDocument d2) {
    return d2.getDocumentWordCount();
  }

  public void createTfMap(String secondTerm) {
    for (TextDocument textDocument : this.textDocuments) {
      try {
        if (textDocument.toString().length() > 0) this.tfs.put(textDocument,
                textDocument.getTermFrequency(secondTerm));
      } catch (NullPointerException e) {
        System.out.println("NullPointerException: invalid query");
        e.printStackTrace();
      }
    }
  }

  public void createDocumentLengthMap() {
    for (TextDocument textDocument : this.textDocuments) {
      try {
        if (textDocument.toString().length() > 0) this.documentLengths.put(textDocument,
                textDocument.getDocumentWordCount());
      } catch (NullPointerException e) {
        System.out.println("NullPointerException: invalid query");
        e.printStackTrace();
      }
    }
  }

  /**
   * Returns a HashMap containing statistics of each link in a main doc
   * @param secondTerm second term to be searched in each link
   * @return the output hashmap
   */
  public Map<String, List<String>> createOutputMap(String secondTerm) {
    this.outputMap = new HashMap<>();
    for (String title : this.bodyText.keySet()) {
      List<String> results = new LinkedList();
      TextDocument currDoc = new TextDocument(this.bodyText.get(title));
      double tf = currDoc.getTermFrequency(secondTerm);
      double cosineSimilarity = getCosineSimilarity(query, currDoc);
      int docLength = getDocumentLength(currDoc);
      results.add("tf: " + tf);
      results.add("cosine similarity: " + cosineSimilarity);
      results.add("word count: " + docLength);
      outputMap.put(title, results);
      System.out.println(title + " " + tf + " " + cosineSimilarity + " " + docLength);
    }
    return this.outputMap;
  }

  /**
   *
   */
  public void getResults(int orderPreferences) {
    if (orderPreferences == 1) {

      Stream<Map.Entry<TextDocument, Double>> sorted = this.tfs.entrySet().stream().sorted(Map.Entry.comparingByValue());
//      Map<Integer, String> sortedMap =
//              this.tfs.entrySet().stream()
//                      .sorted(Map.Entry.comparingByValue())
//                      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
//                              (e1, e2) -> e1, LinkedHashMap::new));

    } else if (orderPreferences == 2) {

    } else if (orderPreferences == 3) {

    }
    Map<String, List<String>> orderedOutputMap = new HashMap<>();
    return this.outputMap;
  }

}
