import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        Student student1 = new Student("John Doe", "S101");

        while (true) {

            System.out.println("\n===== Course Management System =====");

            System.out.println("1. Add Course");
            System.out.println("2. Enroll Student");
            System.out.println("3. Assign Grade");
            System.out.println("4. Calculate Overall Grade");
            System.out.println("5. Display Total Enrollment");
            System.out.println("6. Exit");

            System.out.print("Enter choice: ");

            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {

                case 1:

                    System.out.print("Enter course code: ");
                    String code = input.nextLine();

                    System.out.print("Enter course name: ");
                    String name = input.nextLine();

                    System.out.print("Enter maximum capacity: ");
                    int capacity = input.nextInt();

                    CourseManagement.addCourse(code, name, capacity);

                    break;

                case 2:

                    if (CourseManagement.getCourses().isEmpty()) {

                        System.out.println("No courses available.");

                        break;
                    }

                    Course course =
                            CourseManagement.getCourses().get(0);

                    CourseManagement.enrollStudent(student1, course);

                    break;

                case 3:

                    if (CourseManagement.getCourses().isEmpty()) {

                        System.out.println("No courses available.");

                        break;
                    }

                    course = CourseManagement.getCourses().get(0);

                    System.out.print("Enter grade: ");

                    double grade = input.nextDouble();

                    CourseManagement.assignGrade(student1, course, grade);

                    break;

                case 4:

                    double overall =
                            CourseManagement.calculateOverallGrade(student1);

                    System.out.println("Overall Grade: " + overall);

                    break;

                case 5:

                    System.out.println("Total Enrolled Students: "
                            + Course.getTotalEnrolledStudents());

                    break;

                case 6:

                    System.out.println("Exiting program...");

                    System.exit(0);

                default:

                    System.out.println("Invalid choice.");
            }
        }
    }
}