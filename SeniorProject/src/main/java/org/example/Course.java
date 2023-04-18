package org.example;

public class Course {
    public String semester;
    public String prefix;
    public int number;
    public String description;
    public float creds;
    public String grade;

    public String getSemester() {
        return semester;
    }
    public void setSemester(String semester) {
        this.semester = semester;
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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public float getCreds() {
        return creds;
    }
    public void setCreds(float creds) {
        this.creds = creds;
    }
    public String getGrade() {
        return grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }
    public String printout() {
        String ret = "Prefix: " + prefix + "\nSemester: " + semester + "\nNumber: " + number + "\nDescription: " + description + "\nCredits: " + creds + "\nGrade: " + grade;
        return ret;
    }
}
