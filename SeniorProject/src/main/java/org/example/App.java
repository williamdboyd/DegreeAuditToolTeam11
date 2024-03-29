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
      //Creating an empty student to be used
      Student student = new Student();
      //Creating the list of degree plans
      ArrayList<DegreePlan> degreePlans = initializeDegreePlans();
      DegreePlan DP = new DegreePlan();
      //List of Leveling/Pre-Reqs
      ArrayList<Course> levelingCourses = initalizeLevelingCourses();
      

      //START OF USER INTERFACING CODE
      Scanner sc = new Scanner(System.in);
      System.out.println("\n\n\n\n\n\n\n\n\nWelcome to the Degree Audit System!");
      boolean invalidInput = true;

      boolean doingAudit = false;
      boolean doingPlanning = false;

      while(invalidInput) {
        System.out.println("Please enter \'Planning\' to start a degree plan, or enter \'Audit\' to perform a Audit:");
        String input = sc.nextLine();
        
        if(input.equals("Audit")) {
          doingAudit = true;
          invalidInput = false;
        } else if (input.equals("Planning")) {
          doingPlanning = true;
          invalidInput = false;
        } else {
          System.out.println("Invalid input");
        }
      }
      invalidInput = true;
      while(invalidInput) {
        System.out.println("Would you like to load a student or create a new one?(Load/Create): ");
        String input = sc.nextLine();
        if(input.equals("Load")) {
          System.out.println("Please enter the full name of the student you would like to load: ");
          String name = sc.nextLine();
          student = loadStudent(name, degreePlans);
          if (student == null) {
            System.out.println("Student not found");
          } else{ 
            student.initializeCourses();
            invalidInput = false; 
          }
        } else if (input.equals("Create")) {
          System.out.println("Please enter the name of the transcript file you would like to use. EX:(ExampleTranscript.pdf): ");
          String filePath = sc.nextLine();
          student = TranscriptReader.parsePDF(filePath);
          if (student == null) {
            System.out.println("File not found");
          } else{ 
            boolean DPInput = true;
            System.out.println("Please enter the name of the degree plan you would like to use: ");
            while(DPInput) {
              String DPname = sc.nextLine();
              DP = getDPbyName(DPname, degreePlans);
              if (DP == null) {
                System.out.println("Degree Plan not found, please try again: ");
              } else {
                student.setDegreePlan(DP);
                DPInput = false;
              }
            }
           // System.out.println("Before init student Courses: ");
            student.initializeCourses();
           // System.out.println("After init sutdent courses:" + DP.getCoreCourses().toString());

            invalidInput = false;

            boolean nInput = true;
            System.out.println("Will this student be fast track, thesis, neither, or both?(Fast/Thesis/Neither/Both):");
            while(nInput) {
              String FT = sc.nextLine();
              if(FT.equals("Fast")) {
                student.setThesis(false);
                student.setFastTrack(true);
                nInput = false;
              } else if (FT.equals("Thesis")) {
                student.setThesis(true);
                student.setFastTrack(false);
                nInput = false;
              } else if (FT.equals("Neither")) {
                student.setFastTrack(false);
                student.setThesis(false);
                nInput = false;
              } else if (FT.equals("Both")) {
                student.setFastTrack(true);
                student.setThesis(true);
                nInput = false;
              } else {
                System.out.println("Invalid input, Try again:");
              }
            }
          }
        } else {
          System.out.println("Invalid input");
        }
        boolean nestedInput = true;
            int count = 0;
            System.out.println("The following is a list of Pre-Req or leveling courses: ");

            for(Course course : levelingCourses) {
              System.out.println(String.valueOf(count) + "." + course.toString());
              count += 1;
            }
            System.out.println("Select Pre-Reqs to be included or leave empty if none. EX:(0,1,3):");
            while (nestedInput) {
              String preReqs = sc.nextLine();
              if(parsePreReqsInput(preReqs, student, levelingCourses)) {
                nestedInput = false;
              } else {
                System.out.println("Invalid input, please try again:");
              }
            }
      }

      boolean validInput = true;
      while(validInput) {
        System.out.println("\nDEGREE PLAN:");
        System.out.println("Core Classes:");
        for (Course course : student.getCoreCourses()) {
          System.out.println(course.toString());
        }
  
        System.out.println("Elective Classes:");
        for (Course course : student.getElectiveCourses()) {
          System.out.println(course.toString());
        }
  
        System.out.println("Pre-Reqs:");
        for (Course course : student.getPreReqCourses()) {
          System.out.println(course.toString());
        }
  
        System.out.println("Would you like to edit degree plan?(Y/N):");
        String input = sc.nextLine();
        if (input.equals("Y")) {
          boolean nestedInput = true;
          while(nestedInput) {
            System.out.println("Would you like to change a course? If not enter Cancel.(Move/Delete/Cancel):");
            String operation = sc.nextLine();
            if (operation.equals("Add")) {
              //System.out.println("Please enter the course you would like to add EX:(CS 63** Artificial Intelligence A-) ");
            } else if (operation.equals("Delete")) {
              System.out.println("Please enter the course you would like to delete EX:(prereq CS 63**) ");
              boolean doubleNestedInput = true;
              while(doubleNestedInput) {
                String deleteCourse = sc.nextLine();
                if(deleteCourse.equals("Cancel")) {
                  doubleNestedInput = false;
                } else if(student.deleteCourse(deleteCourse)) {
                  System.out.println("Course deleted successfully");
                  doubleNestedInput = false;
                } else {
                  System.out.println("Invalid input, Try again:");
                }

              }
            } else if (operation.equals("Move")) {
              System.out.println("Please enter the EXISTING course you would like to move EX:(CS 5343 prereq to elective):");
              boolean doubleNestedInput = true;
              while(doubleNestedInput) {
                String moveCourse = sc.nextLine();
                if(moveCourse.equals("Cancel")) {
                  doubleNestedInput = false;
                } else if(student.moveCourse(moveCourse)) {
                  System.out.println("Course moved successfully");
                  doubleNestedInput = false;
                } else {
                  System.out.println("Invalid input, Try again:");
                }
              }
              nestedInput = false;
            } else if (operation.equals("Cancel")) {
              nestedInput = false;
            } else {
              System.out.println("Invalid input, Try again:");
            }
          }
        } else if (input.equals("N")) {
          validInput = false;
        } else {
          System.out.println("Invalid input, Try again:");
        }
      }

      student.calculateCumGPA();
      validInput = true;
      System.out.println("Would you like to save this student?(Y/N):");
      while(validInput) {
        String input = sc.nextLine();
        if (input.equals("Y")) {
          saveStudent(student, student.getName());
          validInput = false;
        } else if (input.equals("N")) {
          validInput = false;
        } else {
          System.out.println("Invalid input, Try again:");
        }
      }

      validInput = true;
      
      System.out.println("Would you like to generate a printable degree plan?(Y/N):");
      while(validInput) {
        String input = sc.nextLine();
        if (input.equals("Y")) {
          //generateDegreePlanPDF(student, student.degreePlan);
          validInput = false;
        } else if (input.equals("N")) {
          validInput = false;
        } else {
          System.out.println("Invalid input, Try again:");
        }
      }

      System.out.println("Would you like to continue Audit?(Y/N):");
      validInput = true;
      while(validInput) {
        String input = sc.nextLine();
        if (input.equals("Y")) {
          validInput = false;
        } else if (input.equals("N")) {
          System.exit(0);
        } else {
          System.out.println("Invalid input, Try again:");
        }
      }

      System.out.println("Core GPA: " + String.valueOf(student.getCoreGPA()) + "\nElective GPA: " + String.valueOf(student.getElectiveGPA()) + "\nCumulative GPA: " + String.valueOf(student.getCumulativeGPA()));

      validInput = true;
      System.out.println("Will the student be taking a extra Elective?(Y/N):");
      while(validInput) {
        String input = sc.nextLine();
        if (input.equals("Y")) {
          boolean nestedInput = true;
          System.out.println("Please enter the prefix and the number of the course you would like to add EX:(CS 63** Algorithms): ");
          while(nestedInput){
            String courseInput = sc.nextLine();
            if(parseElectiveInput(courseInput)) {

              String[] inputList = courseInput.split(" ");
              inputList[0] = inputList[0].toUpperCase();
              Course elective = new Course(inputList[0], Integer.valueOf(inputList[1]), inputList[2]);
              elective.setGrade("none");
              student.getElectiveCourses().add(elective);
              System.out.println("Course added successfully");
              nestedInput = false;
            } else {
              System.out.println("Invalid input, please try again:");
            }
          }
          validInput = false;
        } else if (input.equals("N")) {
          validInput = false;
        } else {
          System.out.println("Invalid input, try again:");
        }
      }

      System.out.println("Will the student be taking additional graduate courses?(Y/N):");
      validInput = true;
      while(validInput) {
        String input = sc.nextLine();
        if(input.equals("Y")){

        } else if (input.equals("N")) {
          validInput = false;
        } else {
          System.out.println("Invalid input, try again:");
        }
      }

      ArrayList<Course> DisposedPreReqs = new ArrayList<Course>();
      for (Course REQcourse : student.getDegreePlan().getPreRequisiteCourses()) {
        boolean contains = false;
        for (Course course : student.getPreReqCourses()) {
          if ((course.getPrefix().equals(REQcourse.getPrefix()) && (course.getNumber() == REQcourse.getNumber()))) {
            contains = true;
          }
        }
        if(!contains) {
          DisposedPreReqs.add(REQcourse);
        }
      }

      student.calculateCumGPA();
      System.out.println("Core GPA: " + String.valueOf(student.getCoreGPA()) + "\nElective GPA: " + String.valueOf(student.getElectiveGPA()) + "\nCumulative GPA: " + String.valueOf(student.getCumulativeGPA()));



      //Setting Disposition for the list of leveling/pre-req courses
      System.out.println("Please set the disposition of all the uncompleted pre-reqs");
      for (Course course: student.getPreReqCourses()) {
        System.out.println("What is the disposition of " + course.toString() + "?");
        String input = sc.nextLine();
        course.setDescription(input);
      }
      

      System.out.println("Would you like to generate and save a printable audit?(Y/N):");
      validInput = true;
      while(validInput) {
        String input = sc.nextLine();
        if(input.equals("Y")) {
          try {
            createAudit(student);
          } catch(IOException e) {
            System.out.println("Couldn't create Audit");
          }
          validInput = false;
        } else if (input.equals("N")){
          validInput = false;
        } else {
          System.out.println("Invalid input, try again:");
        }
      }

      sc.close();
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
    public static Student loadStudent(String fileName, ArrayList<DegreePlan> DP) {
        Student student = new Student();
        ArrayList<Course> courses = new ArrayList<Course>();
        File myFile = new File("savedStudents\\" + fileName + ".txt");
        try {
          //Parsing the student file
          Scanner sc = new Scanner(myFile);
          student.setName(sc.nextLine().substring(14));
          student.setID(Integer.parseInt(sc.nextLine().substring(12)));
          student.setProgram(sc.nextLine().substring(9));
          String DPName = sc.nextLine().substring(13);
          student.setDegreePlan(getDPbyName(DPName, DP));
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
              Course course = new Course(lineArray[0].substring(23), lineArray[1].substring(7), Integer.parseInt(lineArray[2].substring(7)), lineArray[3].substring(6), lineArray[4].substring(12), lineArray[5].substring(11), Double.parseDouble(lineArray[6].substring(8, lineArray[6].length() - 1)));
              courses.add(course);
            }
          }
          student.setCourses(courses);
          sc.close();
          System.out.println("Successfully loaded student.");
        } catch(IOException e) {
          System.out.println("Couldn't find file.");
          return null;
        }

        return student;
    }

    //Function for creating Audit
    public static void createAudit(Student passStudent) throws FileNotFoundException {
      Student student = passStudent;
      String path = "_Audit.pdf";
      path = student.getName() + path;
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
      document.add(createParagraphWithTab("","Track: ", "", student.getDegreePlan().getDPname()));

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
      for (Course course : student.getPreReqCourses()) {
        document.add(new Paragraph(course.toString()));
      }

      //Outstanding Requirements
      document.add(new Paragraph("\n"));
      document.add(new Paragraph("Outstanding Requirements").setBold());
      
      String outstandingCore = student.outstandingCore();
      String outstandingElectives = student.outstandingElectives();
      String outstandingCourses = student.outstandingCourses();
      
      int pos = outstandingCore.indexOf(":", 0);
      String firstCore = "";
      String secondCore = "";

      if (pos != -1) {
        firstCore = outstandingCore.substring(0, pos+1);
        secondCore = outstandingCore.substring(pos+1, outstandingCore.length());
        secondCore = "\t" + secondCore;
        document.add(new Paragraph(firstCore));
        Paragraph sC = new Paragraph();
        sC.add(new Tab());
        sC.add(secondCore);
        document.add(sC);
      } else {
        document.add(new Paragraph(outstandingCore));
      }

      pos = outstandingElectives.indexOf(":", 0);
      String firstElectives = "";
      String secondElectives = "";
      if (pos != -1) {
        firstElectives = outstandingElectives.substring(0, pos+1);
        secondElectives = outstandingElectives.substring(pos+1, outstandingElectives.length());
        secondElectives = "\t" + secondElectives;
        document.add(new Paragraph(firstElectives));
        Paragraph sE = new Paragraph();
        sE.add(new Tab());
        sE.add(secondElectives);
        document.add(sE);
      } else {
        document.add(new Paragraph(outstandingElectives));
      }

      pos = outstandingCourses.indexOf(":", 0);
      String firstCourses = "";
      String secondCourses = "";
      if (pos != -1) {
        firstCourses = outstandingCourses.substring(0, pos+1);
        secondCourses = outstandingCourses.substring(pos+1, outstandingCourses.length());
        secondCourses = "\t" + secondCourses;
        document.add(new Paragraph(firstCourses));
        Paragraph sCour = new Paragraph();
        sCour.add(new Tab());
        sCour.add(secondCourses);
        document.add(sCour);
      } else {
        document.add(new Paragraph(outstandingCourses));
      }

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
      for (DegreePlan degreePlan : degreePlans) {
        if (degreePlan.getDPname().equals(input))
          return true;
      }
      return false;
    }

    public static DegreePlan getDPbyName(String input, ArrayList<DegreePlan> degreePlans) {
      for (DegreePlan degreePlan : degreePlans) {
        if (degreePlan.getDPname().equals(input))
          return degreePlan;
      }
      return null;
    }

    public static boolean parseElectiveInput(String input) {
      try {
        String[] inputList = input.split(" ");
        inputList[0] = inputList[0].toUpperCase();
        if((inputList[0].equals("CS") || inputList[0].equals("SE")) && (Integer.valueOf(inputList[1]) > 5000 && Integer.valueOf(inputList[1]) < 7000))
          return true;
        else 
          return false;

      } catch (Exception e) {
        return false;
      }
    }
    public static boolean parsePreReqsInput(String input, Student student, ArrayList<Course> levelingCourses) {
      if(input.equals("")) {
        ArrayList<Course> empty = new ArrayList<Course>();
        student.getDegreePlan().setPreRequisiteCourses(empty);
        return true;
      }
      try {
        String[] preReqs = input.split(",");
        ArrayList<Course> coursesToKeep = new ArrayList<Course>();
        for (String preReq : preReqs) {
          //Needs to add check to see if the course already is taken
          int position = Integer.valueOf(preReq);
          coursesToKeep.add(levelingCourses.get(position));
        }
        student.setPreReqCourses(coursesToKeep);
        return true;
      } catch (Exception e) {
        return false;
      }
    }


    //Initializes the list of all Degree Plans and returns it
    public static ArrayList<DegreePlan> initializeDegreePlans() {
      ArrayList<DegreePlan> degreePlans = new ArrayList<DegreePlan>();

      //Initializing the Degree Plans
      DegreePlan SoftwareEngineering = new DegreePlan("Software Engineering", setSECoreCourses(), 15.0, setSEpreReq());
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

    public static ArrayList<Course> initalizeLevelingCourses() {
      ArrayList<Course> levelingCourses = new ArrayList<Course>();
      levelingCourses.add(new Course("CS", 3341, "Probablility and Statistics in Computer Science"));
      levelingCourses.add(new Course("CS", 5303,"Computer Science 1"));
      levelingCourses.add(new Course("CS", 5330, "Computer Science 2"));
      levelingCourses.add(new Course("CS", 5333,"Discrete Structures"));
      levelingCourses.add(new Course("CS", 5343, "Algorithm Analysis & Data Structures"));
      levelingCourses.add(new Course("CS", 5348, "Operating Systems Concepts"));
      levelingCourses.add(new Course("CS", 5349,"Automata Theory"));
      levelingCourses.add(new Course("CS", 5354, "Software Engineering"));
      levelingCourses.add(new Course("CS", 5390, "Computer Networks"));
      return levelingCourses;
    }

}
