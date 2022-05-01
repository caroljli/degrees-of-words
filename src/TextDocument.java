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

  /**
   * Processes all frequencies for each term contained in the file by converting to lower case, removing non-letters,
   * and updating the frequency in the private term-frequency map.
   */
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

  /**
   * Returns the term frequency for a given word. Term frequency for non-document words is 0.
   * @param word query term
   * @return the term frequency for the word in the document
   */
  public double getTermFrequency(String word) {
    return this.termFrequency.getOrDefault(word, 0);
  }

  /**
   * Returns the number of words in each document
   * @return integer number of words
   */
  public int getDocumentWordCount() {
    String[] words = this.inputText.split(" ");
    return words.length;
  }

  /**
   * @return the input document to process for term frequency, represented as a String.
   */
  public String getInputText() {
    return this.inputText;
  }

  /**
   * @return the list of terms for the input text
   */
  public Set<String> getTermList() {
    return this.termFrequency.keySet();
  }

  /**
   * The overridden method from Comparable interface, used when one inputText document is compared to another.
   * @param other the inputText to compare this inputText to.
   * @return the Comparable output of String comparison, represented as an integer
   */
  public int compareTo(TextDocument other) {
    return this.inputText.compareTo(other.getInputText());
  }

  /**
   * @return the input document to process for term frequency, represented as a String.
   */
  public String toString() {
    return this.inputText;
  }

}
