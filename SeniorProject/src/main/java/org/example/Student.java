package org.example;
import java.util.ArrayList;
import java.util.List;

public class Student {
    // Attributes
    String name;
    Integer id;
    String program;
    DegreePlan degreePlan;
    String major;
    List<Course> courses;
    List<Course> coreCourses;
    List<Course> electiveCourses;
    List<Course> preReqCourses;
    String specialization;
    String admittedDate;
    String anticipatedGraduation;
    Boolean fastTrack;
    Boolean thesis;
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
        degreePlan = new DegreePlan();
        major = "";
        courses = new ArrayList<Course>();
        coreCourses = new ArrayList<Course>();
        electiveCourses = new ArrayList<Course>();
        preReqCourses = new ArrayList<Course>();
        specialization = "";
        admittedDate = "";
        anticipatedGraduation = "";
        fastTrack = false;
        thesis = false;
        cumulativeGPA = 0.0;
        coreGPA = 0.0;
        electiveGPA = 0.0;
        academicStanding = "";
        graduationStatus = false;
    }

    //Constructor for Student with full information
    public Student(String name, Integer id, String program, String major, List<Course> courses, String specialization, String admittedDate, String anticipatedGraduation, Boolean fastTrack, Boolean thesis, double cumulativeGPA, double coreGPA, double electiveGPA, String academicStanding, boolean graduationStatus) {
        this.name = name;
        this.id = id;
        this.program = program;
        this.major = major;
        this.courses = courses;
        this.specialization = specialization;
        this.admittedDate = admittedDate;
        this.anticipatedGraduation = anticipatedGraduation;
        this.fastTrack = fastTrack;
        this.thesis = thesis;
        this.cumulativeGPA = cumulativeGPA;
        this.coreGPA = coreGPA;
        this.electiveGPA = electiveGPA;
        this.academicStanding = academicStanding;
        this.graduationStatus = graduationStatus;
    }


    //toString method
    public String toString() {
        String student = "Student Name: " + name + "\nStudent ID: " + id + "\nProgram: " + program + "\nMajor: " + major + "\nSpecialization: " + specialization + "\nAdmitted Date: " + admittedDate + "\nAnticipated Graduation: " + anticipatedGraduation + "\nFast Track: " + fastTrack + "\nThesis: " + thesis + "\nCumulative GPA: " + cumulativeGPA + "\nCore GPA: " + coreGPA + "\nElective GPA: " + electiveGPA + "\nAcademic Standing: " + academicStanding + "\nGraduation Status: " + graduationStatus + "\n";
        for (Course course : courses) {
            student += course.fulltoString() + "\n";
        }
        return student;
    }

    public ArrayList<Course> checkCoreCourses(ArrayList<Course> passCoreReqs) {
        //Takes the list of courses a student has taken and compares them to the list of core courses
        ArrayList<Course> coreReqs = new ArrayList<Course>(passCoreReqs);
        this.coreCourses = new ArrayList<Course>();
        for (Course course : courses) {
            for (Course coreReq : passCoreReqs) {
                if (course.getPrefix().equals(coreReq.getPrefix()) && course.getNumber() == coreReq.getNumber()) {
                    coreReqs.remove(coreReq);
                    this.coreCourses.add(coreReq);
                }
            }
        }

        //Returns empty if all core courses have been taken otherwise returns the list of core courses that still need to be taken
        return coreReqs;
    }

    //Checks if the student meets the elective Hour Requirement, returns the remaining elective hours
    public Double checkElectiveCourses(Double electiveHourReq) {
        this.electiveCourses = new ArrayList<Course>();
        Double hoursLeft = electiveHourReq;
        boolean clearsCore = true;
        boolean clearsPreReq = true;

        for (Course course : courses) {
            for (Course coreCourse : coreCourses)
                if (course.isEqual(coreCourse)) {
                    clearsCore = false;
                    break;
                } else {
                    for (Course PreReqCourse : preReqCourses){
                        if (course.isEqual(PreReqCourse)){
                            clearsPreReq = false;
                            break;
                        }
                    }
            }
            
            //Adds the course as an elective course if they are not apart of the Core or PreReq Courses
            if (clearsCore && clearsPreReq) {
                electiveCourses.add(course);
            }
        }


        //Iterates through the electives after they've been added and subtracts the credits from the hoursLeft
        for (Course course : electiveCourses) {
            hoursLeft -= course.getCreds();
        }
        
        //Returns empty if all elective courses have been taken otherwise returns the list of elective courses that still need to be taken
        return hoursLeft;
    }

    //Checks if the student meets the preReq Course Requirement returns a list of all the preReq courses that still need to be taken
    public ArrayList<Course> checkPreRequisiteCourses(ArrayList<Course> DPpreReqCourses) {
        this.preReqCourses = new ArrayList<Course>();
        ArrayList<Course> altPreReqCourses = new ArrayList<Course>(DPpreReqCourses);
        for (Course course : courses) {
            for (Course preReqCourse : DPpreReqCourses) {
                if (course.isEqual(preReqCourse)) {
                    altPreReqCourses.remove(preReqCourse);
                    this.preReqCourses.add(course);
                }
            }
        }

        return altPreReqCourses;
    }


    public void calculateCumGPA() {
    
    }

    public void calculateCoreGPA() {
        //Takes the list of core courses a student has taken and calculates their core GPA
        //Only works after checkCoreCourses() has been called
        for(Course course: coreCourses){
            this.coreGPA += course.getCreds();
        }
    }

    public void calculateElectiveGPA() {
        //Takes the list of elective courses a student has taken and calculates their elective GPA
        //Only works after checkElectiveCourses() has been called
        for(Course course: electiveCourses){
            this.electiveGPA += course.getCreds();
        }
    }

    public void removeCourse(Course course){
        //Takes a course and removes it from the list of courses a student has taken
        this.courses.remove(course);
        if (this.coreCourses.contains(course)){
            this.coreCourses.remove(course);
        }
        else if (this.electiveCourses.contains(course)){
            this.electiveCourses.remove(course);
        }
    }

    public void initializeCourses(){
            //Checking Prerequisites
            this.checkPreRequisiteCourses(this.degreePlan.getPreRequisiteCourses());
            //Checking student core Requirements
            this.checkCoreCourses(this.degreePlan.getCoreCourses());
            //Checking student elective Requirements
            this.checkElectiveCourses(this.degreePlan.getElectiveHourRequirement());
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
    public Boolean getFastTrack() {
        return fastTrack;
    }
    public void setFastTrack(Boolean fastTrack) {
        this.fastTrack = fastTrack;
    }
    public void setThesis(Boolean thesis) {
        this.thesis = thesis;
    }
    public Boolean getThesis() {
        return thesis;
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
    public List<Course> getPreReqCourses() {
        return preReqCourses;
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
    public void setDegreePlan(DegreePlan degreePlan) {
        this.degreePlan = degreePlan;
    }
    public DegreePlan getDegreePlan() {
        return degreePlan;
    }
    

}