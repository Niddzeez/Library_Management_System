public class Book{
    private String title;
    private String author;
    private int bookId;
    private int totalCopies;
    private int availableCopies;

    public Book(String title, String author, int bookid, int totalCopies){
        this.title = title;
        this.author = author;
        this.bookId = bookid;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    public String getTitle(){
        return title;
    }

    public String getAuthor(){
        return  author;
    }

    public int getBookId(){
        return bookId;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public boolean isAvailable(){
        return availableCopies > 0;
    }

    @Override
    public String toString() {
        return "ID: " + bookId + ", Title: " + title + ", Author: " + author + ", Copies: " + availableCopies;
    }

    public void borrowCopy() throws Exception{
        if(availableCopies<=0){
            throw new Exception("No copies available at the moment! Try again later!");
        }
        else{
            availableCopies--;
        }
    }

    public void returnCopy(){
        if(availableCopies<totalCopies) availableCopies++;
    }

    public void displayDetails(){
        System.out.println("Book ID: " + bookId);
        System.out.println("Title: " + title);
        System.out.println("Author: " + author);
        System.out.println("Available Copies: " + availableCopies + "/" + totalCopies +"\n\n");
    }


}