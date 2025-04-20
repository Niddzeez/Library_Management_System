import java.util.*;
import java.time.*;

/*
 ● Design a Library Management System where users (students and librarians) can borrow,
return, and manage books.
● Book class should have title, author, bookId, isAvailable, number of copies data
members.
● Store book details and user details dynamically.
● Perform all the required operations to successfully operate a Library.
● Student and Librarian classes should be inherited from a common User class.
● Exceptions should be handled in an efficient way. Examples include “book not available”,
“invalid user exceptions”.
● Implement all the methods that are necessary to operate a library.
*/
// 1. Book
// 2. User (abstract) - common properties for Student and Librarian
// 3. Student (inherits User)
// 4. Librarian (inherits User)
// 5. LibrarySystem - main class to manage books and users


public  abstract class User{
    private String name;
    private  int UserID;
    private String email;
    private String password;
    private List<Book> borrowedBooks;
    private UserRole role;

    public User(String name, int userID, String email, String password ){
        this.name = name;
        this.UserID = userID;
        this.password = password;
        this.email = email;
        this.borrowedBooks = new ArrayList<>();
    }

    public String getPassword(){
        return password;
    }

    public int getUserID() {
        return UserID;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public UserRole getRole() {
        return role;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBorrowedBooks(List<Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    public void setRole(UserRole role){
        this.role = role;
    }

    public void returnBook(Book book) throws Exception {

        if(!borrowedBooks.contains(book)) {
            throw new Exception("The book " + book.getTitle() + " isn't borrowed!");
        }
        else{
            book.returnCopy();
            borrowedBooks.remove(book);
            System.out.println("Book returned successfully: " + book.getTitle());
        }
    }

    public void viewProfile(){
        System.out.println("Details of User : ");
        System.out.println("User ID: " + UserID);
        System.out.println("Title : " + name);
        System.out.println("Email: " + email);
        System.out.println("Borrowed books: " + borrowedBooks);
        System.out.println("User Role: " + role);
    }

    public void addBookToBorrowed(Book book) throws Exception {
        if (book == null) {
            throw new Exception("Invalid Book. Cannot add null to borrowed list.");
        }
        borrowedBooks.add(book);
    }

    public void viewBorrowedBooks() throws Exception{
        List<Book> booklist = getBorrowedBooks();
        if(booklist == null){
            throw new Exception("You have borrowed no books!");
        }
        else {
            System.out.println("Books currently borrowed: ");
            for(Book book : booklist){
                System.out.println("- " + book.getTitle());
            }
        }
    }

}


class Student extends User{

    public Student(String name, int userID, String email, String password) {
        super(name, userID, email, password);
        setRole(UserRole.STUDENT);
    }

    public void borrowBooks(Book book) throws Exception {
        if(book.isAvailable()){
            book.borrowCopy();
            addBookToBorrowed(book);
            System.out.println("Book borrowed successfully: " + book.getTitle());
        }
        else throw new Exception("The book is not available currently. Please try again later!");;
    }


}

class Librarian extends User{
    private double salary;
    private LocalDate hiringDate;

    public Librarian(String name, int userID, String email, String password, LocalDate hiringDate) {
        super(name, userID, email, password);
        setRole(UserRole.LIBRARIAN);
        this.hiringDate = hiringDate;
    }

    public void setSalary(double amount)
    {
        this.salary = amount;
    }

    public void setHiringDate(LocalDate date){
        this.hiringDate = date;;
    }

    public double getSalary(){
        return salary;
    }

    public LocalDate getHiringDate(){
        return hiringDate;
    }

    public void addBookToLibrary(Library library, Book book, int copies) throws Exception {
        if (library.getBookMap().containsKey(book.getBookId())) {
            Book existingBook = library.getBookMap().get(book.getBookId());
            existingBook.setTotalCopies(existingBook.getTotalCopies() + copies);
            throw new Exception("Book already exists, updated copy count.");
        } else {
            book.setTotalCopies(copies);
            library.getBookMap().put(book.getBookId(), book);
        }
    }

    public void removeBookFromLibrary(Library library, Book book) throws Exception{
        if(library.getBookMap().containsKey(book.getBookId())){
            library.getBookMap().remove(book.getBookId(), book);
            System.out.println("Book "+book.getTitle()+" removed successfully");
        }
        else throw new Exception("The book doesn't exist");
    }

    public void updateBookDetails(Library library, Book book, String newTitle, String newAuthor, int newCopies) throws Exception {
        if (library.getBookMap().containsKey(book.getBookId())) {
            book.setTitle(newTitle);
            book.setAuthor(newAuthor);
            book.setTotalCopies(newCopies);
            System.out.println("Book details updated successfully: " + book.getTitle());
        } else {
            throw new Exception("Book not found in the library.");
        }
    }

    // View all users (for admin purposes)
    public void viewAllUsers(Library library) {
        System.out.println("List of all registered users: ");
        for (User user : library.getUsers().values()) {
            System.out.println("User ID: " + user.getUserID() + ", Name: " + user.getName() + ", Role: " + user.getRole());
        }
    }

    public void approveLibrarianApplications(Library library) {
        List<Librarian> pending = library.getPendingLibrarians();
        if (pending.isEmpty()) {
            System.out.println("No applications to approve.");
            return;
        }

        for (int i = 0; i < pending.size(); i++) {
            Librarian lib = pending.get(i);
            System.out.println((i + 1) + ". " + lib.getName() + " - " + lib.getEmail());
        }

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of the librarian to approve: ");
        int choice = sc.nextInt();
        sc.nextLine();  // clear newline

        if (choice >= 1 && choice <= pending.size()) {
            Librarian approved = pending.get(choice - 1);
            try {
                library.approveLibrarian(approved);
                System.out.println("Approved: " + approved.getName());
            } catch (Exception e) {
                System.out.println("Error approving librarian: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }


}