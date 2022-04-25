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
  private Map<String, List<String>> outputMap = new HashMap<>();

  public DocumentSearch() {
    // TODO
  }

  public void getAllLinks() {
    // TODO
  }

  public boolean searchLinks(String term) {
    // TODO
    return true;
  }

  public int getTfIdf() {
    // TODO
    return 0;
  }

  public int getCosineSimilarity() {
    // TODO
    return 0;
  }

  public Map<String, List<String>> getResults(int orderPreference) {
    return this.outputMap;
  }
}
