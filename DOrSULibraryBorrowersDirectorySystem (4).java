import java.util.Scanner;
import java.util.regex.Pattern;

public class DOrSULibraryBorrowersDirectorySystem {

    // ====== Configuration / Limits ======
    private static final int MAX_BORROWERS = 100;

    // ====== Data Storage (arrays and variables) ======
    private static String[] names = new String[MAX_BORROWERS];
    private static String[] ids = new String[MAX_BORROWERS];
    private static String[] courses = new String[MAX_BORROWERS];
    private static String[] contactNumbers = new String[MAX_BORROWERS];
    private static int[] borrowedBooks = new int[MAX_BORROWERS];

    private static int borrowerCount = 0;

    private static Scanner scanner = new Scanner(System.in);

    // ====== MAIN ======
    public static void main(String[] args) {

        // Pre-load one borrower for testing search/edit (e.g., test #3, #5)
        preloadSampleBorrowers();

        System.out.println("===== DOrSU Library Borrower’s Directory System =====");

        int choice;
        do {
            choice = getMenuChoice();
            switch (choice) {
                case 1:
                    addBorrower();
                    break;
                case 2:
                    searchBorrower();
                    break;
                case 3:
                    editBorrower();
                    break;
                case 4:
                    displayAllBorrowers();
                    break;
                case 5:
                    computeAverageBorrowedBooks();
                    break;
                case 6:
                    System.out.println("Exiting program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            System.out.println();
        } while (choice != 6);
    }

    // Preload some borrowers to support the given test cases
    private static void preloadSampleBorrowers() {
        // Juan Dela Cruz: needed for test #3 and #5
        names[0] = "Juan Dela Cruz";
        ids[0] = "2023-00001";
        courses[0] = "BSIT";
        contactNumbers[0] = "09123456780";
        borrowedBooks[0] = 2;
        borrowerCount = 1;

        // Add a couple more borrowers for test #6 (average)
        names[1] = "Ana Lopez";
        ids[1] = "2023-00002";
        courses[1] = "BSCS";
        contactNumbers[1] = "09123456781";
        borrowedBooks[1] = 4;

        names[2] = "Mark Reyes";
        ids[2] = "2023-00003";
        courses[2] = "BSIT";
        contactNumbers[2] = "09123456782";
        borrowedBooks[2] = 3;

        borrowerCount = 3;
    }

    // ====== MENU ======
    public static int getMenuChoice() {
        System.out.println("--------------------------------------------------");
        System.out.println("1. Add borrower");
        System.out.println("2. Search borrower by name or ID");
        System.out.println("3. Edit borrower information");
        System.out.println("4. Display all borrowers");
        System.out.println("5. Compute average number of borrowed books");
        System.out.println("6. Exit program");
        System.out.print("Enter your choice (1-6): ");

        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Enter a number (1-6): ");
            scanner.next(); // discard invalid
        }
        return scanner.nextInt();
    }

    // ====== CORE FEATURES ======

    // 1. Add Borrower
    public static void addBorrower() {
        if (borrowerCount >= MAX_BORROWERS) {
            System.out.println("Borrower list is full. Cannot add more.");
            return;
        }
        scanner.nextLine(); // consume leftover newline

        String name;
        do {
            System.out.print("Enter name: ");
            name = scanner.nextLine().trim();
        } while (!validateName(name));

        String id;
        do {
            System.out.print("Enter ID (format: YYYY-#####): ");
            id = scanner.nextLine().trim();
        } while (!validateID(id));

        String course;
        do {
            System.out.print("Enter department/course (e.g., BSIT): ");
            course = scanner.nextLine().trim();
        } while (!validateDepartmentOrCourse(course));

        String contact;
        do {
            System.out.print("Enter contact number (11 digits, starts with 09): ");
            contact = scanner.nextLine().trim();
        } while (!validateContactNumber(contact));

        int books;
        while (true) {
            System.out.print("Enter number of borrowed books (0 or more): ");
            if (scanner.hasNextInt()) {
                books = scanner.nextInt();
                if (books >= 0) break;
            } else {
                scanner.next(); // discard invalid
            }
            System.out.println("Invalid number. Try again.");
        }

        // Store the new borrower
        names[borrowerCount] = name;
        ids[borrowerCount] = id;
        courses[borrowerCount] = course;
        contactNumbers[borrowerCount] = contact;
        borrowedBooks[borrowerCount] = books;
        borrowerCount++;

        System.out.println("Borrower successfully added.");
    }

    // 2. Search Borrower 
    public static void searchBorrower() {
        if (borrowerCount == 0) {
            System.out.println("No borrowers in the list.");
            return;
        }
        scanner.nextLine(); // consume newline

        System.out.print("Search by (1) Name or (2) ID? Enter 1 or 2: ");
        int type;
        while (true) {
            if (scanner.hasNextInt()) {
                type = scanner.nextInt();
                if (type == 1 || type == 2) break;
            } else {
                scanner.next();
            }
            System.out.print("Invalid choice. Enter 1 for Name or 2 for ID: ");
        }
        scanner.nextLine(); // consume newline

        int index = -1;
        if (type == 1) {
            System.out.print("Enter name (or part of name): ");
            String key = scanner.nextLine().trim().toLowerCase();
            for (int i = 0; i < borrowerCount; i++) {
                if (names[i].toLowerCase().contains(key)) {
                    index = i;
                    break;
                }
            }
        } else {
            System.out.print("Enter ID: ");
            String key = scanner.nextLine().trim();
            for (int i = 0; i < borrowerCount; i++) {
                if (ids[i].equalsIgnoreCase(key)) {
                    index = i;
                    break;
                }
            }
        }

        if (index == -1) {
            System.out.println("Borrower not found.");
        } else {
            System.out.println("Borrower found:");
            printBorrower(index);
        }
    }

    // 3. Edit Borrower
    public static void editBorrower() {
        if (borrowerCount == 0) {
            System.out.println("No borrowers in the list.");
            return;
        }
        scanner.nextLine(); // consume newline

        System.out.print("Enter ID of borrower to edit: ");
        String key = scanner.nextLine().trim();

        int index = -1;
        for (int i = 0; i < borrowerCount; i++) {
            if (ids[i].equalsIgnoreCase(key)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            System.out.println("Borrower not found.");
            return;
        }

        System.out.println("Current information:");
        printBorrower(index);

        System.out.println("What would you like to edit?");
        System.out.println("1. Name");
        System.out.println("2. Department/Course");
        System.out.println("3. Contact number");
        System.out.println("4. Number of borrowed books");
        System.out.print("Enter your choice (1-4): ");

        int choice;
        while (true) {
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice >= 1 && choice <= 4) break;
            } else {
                scanner.next();
            }
            System.out.print("Invalid choice. Enter 1-4: ");
        }
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                String name;
                do {
                    System.out.print("Enter new name: ");
                    name = scanner.nextLine().trim();
                } while (!validateName(name));
                names[index] = name;
                System.out.println("Name updated.");
                break;
            case 2:
                String course;
                do {
                    System.out.print("Enter new department/course: ");
                    course = scanner.nextLine().trim();
                } while (!validateDepartmentOrCourse(course));
                courses[index] = course;
                System.out.println("Department/Course updated.");
                break;
            case 3:
                String contact;
                do {
                    System.out.print("Enter new contact number: ");
                    contact = scanner.nextLine().trim();
                } while (!validateContactNumber(contact));
                contactNumbers[index] = contact;
                System.out.println("Contact number updated.");
                break;
            case 4:
                int books;
                while (true) {
                    System.out.print("Enter new number of borrowed books: ");
                    if (scanner.hasNextInt()) {
                        books = scanner.nextInt();
                        if (books >= 0) break;
                    } else {
                        scanner.next();
                    }
                    System.out.println("Invalid number. Try again.");
                }
                borrowedBooks[index] = books;
                System.out.println("Borrow count updated.");
                break;
            default:
                System.out.println("No changes made.");
        }
    }

    // 4. Display All Borrowers
    public static void displayAllBorrowers() {
        if (borrowerCount == 0) {
            System.out.println("No borrowers in the list.");
            return;
        }
        System.out.println("----------------------------------------------------------");
        System.out.printf("%-4s | %-20s | %-11s | %-8s | %-13s | %-5s%n", "No.", "Name", "ID", "Course", "Contact No.", "Books");
        System.out.println("----------------------------------------------------------");
        for (int i = 0; i < borrowerCount; i++) {
            System.out.printf("%-4d | %-20s | %-11s | %-8s | %-13s | %-5d%n",
                    (i + 1),
                    names[i],
                    ids[i],
                    courses[i],
                    contactNumbers[i],
                    borrowedBooks[i]);
        }
        System.out.println("----------------------------------------------------------");
    }

    // 5. Compute Average Number of Borrowed Books
    public static void computeAverageBorrowedBooks() {
        if (borrowerCount == 0) {
            System.out.println("No borrowers in the list.");
            return;
        }
        int sum = 0;
        for (int i = 0; i < borrowerCount; i++) {
            sum += borrowedBooks[i];
        }
        double average = (double) sum / borrowerCount;
        System.out.printf("Average number of borrowed books: %.0f%n", average);
    }

    // ====== VALIDATION METHODS ======

    public static boolean validateName(String name) {
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return false;
        }
        // Letters, spaces, apostrophes, and periods
        if (!Pattern.matches("[A-Za-z .']+", name)) {
            System.out.println("Invalid name format. Use letters and spaces only.");
            return false;
        }
        return true;
    }

    // Example format: 2023-04567  (4 digits, a dash, 5 digits)
    public static boolean validateID(String id) {
        if (!Pattern.matches("\\d{4}-\\d{5}", id)) {
            System.out.println("Invalid ID format. Correct format: YYYY-##### (e.g., 2023-04567).");
            return false;
        }
        return true;
    }

    public static boolean validateDepartmentOrCourse(String course) {
        if (course.isEmpty()) {
            System.out.println("Department/Course cannot be empty.");
            return false;
        }
        // Simple pattern: letters only (no numbers), 2–10 characters
        if (!Pattern.matches("[A-Za-z]{2,10}", course)) {
            System.out.println("Invalid department/course. Use letters only, e.g., BSIT, BSED.");
            return false;
        }
        return true;
    }

    // Example: Philippine mobile numbers, 11 digits starting with 09
    public static boolean validateContactNumber(String contact) {
        if (!Pattern.matches("09\\d{9}", contact)) {
            System.out.println("Invalid contact number. Use 11 digits starting with 09 (e.g., 09123456789).");
            return false;
        }
        return true;
    }

    // ====== Helper to print a borrower ======
    private static void printBorrower(int index) {
        System.out.println("Name: " + names[index]);
        System.out.println("ID: " + ids[index]);
        System.out.println("Course: " + courses[index]);
        System.out.println("Contact: " + contactNumbers[index]);
        System.out.println("Borrowed books: " + borrowedBooks[index]);
    }
}