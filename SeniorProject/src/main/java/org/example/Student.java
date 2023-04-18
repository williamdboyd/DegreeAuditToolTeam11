package org.example;
import java.util.ArrayList;
import java.util.List;

public class Student {
    // Attributes
    String name;
    Integer id;
    String program;
    String major;
    List<Course> courses;
    List<Course> coreCourses;
    List<Course> electiveCourses;
    String specialization;
    String admittedDate;
    String anticipatedGraduation;
    String fastTrackorThesis;
    double cumulativeGPA;
    double coreGPA;
    double electiveGPA;
    String academicStanding;
    boolean graduationStatus;

    //Default Constructor
    public Student() {
        name = "";
        id = 0;
        program = "";
        major = "";
        courses = new ArrayList<Course>();
        specialization = "";
        admittedDate = "";
        anticipatedGraduation = "";
        fastTrackorThesis = "";
        cumulativeGPA = 0.0;
        coreGPA = 0.0;
        academicStanding = "";
        graduationStatus = false;
    }

    //Constructor for Student with full information
    public Student(String name, Integer id, String program, String major, List<Course> courses, String specialization, String admittedDate, String anticipatedGraduation, String fastTrackorThesis, double cumulativeGPA, double coreGPA, double electiveGPA, String academicStanding, boolean graduationStatus) {
        this.name = name;
        this.id = id;
        this.program = program;
        this.major = major;
        this.courses = courses;
        this.specialization = specialization;
        this.admittedDate = admittedDate;
        this.anticipatedGraduation = anticipatedGraduation;
        this.fastTrackorThesis = fastTrackorThesis;
        this.cumulativeGPA = cumulativeGPA;
        this.coreGPA = coreGPA;
        this.electiveGPA = electiveGPA;
        this.academicStanding = academicStanding;
        this.graduationStatus = graduationStatus;
    }


    //toString method
    public String toString() {
        System.out.println("Student Name: " + name);
        System.out.println("Student ID: " + id);
        System.out.println("Program: " + program);
        System.out.println("Major: " + major);
        for (Course course : courses) {
            System.out.println(course.toString());
        }
        System.out.println("Specialization: " + specialization);
        System.out.println("Admitted Date: " + admittedDate);
        System.out.println("Anticipated Graduation: " + anticipatedGraduation);
        System.out.println("Fast Track or Thesis: " + fastTrackorThesis);
        System.out.println("Cumulative GPA: " + cumulativeGPA);
        System.out.println("Core GPA: " + coreGPA);
        System.out.println("Academic Standing: " + academicStanding);
        System.out.println("Graduation Status: " + graduationStatus);
        return "";
    }



    public void checkCoreCourses() {
        //Takes the list of courses a student has taken and compares them to the list of core courses
    }

    public void checkElectiveCourses() {
        //Takes the list of courses a student has taken and compares them to the list of electrive courses
    }

    public void calculateCoreGPA() {
        //Takes the list of core courses a student has taken and calculates their core GPA
    }

    public void calculateElectiveGPA() {
        //Takes the list of elective courses a student has taken and calculates their elective GPA
    }




    // Getters/Setters for the variables
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getID() {
        return id;
    }
    public void setID(Integer id) {
        this.id = id;
    }

    public String getProgram() {
        return program;
    }
    public void setProgram(String program) {
        this.program = program;
    }

    public String getMajor() {
        return major;
    }
    public void setMajor(String major) {
        this.major = major;
    }

    public List<Course> getCourses() {
        return courses;
    }
    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public void addCourse(Course c){
        this.courses.add(c);
    }

    public List<Course> getCoreCourses() {
        return coreCourses;
    }

    public List<Course> getElectiveCourses() {
        return electiveCourses;
    }

    public String getSpecialization() {
        return specialization;
    }
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getAdmittedDate() {
        return admittedDate;
    }
    public void setAdmittedDate(String admittedDate) {
        this.admittedDate = admittedDate;
    }

    public String getAnticipatedGraduation() {
        return anticipatedGraduation;
    }
    public void setAnticipatedGraduation(String anticipatedGraduation) {
        this.anticipatedGraduation = anticipatedGraduation;
    }

    public String getFastTrackorThesis() {
        return fastTrackorThesis;
    }
    public void setFastTrackorThesis(String fastTrackorThesis) {
        this.fastTrackorThesis = fastTrackorThesis;
    }

    public double getCumulativeGPA() {
        return cumulativeGPA;
    }
    public void setCumulativeGPA(double cumulativeGPA) {
        this.cumulativeGPA = cumulativeGPA;
    }

    public double getCoreGPA() {
        return coreGPA;
    }
    public void setCoreGPA(double coreGPA) {
        this.coreGPA = coreGPA;
    }

    public double getElectiveGPA() {
        return electiveGPA;
    }
    public void setElectiveGPA(double electiveGPA) {
        this.electiveGPA = electiveGPA;
    }
    public String getAcademicStanding() {
        return academicStanding;
    }
    public void setAcademicStanding(String academicStanding) {
        this.academicStanding = academicStanding;
    }

    public boolean isGraduationStatus() {
        return graduationStatus;
    }
    public void setGraduationStatus(boolean graduationStatus) {
        this.graduationStatus = graduationStatus;
    }

    

}