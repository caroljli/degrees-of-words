import java.util.*;

/**
 * This class represents a document and keeps track of its term frequencies.
 * Adapted from swapneel's Document class.
 */

public class TextDocument implements Comparable<TextDocument> {

  /**
   * Term frequency HashMap to keep track of term frequencies.
   */
  private final HashMap<String, Integer> termFrequency;

  /**
   * The inputText to process for term frequency.
   */
  private final String inputText;

  /**
   * Constructor for Document class; takes input class and retrieves the term frequency.
   * @param inputText Text to process
   */
  public TextDocument(String inputText) {
    this.inputText = inputText;
    this.termFrequency = new HashMap<>();
    this.processTermFrequencies();
  }

  private void processTermFrequencies() {
    String[] terms = this.inputText.split(" ");

    for (String nextWord : terms) {
      String filteredWord = nextWord.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
      if (!(filteredWord.equalsIgnoreCase(""))) {
        if (this.termFrequency.containsKey(filteredWord)) {
          int oldCount = this.termFrequency.get(filteredWord);
          this.termFrequency.put(filteredWord, ++oldCount);
        } else {
          this.termFrequency.put(filteredWord, 1);
        }
      }
    }

  }

  public double getTermFrequency(String word) {
    return this.termFrequency.getOrDefault(word, 0);
  }

  public String getInputText() {
    return this.inputText;
  }

  public Set<String> getTermList() {
    return this.termFrequency.keySet();
  }

  public int compareTo(TextDocument other) {
    return inputText.compareTo(other.getInputText());
  }

  public String toString() {
    return this.inputText;
  }

}
