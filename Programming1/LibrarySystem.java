import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Class representing a Book
class Book {

    String title;
    String author;
    int quantity;

    public Book(String title, String author, int quantity) {
        this.title = title;
        this.author = author;
        this.quantity = quantity;
    }
}

// Main Library System class
public class LibrarySystem {

    // Store books using HashMap
    static Map<String, Book> library = new HashMap<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        int choice = 0;   // FIX: initialize choice

        do {
            System.out.println("\n===== LIBRARY MENU =====");
            System.out.println("1. Add Books");
            System.out.println("2. Borrow Books");
            System.out.println("3. Return Books");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
                continue;
            }

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    addBook();
                    break;

                case 2:
                    borrowBook();
                    break;

                case 3:
                    returnBook();
                    break;

                case 4:
                    System.out.println("Exiting program. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Please select from the menu.");
            }

        } while (choice != 4);
    }

    // Method to add books
    public static void addBook() {

        System.out.print("Enter book title: ");
        String title = scanner.nextLine();

        System.out.print("Enter author name: ");
        String author = scanner.nextLine();

        System.out.print("Enter quantity: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Invalid quantity.");
            scanner.next();
            return;
        }

        int quantity = scanner.nextInt();
        scanner.nextLine();

        if (library.containsKey(title)) {

            Book existingBook = library.get(title);
            existingBook.quantity += quantity;

            System.out.println("Book already exists. Quantity updated to: "
                    + existingBook.quantity);

        } else {

            Book newBook = new Book(title, author, quantity);
            library.put(title, newBook);

            System.out.println("Book added successfully.");
        }
    }

    // Method to borrow books
    public static void borrowBook() {

        System.out.print("Enter book title: ");
        String title = scanner.nextLine();

        if (library.containsKey(title)) {

            Book book = library.get(title);

            System.out.print("Enter quantity to borrow: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid number.");
                scanner.next();
                return;
            }

            int quantity = scanner.nextInt();
            scanner.nextLine();

            if (quantity <= 0) {
                System.out.println("Quantity must be greater than zero.");
            } 
            else if (book.quantity >= quantity) {

                book.quantity -= quantity;

                System.out.println("Book borrowed successfully.");
                System.out.println("Remaining copies: " + book.quantity);

            } 
            else {

                System.out.println("Error: Not enough copies available.");
            }

        } 
        else {

            System.out.println("Error: Book not found in the library.");
        }
    }

    // Method to return books
    public static void returnBook() {

        System.out.print("Enter book title: ");
        String title = scanner.nextLine();

        if (library.containsKey(title)) {

            Book book = library.get(title);

            System.out.print("Enter quantity to return: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid quantity.");
                scanner.next();
                return;
            }

            int quantity = scanner.nextInt();
            scanner.nextLine();

            if (quantity <= 0) {
                System.out.println("Invalid quantity.");
            } 
            else {

                book.quantity += quantity;

                System.out.println("Book returned successfully.");
                System.out.println("Updated copies: " + book.quantity);
            }

        } 
        else {

            System.out.println("Error: This book does not belong to the library.");
        }
    }
}