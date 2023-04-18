package com.example;
import java.util.ArrayList;
import java.util.List;

public class Student {
    public String name = "";
    public int ID = 0;
    public String major = "";
    public String sem1 = "";
    public float ccGPA = 0;
    public List<Course> courses = new ArrayList<Course>();
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getID() {
        return ID;
    }
    public void setID(int iD) {
        ID = iD;
    }
    public String getMajor() {
        return major;
    }
    public void setMajor(String major) {
        this.major = major;
    }
    public String getSem1() {
        return sem1;
    }
    public void setSem1(String sem1) {
        this.sem1 = sem1;
    }
    public float getCcGPA() {
        return ccGPA;
    }
    public void setCcGPA(float ccGPA) {
        this.ccGPA = ccGPA;
    }

    public void addCourse(Course c){
        courses.add(c);
    }

    public String printOut(){
        String ret = "Name: " + name + "\nID: " + ID + "\nFirst Semester: " + sem1 + "\nMajor: " + major + "\nCombined Cumulative GPA: " + ccGPA + "\n\nCOURSES: \n\n";
        for(int i = 0; i < courses.size(); i++){
            ret = ret + courses.get(i).printout() + "\n\n";
        }
        
        return ret;
    }
}
