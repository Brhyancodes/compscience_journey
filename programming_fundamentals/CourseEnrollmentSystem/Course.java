public class Course {

    // Private instance variables
    private String courseCode;
    private String courseName;
    private int maximumCapacity;

    // Static variable
    private static int totalEnrolledStudents = 0;

    // Constructor
    public Course(String courseCode, String courseName, int maximumCapacity) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.maximumCapacity = maximumCapacity;
    }

    // Getter methods
    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getMaximumCapacity() {
        return maximumCapacity;
    }

    // Static method
    public static int getTotalEnrolledStudents() {
        return totalEnrolledStudents;
    }

    // Increment enrollment
    public static void incrementEnrollment() {
        totalEnrolledStudents++;
    }
}