package org.example;

import java.util.ArrayList;

public class DegreePlan {
    String DPname;
    ArrayList<Course> coreCourses;
    Double ElectiveHourRequirement;
    ArrayList<Course> preRequisiteCourses;

    //Default Constructor
    public DegreePlan() {
        DPname = "";
        coreCourses = new ArrayList<Course>();
        ElectiveHourRequirement = 0.0;
        preRequisiteCourses = new ArrayList<Course>();
    }

    public DegreePlan(String DPname, ArrayList<Course> coreCourses, Double ElectiveHourRequirement, ArrayList<Course> preRequisiteCourses) {
        this.DPname = DPname;
        this.coreCourses = coreCourses;
        this.ElectiveHourRequirement = ElectiveHourRequirement;
        this.preRequisiteCourses = preRequisiteCourses;
    }

    //toString method
    public String toString() {
        return "Degree Plan: [DPname=" + DPname + ", coreCourses=" + coreCourses + ", ElectiveHourRequirement=" + Double.toString(ElectiveHourRequirement) + ", preRequisiteCourses=" + preRequisiteCourses + "]";
    }

    // Access/Setters for the variables
    public String getDPname() {
        return DPname;
    }
    public void setDPname(String DPname) {
        this.DPname = DPname;
    }
    public ArrayList<Course> getCoreCourses() {
        return coreCourses;
    }
    public void setCoreCourses(ArrayList<Course> coreCourses) {
        this.coreCourses = coreCourses;
    }
    public Double getElectiveHourRequirement() {
        return ElectiveHourRequirement;
    }
    public void setElectiveHourRequirement(Double ElectiveHourRequirement) {
        this.ElectiveHourRequirement = ElectiveHourRequirement;
    }
    public ArrayList<Course> getPreRequisiteCourses() {
        return preRequisiteCourses;
    }
    public void setPreRequisiteCourses(ArrayList<Course> preRequisiteCourses) {
        this.preRequisiteCourses = preRequisiteCourses;
    }

    public void addCoreCourse(Course course) {
        coreCourses.add(course);
    }

    public void addPreRequisiteCourse(Course course) {
        preRequisiteCourses.add(course);
    }

}
