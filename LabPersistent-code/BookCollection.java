/**
 * The given code is provided to assist you to complete the required tasks. But the given code is
 * often incomplete. You have to read and understand the given code carefully, before you can apply
 * the code properly. You might need to implement additional procedures, such as error checking and
 * handling, in order to apply the code properly.
 */

// you need to import some xml libraries
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
// import any standard library if needed

/**
 * A book collection holds 0 or more books in a collection.
 */
public class BookCollection {
  private List<Book> books;

  /**
   * Creates a new collection with no books by default.
   */
  public BookCollection() {
    this.books = new ArrayList<Book>();
  }

  /**
   * Creates a new book collection with the specified list of books pre-defined.
   *
   * @param books A books list.
   */
  public BookCollection(List<Book> books) {
    this.books = books;
  }

  /**
   * Returns the current list of books stored by this collection.
   *
   * @return A (mutable) list of books.
   */
  public List<Book> getList() {
    return books;
  }

  /**
   * Sets the list of books in this collection to the specified value.
   */
  public void setList(List<Book> books) {
    this.books = books;
  }

  /**
   * A simple human-readable toString implementation. Not particularly useful to save to disk.
   *
   * @return A human-readable string for printing
   */
  @Override
  public String toString() {
    return this.books.stream().map(book -> " - " + book.display() + "\n")
        .collect(Collectors.joining());
  }

  /**
   * Saves this collection to the specified "bespoke" file.
   *
   * @param file The path to a file.
   */
  public void saveToBespokeFile(File file) {
    // TODO: Implement this function yourself. The specific hierarchy is up to you,
    // but it must be in a bespoke format and should match the
    // load function.
    String filename = file.getName();
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
      oos.writeObject(this);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }


  }

  /**
   * Saves this collection to the specified JSON file.
   *
   * @param file The path to a file.
   */
  public void saveToJSONFile(File file) {
    // TODO: Implement this function yourself. The specific hierarchy is up to you,
    // but it must be in a JSON format and should match the load function.
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    try (FileWriter writer = new FileWriter(file)) {
      gson.toJson(getList(), writer);
    } catch (IOException e) {
      e.printStackTrace();
    }


  }

  /**
   * Saves this collection to the specified XML file.
   *
   * @param file The path to a file.
   */
  public void saveToXMLFile(File file) {
    // TODO: Implement this function yourself. The specific hierarchy is up to you,
    // but it must be in an XML format and should match the
    // load function.
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    try {
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.newDocument();

      Element rootEle = doc.createElement("Books");
      doc.appendChild(rootEle);

      for (Book book : books) {
        Element BookEle = doc.createElement("Book");
        rootEle.appendChild(BookEle);

        Element title = doc.createElement("Title");
        title.appendChild(doc.createTextNode(book.title));
        BookEle.appendChild(title);

        Element authorName = doc.createElement("AuthorName");
        authorName.appendChild(doc.createTextNode(book.authorName));
        BookEle.appendChild(authorName);

        Element year = doc.createElement("Year");
        year.appendChild(doc.createTextNode(Integer.toString(book.yearReleased)));
        BookEle.appendChild(year);

        Element genre = doc.createElement("Genre");
        genre.appendChild(doc.createTextNode(book.bookGenre.name));
        BookEle.appendChild(genre);

      }

      Transformer tran = TransformerFactory.newInstance().newTransformer();
      tran.setOutputProperty(OutputKeys.ENCODING, "utf-8");

      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(file);
      tran.transform(source, result);

    } catch (Exception e) {
      e.printStackTrace();
    }


  }

  /**
   * Load a pre-existing book collection from a "bespoke" file.
   *
   * @param file The file to load from. This is guaranteed to exist.
   * @return An initialised book collection.
   */
  public static BookCollection loadFromBespokeFile(File file) {
    // TODO: Implement this function yourself.
    String filename = file.getName();

    BookCollection result = null;
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
      result = (BookCollection) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }



    return result;



  }

  /**
   * Load a pre-existing book collection from a JSON file.
   *
   * @param file The file to load from. This is guaranteed to exist.
   * @return An initialised book collection.
   */
  public static BookCollection loadFromJSONFile(File file) {
    // TODO: Implement this function yourself.
    List<Book> loadbook = new ArrayList<Book>();

    Gson gson = new Gson();
    JsonReader jRead = null;
    final Type BOOK_LIST = new TypeToken<List<Book>>() {}.getType();
    try {
      jRead = new JsonReader(new FileReader(file));

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    BookCollection bookCollection =new BookCollection(gson.fromJson(jRead, BOOK_LIST));

    return bookCollection;


  }

  /**
   * Load a pre-existing book collection from an XML file.
   *
   * @param file The file to load from. This is guaranteed to exist.
   * @return An initialised book collection.
   */
  public static BookCollection loadFromXMLFile(File file) {
    // TODO: Implement this function yourself.

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    List<Book> loadbook = new ArrayList<Book>();
//    BookCollection bookCollection =new BookCollection(loadbook);

    try {
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(file);

      doc.getDocumentElement().normalize();

      NodeList nl = doc.getElementsByTagName("Book");

      for (int i = 0; i < nl.getLength(); i++) {
        Node n = nl.item(i);
        if (n.getNodeType() == Node.ELEMENT_NODE) {
          Element elem = (Element) n;

          String title = elem.getElementsByTagName("Title").item(0).getTextContent();
          String aname = elem.getElementsByTagName("AuthorName").item(0).getTextContent();
          int year = Integer.parseInt(elem.getElementsByTagName("Year").item(0).getTextContent());
          String genre = elem.getElementsByTagName("Genre").item(0).getTextContent();


          BookGenre bgenre = findEnumbyName(genre);

          Book book = new Book(title, aname, year, bgenre);

          loadbook.add(book);

        }
      }

      BookCollection bookCollection = new BookCollection(loadbook);

    } catch (Exception e) {
      e.printStackTrace();
    }

    BookCollection bookCollection =new BookCollection(loadbook);

    return bookCollection;
  }


  public static BookGenre findEnumbyName(String name){
    for(BookGenre bookGenre : BookGenre.values()){
      if(bookGenre.display().equals(name)){
        return bookGenre;
      }
    }
    throw new IllegalArgumentException("name is not support");
  }

  public static void main(String arg[]) {
    Book book1 = new Book("a","bc",1234,BookGenre.NON_FICTION);
    File file = new File("book1.bin");




  }
}
