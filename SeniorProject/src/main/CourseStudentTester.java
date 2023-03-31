
import objects.Course;
import objects.Student;

import java.util.ArrayList;
import java.util.List;

public class CourseStudentTester {
    
    public static void main(String[] args) {
        Course course = new Course("21F","CS", 6331, "P/F", "Upper level CS Class", "Bobbert");
        Course course2 = new Course("21S", "CS", 6421, "A", "Big Data", "Sherry");
        ArrayList<Course> courses = new ArrayList<Course>();
        courses.add(course);
        courses.add(course2);
        Student student = new Student("Will", "54321", "Data Science", "CS", courses, "Data Science", "2022", "2025", "Fast Track", 3.333, 4.000, 3.000, "Good Standing", true);
        System.out.println(student.toString());
    }
}
