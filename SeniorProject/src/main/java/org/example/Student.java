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
        String student = "Student Name: " + name + "\nStudent ID: " + id + "\nProgram: " + program + "\nDegree Plan: " + degreePlan.getDPname() + "\nMajor: " + major + "\nSpecialization: " + specialization + "\nAdmitted Date: " + admittedDate + "\nAnticipated Graduation: " + anticipatedGraduation + "\nFast Track: " + fastTrack + "\nThesis: " + thesis + "\nCumulative GPA: " + cumulativeGPA + "\nCore GPA: " + coreGPA + "\nElective GPA: " + electiveGPA + "\nAcademic Standing: " + academicStanding + "\nGraduation Status: " + graduationStatus + "\n";
        for (Course course : courses) {
            student += course.fulltoString() + "\n";
        }
        return student;
    }

    public ArrayList<Course> checkCoreCourses(ArrayList<Course> passCoreReqs) {
        //Takes the list of courses a student has taken and compares them to the list of core courses
        ArrayList<Course> coreReqs = new ArrayList<Course>(passCoreReqs);
        ArrayList<Course> copyCoreCourses = new ArrayList<Course>();
        this.coreCourses = new ArrayList<Course>();
        for (Course course : courses) {
            for (Course coreReq : passCoreReqs) {
                if (course.isEqual(coreReq)) {
                    coreReqs.remove(coreReq);
                    for (Course coreCourse : copyCoreCourses) {
                        if (course.isEqual(coreCourse)) {
                            this.coreCourses.remove(coreCourse);
                        }
                    }
                    this.coreCourses.add(course);
                    copyCoreCourses = new ArrayList<Course>(coreCourses);
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
        ArrayList<Course> copyElectiveCourses = new ArrayList<Course>();

        
        for (Course course : courses) {
            boolean clearsCore = true;
            boolean clearsPreReq = true;
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
            for (Course electiveCourse : copyElectiveCourses) {
                if ((course.isEqual(electiveCourse))) {
                    this.electiveCourses.remove(electiveCourse);
                }
            }

            if ((clearsCore && clearsPreReq && (course.getNumber() > 5300))) {
                electiveCourses.add(course);
            }

            copyElectiveCourses = new ArrayList<Course>(electiveCourses);
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

    public boolean moveCourse(String moveInput) {
        String[] moveInputArray = moveInput.split(" ");
        String coursePrefix = moveInputArray[0];
        String courseNumberString = moveInputArray[1];
        int courseNumber = Integer.valueOf(courseNumberString);
        String from = moveInputArray[2];
        String to = moveInputArray[4];
        if(from.equals("core") && to.equals("prereq")) {

        } else if (from.equals("core") && to.equals("elective")) {

        } else if (from.equals("elective") && to.equals("core")) {

        } else if (from.equals("elective") && to.equals("prereq")) {

        } else if (from.equals("prereq") && to.equals("core")) {

        } else if (from.equals("prereq") && to.equals("elective")) {
            ArrayList<Course> copypreReqCourses = new ArrayList<>(preReqCourses);
            for (Course course : copypreReqCourses) {
                if ((course.getPrefix().equals(coursePrefix) && (course.getNumber() == courseNumber))) {
                    electiveCourses.add(course);
                    preReqCourses.remove(course);
                }
            }
            return true;
        }
        return false;
    }

    public void calculateCumGPA() {
        calculateCoreGPA();
        calculateElectiveGPA();
        double pre = calculatePreReqGPA();
        double c = 0;
        double e = 0;
        double p = 0;
        for(Course course : this.coreCourses){
            if(course.getGrade() != null) {
                if(!(course.getGrade().equals("W") || course.getGrade().equals("none") || course.getGrade().equals("P") || course.getGrade().equals("I"))){
                    c += course.getCreds();
                }
            }
        }
        for(Course course : this.electiveCourses){
            if(course.getGrade() != null) {
                if(!(course.getGrade().equals("W") || course.getGrade().equals("none") || course.getGrade().equals("P") || course.getGrade().equals("I"))){
                    e += course.getCreds();
                }
            }
        }
        for(Course course : this.preReqCourses){
            if(course.getGrade() != null) {
                if(!(course.getGrade().equals("W") || course.getGrade().equals("none") || course.getGrade().equals("P") || course.getGrade().equals("I"))){
                    p += course.getCreds();
                }
            }
        }
        cumulativeGPA = ((c * coreGPA) + (e * electiveGPA) + (p * pre)) / (e + c + p);
        //System.out.println("Cumulative GPA is: " + cumulativeGPA);
    }

    public void calculateCoreGPA() {
        //Takes the list of core courses a student has taken and calculates their core GPA
        //Only works after checkCoreCourses() has been called
        int count = 0;
        this.coreGPA = 0;
        //System.out.println("grade calc start");
        for(Course course: coreCourses){
            //System.out.println(course.fulltoString());
            double a = 0;
            count += course.getCreds();
            if(course.getGrade().equals("A") || course.getGrade().equals("A+")){
                a = 4.0;
            }
            else if(course.getGrade().equals("A-")){
                a = 3.67;
            }
            else if(course.getGrade().equals("B+")){
                a = 3.33;
            }
            else if(course.getGrade().equals("B")){
                a = 3.0;
            }
            else if(course.getGrade().equals("B-")){
                a = 3.67;
            }
            else if(course.getGrade().equals("A-")){
                a = 2.67;
            }
            else if(course.getGrade().equals("C+")){
                a = 2.33;
            }
            else if(course.getGrade().equals("C")){
                a = 2.0;
            }
            else if(course.getGrade().equals("F")){
                a = 0;
            }
            else if(course.getGrade().equals("W") || course.getGrade().equals("none") || course.getGrade().equals("P") || course.getGrade().equals("I")){
                count -= course.getCreds();
            }
            else{
                //System.out.println("Invalid Letter Grade");
                count -= course.getCreds();
            }
            this.coreGPA += course.getCreds() * a;
        }
        if(count != 0){
            this.coreGPA = this.coreGPA / count;
        }
        //System.out.println("Core GPA Calc: " + this.coreGPA);
    }

    public void calculateElectiveGPA() {
        //Takes the list of elective courses a student has taken and calculates their elective GPA
        //Only works after checkElectiveCourses() has been called
        int count = 0;
        this.electiveGPA = 0;
        //System.out.println("grade calc start");
        for(Course course: electiveCourses){
            //System.out.println(course.getGrade());
            //System.out.println(course.fulltoString());
            double a = 0;
            count += course.getCreds();
            if(course.getGrade() != null) { 
                if(course.getGrade().equals("A") || course.getGrade().equals("A+")){
                    a = 4;
                }
                else if(course.getGrade().equals("A-")){
                    a = 3.67;
                }
                else if(course.getGrade().equals("B+")){
                    a = 3.33;
                }
                else if(course.getGrade().equals("B")){
                    a = 3.0;
                }
                else if(course.getGrade().equals("B-")){
                    a = 3.67;
                }
                else if(course.getGrade().equals("A-")){
                    a = 2.67;
                }
                else if(course.getGrade().equals("C+")){
                    a = 2.33;
                }
                else if(course.getGrade().equals("C")){
                    a = 2.0;
                }
                else if(course.getGrade().equals("F")){
                    a = 0;
                }
                else if(course.getGrade().equals("W") || course.getGrade().equals("none") || course.getGrade().equals("P") || course.getGrade().equals("I")){
                    count -= course.getCreds();
                }
                else{
                    //System.out.println("Invalid Letter Grade");
                    count -= course.getCreds();
                }
                this.electiveGPA += course.getCreds() * a;
            }
        }
        if(count != 0){
            this.electiveGPA = this.electiveGPA / count;
        }
        //System.out.println("Elective GPA Calc: " + this.electiveGPA);
    }

    public double calculatePreReqGPA() {
        //Takes the list of core courses a student has taken and calculates their core GPA
        //Only works after checkCoreCourses() has been called
        int count = 0;
        double ret = 0;
        //System.out.println("grade calc start");
        for(Course course: preReqCourses){
            //System.out.println(course.fulltoString());
            double a = 0;
            count += course.getCreds();
            if(course.getGrade() != null) {
                if(course.getGrade().equals("A") || course.getGrade().equals("A+")){
                    a = 4.0;
                }
                else if(course.getGrade().equals("A-")){
                    a = 3.67;
                }
                else if(course.getGrade().equals("B+")){
                    a = 3.33;
                }
                else if(course.getGrade().equals("B")){
                    a = 3.0;
                }
                else if(course.getGrade().equals("B-")){
                    a = 3.67;
                }
                else if(course.getGrade().equals("A-")){
                    a = 2.67;
                }
                else if(course.getGrade().equals("C+")){
                    a = 2.33;
                }
                else if(course.getGrade().equals("C")){
                    a = 2.0;
                }
                else if(course.getGrade().equals("F")){
                    a = 0;
                }
                else if(course.getGrade().equals("W") || course.getGrade().equals("none") || course.getGrade().equals("P") || course.getGrade().equals("I")){
                    count -= course.getCreds();
                }
                else{
                    //System.out.println("Invalid Letter Grade");
                    count -= course.getCreds();
                }
                ret += course.getCreds() * a;
            }
        }
        if(count == 0){
            return 0;
        }
        ret = ret / count;
        //System.out.println("PreReq GPA count: " + ret);
        return ret;
    }


    //OutstandingElectives, OutstandingCore, and OutstandingCourses the last section of the Audit
    //The returns may need to be adjusted for formatting
    public String outstandingElectives(){
        double total = 0;
        double finished = 0;
        double unfinishedH = 0;
        ArrayList<Course> unfulfilled = new ArrayList<Course>();
        for(Course course : this.electiveCourses){
            if(!course.getGrade().equals("P")){
                total += course.getCreds();
            }
            if(course.getGrade().equals("W") || course.getGrade().equals("none") || course.getGrade().equals("I")){
                unfulfilled.add(course);
                unfinishedH += course.getCreds();
            }
            else{
                double a;
                if(course.getGrade().equals("A") || course.getGrade().equals("A+")){
                    a = 4.0;
                }
                else if(course.getGrade().equals("A-")){
                    a = 3.67;
                }
                else if(course.getGrade().equals("B+")){
                    a = 3.33;
                }
                else if(course.getGrade().equals("B")){
                    a = 3.0;
                }
                else if(course.getGrade().equals("B-")){
                    a = 3.67;
                }
                else if(course.getGrade().equals("A-")){
                    a = 2.67;
                }
                else if(course.getGrade().equals("C+")){
                    a = 2.33;
                }
                else if(course.getGrade().equals("C")){
                    a = 2.0;
                }
                else{
                    a = 0;
                }
                finished += a * course.getCreds();
            }
        }

        if(unfinishedH == 0){
            return "Electives Complete";
        }

        //System.out.println("Elective Stats:\n\nTotal Points Needed: " + total * 3 + "\nTotal Points from completed: " + finished + "\nCourses found to be unfinished: " + unfulfilled.size() + "\n");
        double totalpoints = ((3.0 * total) - finished) / unfinishedH;
        String retElectives = "";
        for(Course c : unfulfilled){
            retElectives += c.getPrefix() + " " + c.getNumber() + ", ";
        }
        retElectives = retElectives.substring(0, retElectives.length()-2);
        if(totalpoints <= 2){
            return ("To maintain a 3.0 elective GPA: the student needs to pass " + retElectives);
        }
        return ("To maintain a 3.0 elective GPA: the student needs a GPA >= " + totalpoints + " in " + retElectives);
    }

    public String outstandingCore(){
        double total = 0;
        double finished = 0;
        double unfinishedH = 0;
        ArrayList<Course> unfulfilled = new ArrayList<Course>();
        for(Course course : this.coreCourses){
            if(!course.getGrade().equals("P")){
                total += course.getCreds();
            }
            if(course.getGrade().equals("W") || course.getGrade().equals("none") || course.getGrade().equals("I")){
                unfulfilled.add(course);
                unfinishedH += course.getCreds();
            }
            else{
                double a;
                if(course.getGrade().equals("A") || course.getGrade().equals("A+")){
                    a = 4.0;
                }
                else if(course.getGrade().equals("A-")){
                    a = 3.67;
                }
                else if(course.getGrade().equals("B+")){
                    a = 3.33;
                }
                else if(course.getGrade().equals("B")){
                    a = 3.0;
                }
                else if(course.getGrade().equals("B-")){
                    a = 3.67;
                }
                else if(course.getGrade().equals("A-")){
                    a = 2.67;
                }
                else if(course.getGrade().equals("C+")){
                    a = 2.33;
                }
                else if(course.getGrade().equals("C")){
                    a = 2.0;
                }
                else{
                    a = 0;
                }
                finished += a * course.getCreds();
            }
        }

        if(unfinishedH == 0){
            return "Core Complete.";
        }

        //System.out.println("Total Points Needed: " + total * 3.19 + "\nTotal Points from completed: " + finished + "\nCourses found to be unfinished: " + unfulfilled.size());
        double totalpoints = ((3.19 * total) - finished) / unfinishedH;
        String retCore = "";
        for(Course c : unfulfilled){
            retCore += c.getPrefix() + " " + c.getNumber() + ", ";
        }
        retCore = retCore.substring(0, retCore.length()-2);
        if(totalpoints <= 2){
            return ("To maintain a 3.19 core GPA: the student needs to pass " + retCore);
        }
        return ("To maintain a 3.19 core GPA: the student needs a GPA >= " + totalpoints + " in " + retCore);
    }

    public String outstandingCourses(){
        double total = 0;
        double finished = 0;
        double unfinishedH = 0;
        ArrayList<Course> unfulfilled = new ArrayList<Course>();
        for(Course course : this.courses){
            if(!course.getGrade().equals("P")){
                total += course.getCreds();
            }
            if(course.getGrade().equals("W") || course.getGrade().equals("none") || course.getGrade().equals("I")){
                unfulfilled.add(course);
                unfinishedH += course.getCreds();
            }
            else{
                double a;
                if(course.getGrade().equals("A") || course.getGrade().equals("A+")){
                    a = 4.0;
                }
                else if(course.getGrade().equals("A-")){
                    a = 3.67;
                }
                else if(course.getGrade().equals("B+")){
                    a = 3.33;
                }
                else if(course.getGrade().equals("B")){
                    a = 3.0;
                }
                else if(course.getGrade().equals("B-")){
                    a = 3.67;
                }
                else if(course.getGrade().equals("A-")){
                    a = 2.67;
                }
                else if(course.getGrade().equals("C+")){
                    a = 2.33;
                }
                else if(course.getGrade().equals("C")){
                    a = 2.0;
                }
                else{
                    a = 0;
                }
                finished += a * course.getCreds();
            }
        }

        if(unfinishedH == 0){
            return "Courses Complete.";
        }

        //System.out.println("Total Points Needed: " + total * 3.19 + "\nTotal Points from completed: " + finished + "\nCourses found to be unfinished: " + unfulfilled.size());
        double totalpoints = ((3.0 * total) - finished) / unfinishedH;
        String retCore = "";
        for(Course c : unfulfilled){
            retCore += c.getPrefix() + " " + c.getNumber() + ", ";
        }
        retCore = retCore.substring(0, retCore.length()-2);
        if(totalpoints <= 2){
            return ("To maintain a 3.0 GPA: the student needs to pass " + retCore);
        }
        return ("To maintain a 3.0 GPA: the student needs a GPA >= " + totalpoints + " in " + retCore);
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

    public boolean deleteCourse(String input){
        String[] courseInfo = input.split(" ");
        if (courseInfo[0].equals("core")){
            for (Course course : this.coreCourses){
                if (course.getPrefix().equals(courseInfo[1]) && String.valueOf(course.getNumber()).equals(courseInfo[2])){
                    this.coreCourses.remove(course);
                    return true;
                }
            }
        } else if (courseInfo[0].equals("elective")){
            for (Course course : this.electiveCourses){
                if (course.getPrefix().equals(courseInfo[1]) && String.valueOf(course.getNumber()).equals(courseInfo[2])){
                    this.electiveCourses.remove(course);
                    return true;
                }
            }

        } else if (courseInfo[0].equals("prereq")) {
            for (Course course : this.preReqCourses){
                if (course.getPrefix().equals(courseInfo[1]) && String.valueOf(course.getNumber()).equals(courseInfo[2])){
                    this.preReqCourses.remove(course);
                    return true;
                }
            }
        }
        return false;
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
    public void setPreReqCourses(List<Course> preReqCourses) {
        this.preReqCourses = preReqCourses;
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