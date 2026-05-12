import java.util.ArrayList;
import java.util.HashMap;

public class Student {

    // Private instance variables
    private String name;
    private String studentId;
    private ArrayList<Course> enrolledCourses;
    private HashMap<Course, Double> grades;

    // Constructor
    public Student(String name, String studentId) {
        this.name = name;
        this.studentId = studentId;
        enrolledCourses = new ArrayList<>();
        grades = new HashMap<>();
    }

    // Getter and Setter Methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public ArrayList<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    // Enroll student in course
    public void enrollCourse(Course course) {
        enrolledCourses.add(course);
        Course.incrementEnrollment();
    }

    // Assign grade to student
    public void assignGrade(Course course, double grade) {
        grades.put(course, grade);
    }

    // Get grades
    public HashMap<Course, Double> getGrades() {
        return grades;
    }
}