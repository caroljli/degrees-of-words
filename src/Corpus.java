import java.util.List;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class represents a corpus of documents and creates an inverted index for these documents.
 * Adapted from swapneel's Corpus class.
 */

public class Corpus {
  private final List<TextDocument> textDocuments;
  private final HashMap<String, Set<TextDocument>> invertedIndex;

  public Corpus(List<TextDocument> textDocuments) {
    this.textDocuments = textDocuments;
    this.invertedIndex = new HashMap<String, Set<TextDocument>>();
    this.createInvertedIndex();
  }

  /**
   * Creates the inverted index.
   */
  private void createInvertedIndex() {
    for (TextDocument textDocument : this.textDocuments) {
      Set<String> terms = textDocument.getTermList();

      for (String term : terms) {
        if (this.invertedIndex.containsKey(term)) {
          Set<TextDocument> list = this.invertedIndex.get(term);
          list.add(textDocument);
        } else {
          Set<TextDocument> list = new TreeSet<>();
          list.add(textDocument);
          this.invertedIndex.put(term, list);
        }
      }
    }
  }

  /**
   * Processes the idf for a given term.
   * @param term a term in a document
   * @return the idf for the term
   */
  public double getInverseDocumentFrequency(String term) {
    if (invertedIndex.containsKey(term)) {
      double size = this.textDocuments.size();
      Set<TextDocument> list = this.invertedIndex.get(term);
      double documentFrequency = list.size();

      return Math.log10(size / documentFrequency);
    } else {
      return 0;
    }
  }

  /**
   * @return the documents
   */
  public List<TextDocument> getDocuments() {
    return this.textDocuments;
  }

  /**
   * @return the invertedIndex
   */
  public HashMap<String, Set<TextDocument>> getInvertedIndex() {
    return this.invertedIndex;
  }
}
