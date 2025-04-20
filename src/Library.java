import java.io.*;
import java.util.*;


public class Library {
    private final Map<Integer, Book> books; // bookId -> Book
    private final Map<Integer, User> users; // userId -> User
    private final List<Librarian> pendingLibrarians = new ArrayList<>();

    static final String BOOKS_FILE = "src/data/books.csv";
    static final String STUDENTS_FILE = "src/data/students.csv";

    public Map<Integer, Student> getStudentMap() {
        Map<Integer, Student> studentMap = new HashMap<>();
        for (User user : users.values()) {
            if (user instanceof Student) {
                Student student = (Student) user; // Cast the user to a Student
                studentMap.put(student.getUserID(), student); // Assuming 'getUserID()' returns a unique ID for each student
            }
        }
        return studentMap;
    }
    public static void loadBooksFromCSV(Library library, String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0].trim());
                String title = parts[1].trim();
                String author = parts[2].trim();
                int copies = Integer.parseInt(parts[3].trim());

                Book book = new Book(title, author, id, copies);
                library.addBook(book);  // Use your method that directly adds a book
            }
            System.out.println("Books loaded from " + filename);
        } catch (IOException e) {
            System.out.println("Failed to load books: " + e.getMessage());
        }
    }

    public static void loadStudentsFromCSV(Library library, String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                String email = parts[2].trim();
                String password = parts[3].trim();

                Student student = new Student(name, id, email, password);
                library.registerUser(student);
            }
            System.out.println("Students loaded from " + filename);
        } catch (Exception e) {
            System.out.println("Failed to load students: " + e.getMessage());
        }
    }

    // Method to save books to CSV
    public static void saveBooksToCSV(Library library, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write("ID,Title,Author,Copies\n"); // Writing header
            for (Book book : library.getBookMap().values()) {
                bw.write(book.getBookId() + "," + book.getTitle() + "," + book.getAuthor() + "," + book.getAvailableCopies() + "\n");
            }
            System.out.println("Books saved to " + filename);
        } catch (IOException e) {
            System.out.println("Failed to save books: " + e.getMessage());
        }
    }


    // Method to save students to CSV
    public static void saveStudentsToCSV(Library library, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write("ID,Name,Email,Password\n"); // Writing header
            for (Student student : library.getStudentMap().values()) {
                bw.write(student.getUserID() + "," + student.getName() + "," + student.getEmail() + "," + student.getPassword() + "\n");
            }
            System.out.println("Students saved to " + filename);
        } catch (IOException e) {
            System.out.println("Failed to save students: " + e.getMessage());
        }
    }

    public Library() {
        this.books = new HashMap<>();
        this.users = new HashMap<>();
    }

    // Add a new book to the library
    public void addBook(Book book) {
        books.put(book.getBookId(), book);
    }

    // Remove a book from the library
    public void removeBook(Integer bookId) throws Exception {
        if (!books.containsKey(bookId)) {
            throw new Exception("Book with ID " + bookId + " not found.");
        }
        books.remove(bookId);
    }

    // Register a new user
    public void registerUser(User user) throws Exception {
        if (users.containsKey(user.getUserID())){
            throw new Exception("User already exists with ID: " + user.getUserID());
        }
        users.put(user.getUserID(), user);
    }

    public void addLibrarianApplication(Librarian librarian) throws Exception {
        if (librarian == null) throw new IllegalArgumentException("Librarian cannot be null.");
        for (User u : users.values()) {
            if (u.getEmail().equals(librarian.getEmail())) {
                throw new Exception("Email already registered.");
            }
        }
        pendingLibrarians.add(librarian);
    }

    public List<Librarian> getPendingLibrarians() {
        return pendingLibrarians;
    }

    public void approveLibrarian(Librarian librarian) throws Exception {
        registerUser(librarian);
        pendingLibrarians.remove(librarian);
    }

    // Find book by ID
    public Book getBookById(Integer bookId) throws Exception {
        if (!books.containsKey(bookId)) {
            throw new Exception("Book with ID " + bookId + " not found.");
        }
        return books.get(bookId);
    }

    // Allow a student to borrow a book
    public void borrowBook(Integer userId, Integer bookId) throws Exception {
        if (!users.containsKey(userId)) throw new Exception("Invalid User ID.");
        if (!books.containsKey(bookId)) throw new Exception("Book not found.");

        User user = users.get(userId);
        if (!(user instanceof Student)) throw new Exception("Only students can borrow books.");

        Book book = books.get(bookId);
        if (!book.isAvailable()) throw new Exception("Book is not available.");

        ((Student) user).borrowBooks(book);  // Your Student class should handle this
        book.setAvailableCopies(book.getAvailableCopies() - 1);
    }

    // Allow a student to return a book
    public void returnBook(Integer userId, Integer bookId) throws Exception {
        if (!users.containsKey(userId)) throw new Exception("Invalid User ID.");
        if (!books.containsKey(bookId)) throw new Exception("Book not found.");

        User user = users.get(userId);
        if (!(user instanceof Student)) throw new Exception("Only students can return books.");

        Book book = books.get(bookId);
        ((Student) user).returnBook(book); // Your Student class should handle this
        book.setAvailableCopies(book.getAvailableCopies() + 1);
    }

    // Search books by title
    public List<Book> searchBooksByTitle(String title) {
        List<Book> result = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    // Search books by author
    public List<Book> searchBooksByAuthor(String author) {
        List<Book> result = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    // List all books
    public void listAllBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in library.");
        } else {
            for (Book book : books.values()) {
                System.out.println(book);
            }
        }
    }

    // List all users
    public void listAllUsers() {
        if (users.isEmpty()) {
            System.out.println("No registered users.");
        } else {
            for (User user : users.values()) {
                System.out.println(user);
            }
        }
    }

    public Map<Integer, Book> getBookMap() {
        return books;
    }

    public Map<Integer, User> getUsers() {
        return users;
    }


    // Get a user by userID
    public User getUserByID(int userID) {
        return users.get(userID);
    }

    public User login(String email, String password) throws Exception {
        for (User user : users.values()) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                System.out.println("Login successful! Welcome " + user.getName());
                return user;
            }
        }
        throw new Exception("Invalid email or password.");
    }

    public void viewAllBooks() throws Exception {
        if (books.isEmpty()) {
            throw new Exception("No books available in the library.");
        } else {
            System.out.println("List of all books in the library:");
            for (Book book : books.values()) {
                book.displayDetails();
            }
        }
    }



}
