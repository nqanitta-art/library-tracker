import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BookStore {
    private final Path file;

    public BookStore(String fileName) {
        this.file = Paths.get(fileName);
    }

    // Reads all books from books.json
    public List<Book> load() {
        List<Book> books = new ArrayList<>();
        if (!Files.exists(file)) {
            return books;
        }
        try {
            String json = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
            // Each book looks like {"title":"...","author":"..."}
            String[] parts = json.split("\\},\\s*\\{");
            for (String part : parts) {
                String title = readField(part, "title");
                String author = readField(part, "author");
                if (title != null && author != null) {
                    books.add(new Book(title, author));
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read " + file);
        }
        return books;
    }

    // Writes all books to books.json
    public void save(List<Book> books) {
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < books.size(); i++) {
            Book b = books.get(i);
            sb.append("  {\"title\":\"").append(b.getTitle())
                    .append("\",\"author\":\"").append(b.getAuthor()).append("\"}");
            if (i < books.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("]\n");
        try {
            Files.write(file, sb.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("Could not save " + file);
        }
    }

    private String readField(String text, String key) {
        String marker = "\"" + key + "\":\"";
        int start = text.indexOf(marker);
        if (start < 0) {
            return null;
        }
        start += marker.length();
        int end = text.indexOf("\"", start);
        return end < 0 ? null : text.substring(start, end);
    }
}