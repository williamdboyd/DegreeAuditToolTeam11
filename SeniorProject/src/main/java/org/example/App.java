package org.example;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TabAlignment;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;


//just tests the transcript reader on a specified pdf

public class App 
{
    public static String[] AdmissionLetter;
    public static void main( String[] args )
    {
        Student hold = TranscriptReader.parsePDF("C.pdf");
        ArrayList<Course> SECourses = new ArrayList<Course>();
        ArrayList<Course> SEpreReq = new ArrayList<Course>();
        SECourses.add(new Course("SE", 6329, "Object Oriented Software Engineering"));
        SECourses.add(new Course("SE", 6361, "Advanced requirements engineering"));
        SECourses.add(new Course("SE", 6362, "Adv Software Archtctr & Design"));
        SECourses.add(new Course("SE", 6367, "Software Testing, Validation & Verification"));
        SECourses.add(new Course("SE", 6387, "Advanced Software Engineering Project"));
        SEpreReq.add(new Course("CS", 5303, "Computer Science 1"));
        SEpreReq.add(new Course("CS", 5330, "Computer Science 2"));
        SEpreReq.add(new Course("CS", 5333, "Discrete Structures"));
        SEpreReq.add(new Course("CS", 5343, "Data Structures"));
        SEpreReq.add(new Course("CS", 5348, "Operating Systems Concepts"));
        SEpreReq.add(new Course("CS", 5354, "Software Engineering"));
        DegreePlan SoftwareEngineering = new DegreePlan("Software Engineering", SECourses, 15.0, SEpreReq);

        try {
            FileWriter myWriter = new FileWriter("filename.txt");
            myWriter.write(hold.toString());
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
          } catch (IOException e) {
            System.out.println("An error occurred.");
            //e.printStackTrace();
          }
          
          saveStudent(hold, hold.getName());
          hold = loadStudent(hold.getName());
          try {
            createAudit(hold , SoftwareEngineering);  //NEEDS TO BE MADE DYNAMIC TO ACCEPT ANY DEGREE PLAN
          } catch(IOException e) {
            System.out.println("Couldn't create Audit");
          }
    }

    //Saves the student object as a file in the savedStudents folder
    public static void saveStudent(Student student, String fileName) {
      File myFile = new File("savedStudents\\"+ fileName + ".txt");
      try {
        myFile.createNewFile();
        try {
          FileWriter myWriter = new FileWriter(myFile);
          myWriter.write(student.toString());
          myWriter.close();
        } catch (IOException e) {
          System.out.println("An error occurred.");
        }
      } catch(IOException e) {
        System.out.println("An error occurred.");
      }
    
    }

    //Loads a student object from a file in the savedStudents folder
    public static Student loadStudent(String fileName) {
        Student student = new Student();
        ArrayList<Course> courses = new ArrayList<Course>();
        File myFile = new File("savedStudents\\" + fileName + ".txt");
        try {
          //Parsing the student file
          Scanner sc = new Scanner(myFile);
          student.setName(sc.nextLine().substring(14));
          student.setID(Integer.parseInt(sc.nextLine().substring(12)));
          student.setProgram(sc.nextLine().substring(9));
          student.setMajor(sc.nextLine().substring(7));
          student.setSpecialization(sc.nextLine().substring(16));
          student.setAdmittedDate(sc.nextLine().substring(15));
          student.setAnticipatedGraduation(sc.nextLine().substring(24));
          student.setFastTrack(Boolean.parseBoolean(sc.nextLine().substring(12)));
          student.setThesis(Boolean.parseBoolean(sc.nextLine().substring(8)));
          student.setCumulativeGPA(Double.parseDouble(sc.nextLine().substring(16)));
          student.setCoreGPA(Double.parseDouble(sc.nextLine().substring(10)));
          student.setElectiveGPA(Double.parseDouble(sc.nextLine().substring(14)));
          student.setAcademicStanding(sc.nextLine().substring(19));
          student.setGraduationStatus(Boolean.parseBoolean(sc.nextLine().substring(19)));
          while(sc.hasNextLine()) {
            String line = sc.nextLine();
            if(line != ""){
              String[] lineArray = line.split(", ");
              Course course = new Course(lineArray[0].substring(14), lineArray[1].substring(7), Integer.parseInt(lineArray[2].substring(7)), lineArray[3].substring(6), lineArray[4].substring(12), lineArray[5].substring(11), Double.parseDouble(lineArray[6].substring(8, lineArray[6].length() - 1)));
              courses.add(course);
            }
          }
          student.setCourses(courses);
          sc.close();
          System.out.println("Successfully loaded student.");
        } catch(IOException e) {
          System.out.println("Couldn't find file.");
        }

        return student;
    }

