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
    try {
      this.document = Jsoup.connect(PLACEHOLDER_URL).get(); // TODO: change to user input
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void getAllLinks() {
    this.allLinks = new HashMap<String, String>();
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
        System.out.println(artiTitle + " " + artiURL);
      }

    }
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


  //testing
}
