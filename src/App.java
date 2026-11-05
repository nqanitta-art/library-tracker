import java.util.List;
import java.util.Scanner;

public class App {
    private static final BookStore store = new BookStore("books.json");

    static void printBanner() {
        System.out.println("=== Library Tracker ===");
    }

    public static void main(String[] args) {
        printBanner();
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println();
            System.out.println("1) Add book  2) List books  0) Exit");
            System.out.print("Choose: ");
            String choice = in.nextLine().trim();
            if (choice.equals("1")) {
                addBook(in);
            } else if (choice.equals("2")) {
                listBooks();
            } else if (choice.equals("0")) {
                System.out.println("Bye!");
                break;
            } else {
                System.out.println("Unknown option");
            }
        }
    }
    static void addBook(Scanner in) {
        System.out.print("Title: ");
        String title = in.nextLine().trim();
        System.out.print("Author: ");
        String author = in.nextLine().trim();
        if (title.isEmpty() || author.isEmpty()) {
            System.out.println("Title and author cannot be empty.");
            return;
        }
        List<Book> books = store.load();
        books.add(new Book(title, author));
        store.save(books);
        System.out.println("Book added.");
    }

    static void listBooks() {
        List<Book> books = store.load();
        if (books.isEmpty()) {
            System.out.println("No books yet.");
            return;
        }
        for (int i = 0; i < books.size(); i++) {
            System.out.println((i + 1) + ". " + books.get(i));
        }
    }

    static void searchBooks(Scanner in) {
        System.out.print("Search title: ");
        String query = in.nextLine().trim();
        List<Book> books = store.load();
        boolean found = false;
        for (Book b : books) {
            if (b.getTitle().contains(query)) {
                System.out.println("- " + b);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No matching books.");
        }
    }
}