    //Function for creating Audit
    public static void createAudit(Student passStudent, DegreePlan DP) throws FileNotFoundException {
        Student student = passStudent;
        String path = "FirstPdf.pdf";
        PdfWriter pdfWriter = new PdfWriter(path);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.addNewPage();
        Document document = new Document(pdfDocument);

        //Checking Prerequisites
        ArrayList<Course> unfinishedPreReqs = student.checkPreRequisiteCourses(DP.getPreRequisiteCourses());
        //Checking student core Requirements
        ArrayList<Course> unfinishedCoreReqs = student.checkCoreCourses(DP.getCoreCourses());
        //Checking student elective Requirements
        Double unfinishedElectiveHours = student.checkElectiveCourses(DP.getElectiveHourRequirement());

        for (Course course : unfinishedPreReqs) {
            System.out.println("Missing PreRequisite: " + course.toString());
        }
        for (Course course : unfinishedCoreReqs) {
            System.out.println("Missing Core Course: " + course.toString());
        }
        if (unfinishedElectiveHours > 0) {
            System.out.println("Missing " + unfinishedElectiveHours + " Elective Hours");
            System.out.println("Elective Courses: ");
            for (Course course : student.getElectiveCourses()) {
                System.out.println(course.toString());
            }
        }

        //Creating Title of the PDF
        Paragraph heading = new Paragraph();
        Text title = new Text("Audit Report");
        heading.add(title);
        heading.setTextAlignment(TextAlignment.CENTER); // aligning to the Center
        heading.setBold();
        heading.setFontSize(16);
        document.add(heading);

        //Creating the General Student Info
        document.add(createParagraphWithTab("Name: ","ID: ", student.getName(), student.getID().toString()));
        document.add(createParagraphWithTab("Plan: ","Major: ", student.getProgram(), student.getMajor()));
        document.add(createParagraphWithTab("","Track: ", "", student.getSpecialization()));

        //Creating the GPA Info
        document.add(new Paragraph("\n"));
        document.add(createDataEntry("Core GPA: ", String.valueOf(student.getCoreGPA())));
        document.add(createDataEntry("Elective GPA: ",String.valueOf(student.getElectiveGPA())));
        document.add(createDataEntry("Combined GPA: ",String.valueOf(student.getCumulativeGPA())));

        //Display the Courses
        document.add(new Paragraph("\n"));
        document.add(displayCourses("Core",student.getCoreCourses()));
        document.add(displayCourses("Elective", student.getElectiveCourses()));

        //Pre-Req
        document.add(new Paragraph("\n"));

        document.add(new Paragraph().add(new Text("Leveling Courses and Pre-requisites from Admission Letter:").setBold()));
        document.add(diplayAdmissionLetter(AdmissionLetter));

        //Outstanding Requirements
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Outstanding Requirements").setBold());



        Paragraph paragraph = new Paragraph();
        document.add(paragraph);
        document.close();
    }

    public static Paragraph createParagraphWithTab(String key, String key2, String value1, String value2) {
        Paragraph p = new Paragraph();
        Text label = new Text(key).setBold();
        Text label2 = new Text(key2).setBold();
        p.addTabStops(new TabStop(200f, TabAlignment.LEFT));
        p.add(label);
        p.add(value1);
        p.add(new Tab());
        p.add(new Tab());
        p.add(new Tab());
        p.add(new Tab());
        p.add(label2);
        p.add(value2);
        p.setFixedLeading(10);
        return p;
    }

    public static Paragraph createDataEntry(String key, String value){
        Paragraph p = new Paragraph();
        Text label = new Text(key).setBold();
        p.add(label);
        p.add(value);
        p.setFixedLeading(10);
        return p;
    }

    public static Paragraph displayCourses(String type, List<Course> list){
        Paragraph p = new Paragraph();
        Text label = new Text(type + " Course: ").setBold();
        p.add(label);
        for(Course course : list){
            p.add(course.getPrefix() + course.getNumber() + ", ");
        }

        p.setFixedLeading(10);
        return p;
    }

    public static Paragraph diplayAdmissionLetter(String[] list) {
        Paragraph p = new Paragraph();
        if(list == null || list.length < 1){
            p.add("None");
            return p;
        }else {
            for (int i = 0; i < list.length; i++) {
                if (i == list.length - 1)
                    p.add(list[i]);
                else
                    p.add(list[i] + "\n");
            }
        }


        p.setFixedLeading(10);
        return p;
    }

}
