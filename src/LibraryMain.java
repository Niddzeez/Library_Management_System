import java.time.LocalDate;
import java.util.*;

public class LibraryMain {

    public static void main(String[] args) throws Exception {
        Library library = new Library();
        Scanner sc = new Scanner(System.in);


        Library.loadBooksFromCSV(library, Library.BOOKS_FILE);
        Library.loadStudentsFromCSV(library, Library.STUDENTS_FILE);

        Librarian adminLibrarian = new Librarian("Admin", 999, "admin@library.com", "admin123", LocalDate.of(2025, 4, 26));
        library.registerUser(adminLibrarian);



        while (true) {
            System.out.println("\n===== Library Management System =====");
            System.out.println("1. Register as Student");
            System.out.println("2. Register as Librarian (Application)");
            System.out.println("3. Login as Student");
            System.out.println("4. Login as Librarian");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = getIntInput(sc);
            switch (choice) {
                case 1 -> registerStudent(sc, library);
                case 2 -> applyAsLibrarian(sc, library);
                case 3 -> studentLogin(sc, library);
                case 4 -> librarianLogin(sc, library);
                case 5 -> {
                    System.out.println("Exiting... Goodbye!");
                    Library.saveBooksToCSV(library, Library.BOOKS_FILE);
                    Library.saveStudentsToCSV(library, Library.STUDENTS_FILE);
                    sc.close();

                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void registerStudent(Scanner sc, Library library) {
        try {
            System.out.print("Enter name: ");
            String name = sc.nextLine();
            System.out.print("Enter ID: ");
            int id = getIntInput(sc);
            System.out.print("Enter email: ");
            String email = sc.nextLine();
            System.out.print("Enter password: ");
            String pwd = sc.nextLine();

            Student student = new Student(name, id, email, pwd);
            library.registerUser(student);
            System.out.println("Student registered successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void applyAsLibrarian(Scanner sc, Library library) {
        try {
            System.out.print("Enter name: ");
            String name = sc.nextLine();
            System.out.print("Enter ID: ");
            int id = getIntInput(sc);
            System.out.print("Enter email: ");
            String email = sc.nextLine();
            System.out.print("Enter password: ");
            String pwd = sc.nextLine();

            Librarian librarian = new Librarian(name, id, email, pwd, LocalDate.now());
            library.addLibrarianApplication(librarian);
            System.out.println("Librarian application submitted. Await admin approval.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void studentLogin(Scanner sc, Library library) {
        try {
            System.out.print("Enter email: ");
            String email = sc.nextLine();
            System.out.print("Enter password: ");
            String password = sc.nextLine();

            User user = library.login(email, password);
            if (!(user instanceof Student student)) {
                System.out.println("Not a student account.");
                return;
            }
            while (true) {
                System.out.println("\n-- Student Menu --");
                System.out.println("1. Borrow Book");
                System.out.println("2. Return Book");
                System.out.println("3. View Borrowed Books");
                System.out.println("4. Search Book by Title");
                System.out.println("5. Search Book by Author");
                System.out.println("6. Logout");
                System.out.print("Choice: ");

                int option = getIntInput(sc);
                switch (option) {
                    case 1 -> {
                        System.out.print("Enter Book ID: ");
                        int bid = getIntInput(sc);
                        Book book = library.getBookById(bid);
                        if (book == null) System.out.println("Book not found.");
                        else if (!book.isAvailable()) System.out.println("No copies available.");
                        else {
                            book.borrowCopy();
                            student.addBookToBorrowed(book);
                            System.out.println("Borrowed: " + book.getTitle());
                        }
                    }
                    case 2 -> {
                        System.out.print("Enter Book ID: ");
                        int bid = getIntInput(sc);
                        Book book = library.getBookById(bid);
                        if (book == null) System.out.println("Book not found.");
                        else student.returnBook(book);
                    }
                    case 3 -> student.getBorrowedBooks().forEach(System.out::println);
                    case 4 -> {
                        System.out.print("Enter title: ");
                        library.searchBooksByTitle(sc.nextLine()).forEach(System.out::println);
                    }
                    case 5 -> {
                        System.out.print("Enter author: ");
                        library.searchBooksByAuthor(sc.nextLine()).forEach(System.out::println);
                    }
                    case 6 -> {
                        System.out.println("Logging out.");
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            }
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private static void librarianLogin(Scanner sc, Library library) {
        try {
            System.out.print("Enter email: ");
            String email = sc.nextLine();
            System.out.print("Enter password: ");
            String password = sc.nextLine();

            User user = library.login(email, password);
            if (!(user instanceof Librarian librarian)) {
                System.out.println("Not a librarian account.");
                return;
            }

            while (true) {
                System.out.println("\n-- Librarian Menu --");
                System.out.println("1. Add Book");
                System.out.println("2. View All Books");
                System.out.println("3. Approve Librarian Applications");
                System.out.println("4. Logout");
                System.out.print("Choice: ");

                int option = getIntInput(sc);
                switch (option) {
                    case 1 -> {
                        System.out.print("Book ID: ");
                        int id = getIntInput(sc);
                        System.out.print("Title: ");
                        String title = sc.nextLine();
                        System.out.print("Author: ");
                        String author = sc.nextLine();
                        System.out.print("Copies: ");
                        int copies = getIntInput(sc);

                        Book book = new Book(title, author, id, copies);
                        librarian.addBookToLibrary(library, book, copies);
                        System.out.println("Book added.");
                    }
                    case 2 -> library.viewAllBooks();
                    case 3 -> librarian.approveLibrarianApplications(library);
                    case 4 -> {
                        System.out.println("Logging out.");
                        return;
                    }
                    default -> System.out.println("Invalid option.");
                }
            }
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private static int getIntInput(Scanner sc) {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Try again: ");
            }
        }
    }
}