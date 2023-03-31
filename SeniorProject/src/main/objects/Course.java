package objects;

public class Course {
    String semesterTaken;
    String prefix;
    int number;
    String grade;
    String description;
    String instructor;

    //Default Constructor
    public Course() {
        semesterTaken = "";
        prefix = "";
        number = 0;
        grade = "";
        description = "";
        instructor = "";
    }

    //Constructor for Course with full information
    public Course(String semesterTaken, String prefix, int number, String grade, String description, String instructor) {
        this.semesterTaken = semesterTaken;
        this.prefix = prefix;
        this.number = number;
        this.grade = grade;
        this.description = description;
        this.instructor = instructor;
    }



    //toString method
    public String toString() {
        return "Course: [semesterTaken=" + semesterTaken + ", prefix=" + prefix + ", number=" + Integer.toString(number) + ", grade=" + grade
                + ", description=" + description + ", instructor=" + instructor + "]";
    }




    // Access/Setters for the variables
    public String getSemesterTaken() {
        return semesterTaken;
    }
    public void setSemesterTaken(String semesterTaken) {
        this.semesterTaken = semesterTaken;
    }
    public String getPrefix() {
        return prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public String getGrade() {
        return grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getInstructor() {
        return instructor;
    }
    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
}
