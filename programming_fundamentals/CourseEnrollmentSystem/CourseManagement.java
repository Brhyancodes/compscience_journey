import java.util.ArrayList;

public class CourseManagement {

    // Private static variable
    private static ArrayList<Course> courses = new ArrayList<>();

    // Add course
    public static void addCourse(String code, String name, int capacity) {

        Course course = new Course(code, name, capacity);

        courses.add(course);

        System.out.println("Course added successfully.");
    }

    // Enroll student
    public static void enrollStudent(Student student, Course course) {

        if (student.getEnrolledCourses().size() < course.getMaximumCapacity()) {

            student.enrollCourse(course);

            System.out.println("Student enrolled successfully.");

        } else {

            System.out.println("Course has reached maximum capacity.");
        }
    }

    // Assign grade
    public static void assignGrade(Student student, Course course, double grade) {

        student.assignGrade(course, grade);

        System.out.println("Grade assigned successfully.");
    }

    // Calculate overall grade
    public static double calculateOverallGrade(Student student) {

        double total = 0;
        int count = 0;

        for (double grade : student.getGrades().values()) {

            total += grade;
            count++;
        }

        if (count == 0) {
            return 0;
        }

        return total / count;
    }

    // Get courses
    public static ArrayList<Course> getCourses() {
        return courses;
    }
}