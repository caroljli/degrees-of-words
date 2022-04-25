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

  public DocumentSearch() {
    try {
      this.document = Jsoup.connect(PLACEHOLDER_URL).get(); // TODO: change to user input
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /*
  This method will navigate to the base URL and collect all the links in the main body. The links are added to the
  hash map "allLinks" with the relevant article title. It then navigates to these links and collects all text in the
  topic-paragraph class and adds them to a map with the article title and the relevant text.
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

  /*
  This method is a private method used in function "getAllLinks" that navigates to an input URL and retrieves all text
  in the main body that is considered a topic-paragraph by Jsoup. We use the ignoreContentType command to avoid
  exceptions encountered when travelling to a .jpeg link.

  NOTE: The string returned CAN be empty.
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
