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
      //Creating the list of degree plans
      ArrayList<DegreePlan> degreePlans = initializeDegreePlans();
      //START OF USER INTERFACING CODE
      Scanner sc = new Scanner(System.in);
      System.out.println("\n\n\n\n\n\n\n\n\nWelcome to the Degree Audit System!");
      boolean invalidInput = true;

      /*while(invalidInput) {
        System.out.println("Please enter a valid Degree Plan track or enter \'Audit\' to perform a Audit: ");
        String input = sc.nextLine();
        
        if(input == "Audit") {

        } else if (checkDPforInput(input, degreePlans)) {

        } else {
          System.out.println("Invalid input");
        }
      }*/

      Student hold = new Student();
      hold = TranscriptReader.parsePDF("C.pdf");

      hold.initializeCourses();
      for (Course course : hold.getElectiveCourses()) {
        System.out.println(course.toString());
      }

      saveStudent(hold, hold.getName());
      hold = loadStudent(hold.getName());

      try {
        createAudit(hold);
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
    public static void createAudit(Student passStudent) throws FileNotFoundException {
        Student student = passStudent;
        String path = "FirstPdf.pdf";
        PdfWriter pdfWriter = new PdfWriter(path);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.addNewPage();
        Document document = new Document(pdfDocument);

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

    public static boolean checkDPforInput(String input, ArrayList<DegreePlan> degreePlans) {
      return false;
    }

    //Initializes the list of all Degree Plans and returns it
    public static ArrayList<DegreePlan> initializeDegreePlans() {
      ArrayList<DegreePlan> degreePlans = new ArrayList<DegreePlan>();

      //Initializing the Degree Plans
      DegreePlan SoftwareEngineering = new DegreePlan("SoftwareEngineering", setSECoreCourses(), 15.0, setSEpreReq());
      DegreePlan DataScience = new DegreePlan("Data Science", setDSCoreCourses(), 15.0, setDSpreReq());
      DegreePlan Systems = new DegreePlan("Systems", setSysCoreCourses(), 15.0, setSyspreReq());
      DegreePlan CyberSecurity = new DegreePlan("Cyber Security", setCyberCoreCourses(), 12.0, setCyberpreReq());
      DegreePlan IntelligentSystems = new DegreePlan("Intelligent Systems", setISCoreCourses(), 15.0, setISpreReq());
      DegreePlan InteractiveComputing = new DegreePlan("Interactive Computing", setICCoreCourses(), 15.0, setICpreReq());
      DegreePlan TraditionalCS = new DegreePlan("Traditional CS", setTCCoreCourses(), 15.0, setTCpreReq());
      DegreePlan NetworksTelecommunication = new DegreePlan("Networks and Telecommunication", setNTCoreCourses(), 15.0, setNTpreReq());

      //adding the degree plans to the list
      degreePlans.add(SoftwareEngineering);
      degreePlans.add(DataScience);
      degreePlans.add(Systems);
      degreePlans.add(CyberSecurity);
      degreePlans.add(IntelligentSystems);
      degreePlans.add(InteractiveComputing);
      degreePlans.add(TraditionalCS);
      degreePlans.add(NetworksTelecommunication);

      return degreePlans;
    }

    //Sets and returns the list of Software Engineering Pre-Reqs
    public static ArrayList<Course> setSEpreReq() {
      ArrayList<Course> SEpreReq = new ArrayList<Course>();
      SEpreReq.add(new Course("CS", 5303, "Computer Science 1"));
      SEpreReq.add(new Course("CS", 5330, "Computer Science 2"));
      SEpreReq.add(new Course("CS", 5333, "Discrete Structures"));
      SEpreReq.add(new Course("CS", 5343, "Algorithm Analysis & Data Structures"));
      SEpreReq.add(new Course("CS", 5348, "Operating Systems Concepts"));
      SEpreReq.add(new Course("CS", 5354, "Software Engineering"));
      return SEpreReq;
    }

    //Sets and returns the list of Software Engineering Core Courses
    public static ArrayList<Course> setSECoreCourses() {
      ArrayList<Course> SECourses = new ArrayList<Course>();
      SECourses.add(new Course("SE", 6329, "Object Oriented Software Engineering"));
      SECourses.add(new Course("SE", 6361, "Advanced requirements engineering"));
      SECourses.add(new Course("SE", 6362, "Adv Software Archtctr & Design"));
      SECourses.add(new Course("SE", 6367, "Software Testing, Validation & Verification"));
      SECourses.add(new Course("SE", 6387, "Advanced Software Engineering Project"));
      return SECourses;
    }

    public static ArrayList<Course> setDSCoreCourses() {
      ArrayList<Course> DSCourses = new ArrayList<Course>();
      DSCourses.add(new Course("CS", 6313, "Statistical Methods for Data Science"));
      DSCourses.add(new Course("CS", 6350, "Big Data Management and Analytics"));
      DSCourses.add(new Course("CS", 6363, "Design and Analysis of Computer Algorithms"));
      DSCourses.add(new Course("CS", 6375, "Machine Learning"));

      DSCourses.add(new Course("CS", 6301, "Social Network Analytics"));
      DSCourses.add(new Course("CS", 6320, "Natural Language Processing"));
      DSCourses.add(new Course("CS", 6327, "Video Analytics"));
      DSCourses.add(new Course("CS", 6347, "Statistics for Machine Learning"));
      DSCourses.add(new Course("CS", 6360, "Database Design"));

      return DSCourses;
    }

    public static ArrayList<Course> setDSpreReq() {
      ArrayList<Course> DSpreReq = new ArrayList<Course>();
      DSpreReq.add(new Course("CS", 5303, "Computer Science 1"));
      DSpreReq.add(new Course("CS", 5330, "Computer Science 2"));
      DSpreReq.add(new Course("CS", 5333, "Discrete Structures"));
      DSpreReq.add(new Course("CS", 5343, "Algorithm Analysis & Data Structures"));
      DSpreReq.add(new Course("CS", 5348, "Operating Systems Concepts"));
      DSpreReq.add(new Course("CS", 3341, "Probablility and Statistics in Computer Science"));
      return DSpreReq;
    }

    public static ArrayList<Course> setSysCoreCourses() {
      ArrayList<Course> SysCourses = new ArrayList<Course>();
      SysCourses.add(new Course("CS", 6304, "Computer Architecture"));
      SysCourses.add(new Course("CS", 6363, "Design and Analysis of Computer Networks"));
      SysCourses.add(new Course("CS", 6378, "Advanced Operating Systems"));
      SysCourses.add(new Course("CS", 6396, "Real-Time Systems"));

      SysCourses.add(new Course("CS", 6349, "Network Security"));
      SysCourses.add(new Course("CS", 6376, "Parallel Processing"));
      SysCourses.add(new Course("CS", 6380, "Distributed Computing"));
      SysCourses.add(new Course("CS", 6397, "Synthesis and Optimization of High-Performance Systems"));
      SysCourses.add(new Course("CS", 6399, "Parallel Architecture and Systems"));
      return SysCourses;
    }

    public static ArrayList<Course> setSyspreReq() {
      ArrayList<Course> SyspreReq = new ArrayList<Course>();
      SyspreReq.add(new Course("CS", 5303, "Computer Science 1"));
      SyspreReq.add(new Course("CS", 5330, "Computer Science 2"));
      SyspreReq.add(new Course("CS", 5333, "Discrete Structures"));
      SyspreReq.add(new Course("CS", 5343, "Algorithm Analysis & Data Structures"));
      SyspreReq.add(new Course("CS", 5348, "Operating Systems Concepts"));
      SyspreReq.add(new Course("CS", 5390, "Computer Networks"));

      return SyspreReq;
    }

    public static ArrayList<Course> setISCoreCourses() {
      ArrayList<Course> ISCourses = new ArrayList<Course>();
      ISCourses.add(new Course("CS", 6320, "Natural Language Processing"));
      ISCourses.add(new Course("CS", 6363, "Design and Analysis of Computer Algorithms"));
      ISCourses.add(new Course("CS", 6364, "Artificial Intelligence"));
      ISCourses.add(new Course("CS", 6375, "Machine Learning"));

      ISCourses.add(new Course("CS", 6360, "Database Design"));
      ISCourses.add(new Course("CS", 6378, "Advanced Operating Systems"));
      return ISCourses;
    }

    public static ArrayList<Course> setISpreReq() {
      ArrayList<Course> ISpreReq = new ArrayList<Course>();
      ISpreReq.add(new Course("CS", 5303, "Computer Science 1"));
      ISpreReq.add(new Course("CS", 5330, "Computer Science 2"));
      ISpreReq.add(new Course("CS", 5333, "Discrete Structures"));
      ISpreReq.add(new Course("CS", 5343, "Algorithm Analysis & Data Structures"));
      ISpreReq.add(new Course("CS", 5348, "Operating Systems Concepts"));
      return ISpreReq;
    }

    public static ArrayList<Course> setCyberCoreCourses() {
      ArrayList<Course> CyberCourses = new ArrayList<Course>();
      CyberCourses.add(new Course("CS", 6324, "Information Security"));
      CyberCourses.add(new Course("CS", 6363, "Design and Analysis of Computer Algorithms"));
      CyberCourses.add(new Course("CS", 6378, "Advanced Operating Systems"));

      CyberCourses.add(new Course("CS", 6332, "System Security & Malicious Code Analysis"));
      CyberCourses.add(new Course("CS", 6348, "Data and Applications Security"));
      CyberCourses.add(new Course("CS", 6349, "Network Security"));
      CyberCourses.add(new Course("CS", 6377, "Introduction To Cryptography"));
      return CyberCourses;
    }

    public static ArrayList<Course> setCyberpreReq() {
      ArrayList<Course> CyberpreReq = new ArrayList<Course>();
      CyberpreReq.add(new Course("CS", 5303, "Computer Science 1"));
      CyberpreReq.add(new Course("CS", 5330, "Computer Science 2"));
      CyberpreReq.add(new Course("CS", 5333, "Discrete Structures"));
      CyberpreReq.add(new Course("CS", 5343, "Algorithm Analysis & Data Structures"));
      CyberpreReq.add(new Course("CS", 5348, "Operating Systems Concepts"));
      CyberpreReq.add(new Course("CS", 5390, "Computer Networks"));
      return CyberpreReq;
    }

    public static ArrayList<Course> setICpreReq() {
      ArrayList<Course> ICpreReq = new ArrayList<Course>();
      ICpreReq.add(new Course("CS", 5303, "Computer Science 1"));
      ICpreReq.add(new Course("CS", 5330, "Computer Science 2"));
      ICpreReq.add(new Course("CS", 5333, "Discrete Structures"));
      ICpreReq.add(new Course("CS", 5343, "Algorithm Analysis & Data Structures"));
      ICpreReq.add(new Course("CS", 5348, "Operating Systems Concepts"));
      return ICpreReq;
    }

    public static ArrayList<Course> setICCoreCourses() {
      ArrayList<Course> ICCourses = new ArrayList<Course>();
      ICCourses.add(new Course("CS", 6326, "Human Computer Interaction"));
      ICCourses.add(new Course("CS", 6363, "Design and Analysis of Computer Algorithms"));
      ICCourses.add(new Course("CS", 6323, "Computer Animation and Gaming"));
      ICCourses.add(new Course("CS", 6328, "Modeling and Simulation"));
      ICCourses.add(new Course("CS", 6331, "Multimedia Systems"));
      ICCourses.add(new Course("CS", 6334, "Virtual Reality"));
      ICCourses.add(new Course("CS", 6366, "Computer Graphics"));

      return ICCourses;
    }

    public static ArrayList<Course> setTCCoreCourses() {
      ArrayList<Course> TCCourses = new ArrayList<Course>();
      TCCourses.add(new Course("CS", 6363, "Design and Analysis of Computer Algorithms"));
      TCCourses.add(new Course("CS", 6378, "Advanced Operating Systems"));
      TCCourses.add(new Course("CS", 6390, "Advanced Computer Networks"));
      TCCourses.add(new Course("CS", 6353, "Compiler Construction"));
      TCCourses.add(new Course("CS", 6360, "Database Design"));
      TCCourses.add(new Course("CS", 6371, "Advanced Programming Languages"));
      return TCCourses;
    }

    public static ArrayList<Course> setTCpreReq() {
      ArrayList<Course> TCpreReq = new ArrayList<Course>();
      TCpreReq.add(new Course("CS", 5303, "Computer Science 1"));
      TCpreReq.add(new Course("CS", 5330, "Computer Science 2"));
      TCpreReq.add(new Course("CS", 5333, "Discrete Structures"));
      TCpreReq.add(new Course("CS", 5343, "Algorithm Analysis & Data Structures"));
      TCpreReq.add(new Course("CS", 5348, "Operating Systems Concepts"));
      TCpreReq.add(new Course("CS", 5349, "Automata Theory"));
      TCpreReq.add(new Course("CS", 5390, "Computer Networks"));
      

      return TCpreReq;
    }

    public static ArrayList<Course> setNTCoreCourses() {
      ArrayList<Course> NTCourses = new ArrayList<Course>();
      NTCourses.add(new Course("CS", 6352, "Perf. Of Computer Systems and Networks"));
      NTCourses.add(new Course("CS", 6363, "Design and Analysis of Computer Networks"));
      NTCourses.add(new Course("CS", 6378, "Advanced Operating Systems"));
      NTCourses.add(new Course("CS", 6385, "Algorithmic Aspects of Telecommunication Networks"));
      NTCourses.add(new Course("CS", 6390, "Advanced Computer Networks"));
      return NTCourses;
    }

    public static ArrayList<Course> setNTpreReq() {
      ArrayList<Course> NTpreReq = new ArrayList<Course>();
      NTpreReq.add(new Course("CS", 5303, "Computer Science 1"));
      NTpreReq.add(new Course("CS", 5330, "Computer Science 2"));
      NTpreReq.add(new Course("CS", 5333, "Discrete Structures"));
      NTpreReq.add(new Course("CS", 5343, "Algorithm Analysis & Data Structures"));
      NTpreReq.add(new Course("CS", 5348, "Operating Systems Concepts"));
      NTpreReq.add(new Course("CS", 5390, "Computer Networks"));
      NTpreReq.add(new Course("CS", 3341, "Probablility and Statistics in Computer Science"));
      return NTpreReq;
    }

}
