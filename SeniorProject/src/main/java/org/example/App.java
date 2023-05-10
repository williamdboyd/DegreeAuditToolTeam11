package org.example;


import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TabAlignment;
import com.itextpdf.layout.properties.TextAlignment;


import java.io.FileNotFoundException;
import java.io.File;
import java.nio.file.*;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
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
        System.out.println("Please enter an option:\nWould you like to...\n1: Start a degree plan\n2: Perform a Audit\n");
        String input = sc.nextLine();
        
        if(input.equalsIgnoreCase("Audit") || input.equals("2")) {
          doingAudit = true;
          invalidInput = false;
        } else if (input.equalsIgnoreCase("Planning") || input.equals("1") || input.equalsIgnoreCase("Plan") || input.equalsIgnoreCase("Degree Plan") || input.equalsIgnoreCase("Degree")) {
          doingPlanning = true;
          invalidInput = false;
        } else {
          System.out.println("Invalid input");
        }
      }
      invalidInput = true;
      while(invalidInput) {
        System.out.println("Would you like to load a student or create a new one?\n1: Load\n2-Create\n");
        String input = sc.nextLine();
        if(input.equalsIgnoreCase("Load") || input.equals("1")) {
		  boolean nameInput = true;
		  while(nameInput) {
            System.out.println("Please enter the full name of the student you would like to load: ");
            String name = sc.nextLine();
            student = loadStudent(name, degreePlans);
            if (student == null) {
              System.out.println("Student not found");
            } else{ 
              student.initializeCourses();
			  nameInput = false;
              invalidInput = false; 
            }
		  }
        } else if (input.equalsIgnoreCase("Create") || input.equals("2")) {
          boolean pdfInput = true;
          while(pdfInput){
            System.out.println("Please enter the name of the transcript file you would like to use. EX:(ExampleTranscript.pdf): ");
            String filePath = sc.nextLine();
			if (!filepath.endsWith(".pdf")) { //If the filename does not have .pdf at the end, add it.
				filepath.concat(".pdf");
			  }
            student = TranscriptReader.parsePDF(filePath);
            if (student == null) {
              System.out.println("File not found");
            }
            else{
              pdfInput = false;
            }
          }
          boolean DPInput = true;
          System.out.println("Please enter the name of the degree plan you would like to use: \nFor the following degree plans, select the number corrosponding to the one you wish to choose:\n1: Software Engineering\n2: Data Science\n3: Systems\n4: Cyber Security\n5: Intelligent Systems\n6: Interactive Computing\n7: Traditional CS\n8: Networks and Telecommunication");
          while(DPInput) {
            DPInput = false;
            String DPname = sc.nextLine();
            if(DPname.equals("1")){
              DP = getDPbyName("Software Engineering", degreePlans);
            }
            else if(DPname.equals("2")){
              DP = getDPbyName("Data Science", degreePlans);
            }
            else if(DPname.equals("3")){
              DP = getDPbyName("Systems", degreePlans);
            }
            else if(DPname.equals("4")){
              DP = getDPbyName("Cyber Security", degreePlans);
            }
            else if(DPname.equals("5")){
              DP = getDPbyName("Intelligent Systems", degreePlans);
            }
            else if(DPname.equals("6")){
              DP = getDPbyName("Interactive Computing", degreePlans);
            }
            else if(DPname.equals("7")){
              DP = getDPbyName("Traditional CS", degreePlans);
            }
            else if(DPname.equals("8")){
              DP = getDPbyName("Networks and Telecommunication", degreePlans);
            }
            else{
              System.out.println("Degree Plan not found, please try again: ");
              DPInput = true;
            }
            student.setDegreePlan(DP);
          }
          // System.out.println("Before init student Courses: ");
          student.initializeCourses();
          // System.out.println("After init sutdent courses:" + DP.getCoreCourses().toString());

          invalidInput = false;

          boolean nInput = true;
          System.out.println("Will this student be fast track, thesis, neither, or both?\n1: Fast Track\n2: Thesis\n3: Neither\n4: Both\n");
          while(nInput) {
            String FT = sc.nextLine();
            if(FT.equalsIgnoreCase("Fast") || FT.equals("1")) {
              student.setThesis(false);
              student.setFastTrack(true);
              nInput = false;
            } else if (FT.equalsIgnoreCase("Thesis") || FT.equals("2")) {
              student.setThesis(true);
              student.setFastTrack(false);
              nInput = false;
            } else if (FT.equalsIgnoreCase("Neither") || FT.equals("3")) {
              student.setFastTrack(false);
              student.setThesis(false);
              nInput = false;
            } else if (FT.equalsIgnoreCase("Both") || FT.equals("4")) {
              student.setFastTrack(true);
              student.setThesis(true);
              nInput = false;
            } else {
              System.out.println("Invalid input, Try again:");
            }
          }
          
        } else {
          System.out.println("Invalid input");
        }
        boolean nestedInput = true;
            int count = 0;
            System.out.println("The following is a list of Pre-Req or leveling courses: ");

            for(Course course : levelingCourses) {
              System.out.println(String.valueOf(count) + ". " + course.toString());
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
  
        System.out.println("Would you like to edit this degree plan? (Y/N):");
        String input = sc.nextLine();
        if (input.equalsIgnoreCase("Y")) {
          boolean nestedInput = true;
          while(nestedInput) {
            System.out.println("Would you like to change a course? If not enter Cancel.\n1: Move\n2: Delete\n3: Cancel\n");
            String operation = sc.nextLine();
            if (operation.equalsIgnoreCase("Add")) {
              //System.out.println("Please enter the course you would like to add EX:(CS 63** Artificial Intelligence A-) ");
            } else if (operation.equalsIgnoreCase("Delete") || operation.equals("2")) {
              System.out.println("Please enter the course you would like to delete EX:(prereq CS 63**) ");
              boolean doubleNestedInput = true;
              while(doubleNestedInput) {
                String deleteCourse = sc.nextLine();
                if(deleteCourse.equalsIgnoreCase("Cancel") || operation.equals("3")) {
                  doubleNestedInput = false;
                } else if(student.deleteCourse(deleteCourse)) {
                  System.out.println("Course deleted successfully");
                  doubleNestedInput = false;
                } else {
                  System.out.println("Invalid input, Try again:");
                }
              }
            } else if (operation.equalsIgnoreCase("Move") || operation.equals("1")) {
              System.out.println("Please enter the EXISTING course you would like to move EX:(CS 5343 prereq to elective):");
              boolean doubleNestedInput = true;
              while(doubleNestedInput) {
                String moveCourse = sc.nextLine();
                if(moveCourse.equalsIgnoreCase("Cancel") || operation.equals("3")) {
                  doubleNestedInput = false;
                } else if(student.moveCourse(moveCourse)) {
                  System.out.println("Course moved successfully");
                  doubleNestedInput = false;
                } else {
                  System.out.println("Invalid input, Try again:");
                }
              }
              nestedInput = false;
            } else if (operation.equalsIgnoreCase("Cancel")) {
              nestedInput = false;
            } else {
              System.out.println("Invalid input, Try again:");
            }
          }
        } else if (input.equalsIgnoreCase("N")) {
          validInput = false;
        } else {
          System.out.println("Invalid input, Try again:");
        }
      }

      student.calculateCumGPA();
      validInput = true;
      System.out.println("Would you like to save this student to a local file? (Y/N):");
      while(validInput) {
        String input = sc.nextLine();
        if (input.equalsIgnoreCase("Y")) {
          saveStudent(student, student.getName());
          validInput = false;
        } else if (input.equalsIgnoreCase("N")) {
          validInput = false;
        } else {
          System.out.println("Invalid input, Try again:");
        }
      }

      validInput = true;
      
      System.out.println("Would you like to generate a printable degree plan? (Y/N):");
      while(validInput) {
        String input = sc.nextLine();
        if (input.equalsIgnoreCase("Y")) {
          createReport(student, student.degreePlan);
          validInput = false;
        } else if (input.equalsIgnoreCase("N")) {
          validInput = false;
        } else {
          System.out.println("Invalid input, Try again:");
        }
      }

      System.out.println("Would you like to continue Audit?(Y/N):");
      validInput = true;
      while(validInput) {
        String input = sc.nextLine();
        if (input.equalsIgnoreCase("Y")) {
          validInput = false;
        } else if (input.equalsIgnoreCase("N")) {
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
        if (input.equalsIgnoreCase("Y")) {
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
        } else if (input.equalsIgnoreCase("N")) {
          validInput = false;
        } else {
          System.out.println("Invalid input, try again:");
        }
      }

      System.out.println("Will the student be taking additional graduate courses?(Y/N):");
      validInput = true;
      while(validInput) {
        String input = sc.nextLine();
        if(input.equalsIgnoreCase("Y")){
			
        } else if (input.equalsIgnoreCase("N")) {
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
        boolean DSInput = true;
        while(DSInput){
          DSInput = false;
          System.out.println("\nWhat is the disposition of " + course.toString() + "?\n Select the number corrosponding to an option below:\n1: Completed\n2: Waived\n3: Not required by plan or electives\n4: Other(type in)");
          String input = sc.nextLine();
          if(input.equals("1")){
            String semester = "";
            while(semester.equals("")){
              System.out.println("Enter the semester of completion");
              semester = sc.nextLine();
            }
            course.setDescription("Completed: " + semester + ": ");
          }
          else if(input.equals("2")){
            System.out.println("Type any aditional info. If there is none, just hit enter.");
            String extra = sc.nextLine();
            course.setDescription("Waived" + extra);
          }
          else if(input.equals("3")){
            course.setDescription("Not required by plan or electives");
          }
          else if(input.equals("4")){
            System.out.println("Enter the disposition info.");
            String extra = sc.nextLine();
            if(extra.equals("")){
              System.out.println("This field cannot be left blank.");
              DSInput = true;
              continue;
            }
            course.setDescription(extra);
          }
          else{
            System.out.println("Invalid input");
            DSInput = true;
          }
        }
      }
      

      System.out.println("Would you like to generate and save a printable audit? (Y/N):");
      validInput = true;
      while(validInput) {
        String input = sc.nextLine();
        if(input.equalsIgnoreCase("Y")) {
          try {
            createAudit(student);
          } catch(IOException e) {
            System.out.println("Couldn't create Audit");
          }
          validInput = false;
        } else if (input.equalsIgnoreCase("N")){
          validInput = false;
        } else {
          System.out.println("Invalid input, try again:");
        }
      }

      sc.close();
    }

    //Saves the student object as a file in the savedStudents folder
    public static void saveStudent(Student student, String fileName) {
	  try {
			Files.createDirectories(Paths.get("savedStudents\\"));
		} catch (IOException e) {
			//Everything's fine I hope
		}
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
      document.add(createParagraphWithTab("Plan: ","Major: ", "Master", student.getMajor()));
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
        if (course.getGrade() == "none") {
          document.add(new Paragraph(course.toString()));
        } else {
          document.add(new Paragraph(course.toString() + course.getGrade()));
        }
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
        List<Integer> didntMakeIt = new ArrayList<Integer>(); 
        for (String preReq : preReqs) {
          //Needs to add check to see if the course already is taken
          int position = Integer.valueOf(preReq);
          boolean added = false;
          //coursesToKeep.add(levelingCourses.get(position));
          for (Course course : student.getPreReqCourses()) {
            if (course.isEqual(levelingCourses.get(position))){
              coursesToKeep.add(course);
              added = true;
            }
          }
          if(!didntMakeIt.contains(position) && !added) {
            didntMakeIt.add(position);
          }
        }

        for (Integer integer : didntMakeIt) {
          coursesToKeep.add(levelingCourses.get(integer));
        }
        
        student.setPreReqCourses(coursesToKeep);
        return true;
      } catch (Exception e) {
        return false;
      }
    }

    public static void createReport(Student passStudent, DegreePlan plan) {
        Student student = passStudent;
        String path = student.getName() + ".pdf";
        PdfWriter pdfWriter = null;
        try {
            pdfWriter = new PdfWriter(path);
        } catch(Exception e) {
            System.out.println("Error creating PDF");
        }
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.addNewPage();
        Document document = new Document(pdfDocument);

        //Creating Title of the PDF
        Paragraph heading = new Paragraph();
        Text title = new Text("DEGREE PLAN");
        Text title2 = new Text("\nUNIVERSITY OF DALLAS");
        Text title3 = new Text("\nMASTER OF " + student.getMajor().toUpperCase());
        heading.add(title);
        heading.add(title2);
        heading.add(title3);
        heading.setTextAlignment(TextAlignment.CENTER); // aligning to the Center
        heading.setBold();
        heading.setFontSize(12);
        document.add(heading);



        //Creating the General Student Info
        document.add(createEntry("Name of Student", student.getName()));
        document.add(createEntry("Student I.D Number", Integer.toString(student.getID())));
        document.add(createEntry("Semester Admitted to Program", student.getAdmittedDate()));
        document.add(createEntry("Anticipated Graduation", student.getAnticipatedGraduation()));


        //Table Header
        Table table = new Table(new float[] {350f,100f,100f,100f,100f});
        table.addCell("Course Title").setTextAlignment(TextAlignment.CENTER).setBold();
        table.addCell("Course Number").setTextAlignment(TextAlignment.CENTER).setBold();
        table.addCell("UTD Semester").setTextAlignment(TextAlignment.CENTER).setBold();
        table.addCell("Transfer").setTextAlignment(TextAlignment.CENTER).setBold();
        table.addCell("Grade").setTextAlignment(TextAlignment.CENTER).setBold();
        table.startNewRow();
        document.add(table);

        //First red label
        Table table2 = new Table(new float[] {1050f});
        com.itextpdf.kernel.colors.Color myColor = new DeviceRgb(255, 100, 20);
        table2.addCell("CORE COURSES \t "
                 + "(15 Credit Hours)"
                 +" \t "
                 + "3.19"
                 +" Grade Point Average Required").setTextAlignment(TextAlignment.CENTER).setBold().setBackgroundColor(myColor,0.25f);
        document.add(table2);

        //
        Table table3 = new Table(new float[] {350f,100f,100f,100f,100f}).setFontSize(10);
        for (int i = 0; i < 5; i++) {
            if(i < plan.getCoreCourses().size()) {

                //Course Description
                table3.addCell(plan.getCoreCourses().get(i).getDescription());
                //Course Name
                table3.addCell(plan.getCoreCourses().get(i).getPrefix() + plan.getCoreCourses().get(i).getNumber());

                //Checking if student took the course
                if (plan.getCoreCourses().get(i).getSemesterTaken() != null) {
                    table3.addCell(plan.getCoreCourses().get(i).getSemesterTaken());//Course Semester Take
                    table3.addCell(String.valueOf(plan.getCoreCourses().get(i).creds));//Course Credits
                    table3.addCell(plan.getCoreCourses().get(i).getGrade());//Course Grades
                } else {
                    table3.addCell("");
                    table3.addCell("");
                    table3.addCell("");
                }
            }else{
                table3.addCell("");
                table3.addCell("");
                table3.addCell("");
                table3.addCell("");
                table3.addCell("");
            }

            table3.startNewRow();
        }
        table3.startNewRow();
        document.add(table3);

        Table table4 = new Table(new float[] {1050f});
        table4.addCell("One of the following Five Core Course").setTextAlignment(TextAlignment.CENTER).setBold().setBackgroundColor(myColor,0.25f);
        document.add(table4);


        Table table5 = new Table(new float[] {350f,100f,100f,100f,100f});
        for (int i = 0; i < 6; i++) {
            if (5 + i < plan.getCoreCourses().size()){
                //Course Description
                table5.addCell(plan.getCoreCourses().get(5 + i).getDescription());
                //Course Name
                table5.addCell(plan.getCoreCourses().get(5 + i).getPrefix() + plan.getCoreCourses().get(i).getNumber());

                //Checking if student took the course
                if (plan.getCoreCourses().get(5 + i).getSemesterTaken() != null) {
                    table5.addCell(plan.getCoreCourses().get(5 + i).getSemesterTaken());//Course Semester Take
                    table5.addCell(String.valueOf(plan.getCoreCourses().get(5 + i).creds));//Course Credits
                    table5.addCell(plan.getCoreCourses().get(5 + i).getGrade());//Course Grades
                } else {
                    table5.addCell(" ");
                    table5.addCell(" ");
                    table5.addCell(" ");
                }

            }else{
                table5.addCell(" ");
                table5.addCell(" ");
                table5.addCell(" ");
                table5.addCell(" ");
                table5.addCell(" ");
            }

            table5.startNewRow();
        }
        table5.startNewRow();
        document.add(table5);

        Table table6 = new Table(new float[] {1050f});
        table6.addCell("FIVE APPROVED 6000 LEVEL ELECTIVES \t (15* Credit Hours) \t 3.0 Grade Point Average").setTextAlignment(TextAlignment.CENTER).setBold().setBackgroundColor(myColor,0.25f);
        document.add(table6);


        Table table7 = new Table(new float[] {350f,100f,100f,100f,100f});

        for (int i = 0; i < 5; i++) {
            //Checking if student took the course
            if (i < student.getElectiveCourses().size()) {
                //Course Description
                table7.addCell(student.getElectiveCourses().get(i).getDescription() );
                //Course Name
                table7.addCell(student.getElectiveCourses().get(i).getPrefix() + student.getElectiveCourses().get(i).getNumber());
                table7.addCell(student.getElectiveCourses().get(i).getSemesterTaken());//Course Semester Take
                table7.addCell(String.valueOf(student.getElectiveCourses().get(i).creds));//Course Credits
                table7.addCell(student.getElectiveCourses().get(i).getGrade());//Course Grades
            }else {
                table7.addCell(" ");
                table7.addCell(" ");
                table7.addCell(" ");
                table7.addCell(" ");
                table7.addCell(" ");
            }

            table7.startNewRow();
        }
        table7.startNewRow();
        document.add(table7);

        Table table8 = new Table(new float[] {1050f});
        table8.addCell("Additional Electives ( 3 Credit Hours minimum)").setTextAlignment(TextAlignment.CENTER).setBold().setBackgroundColor(myColor,0.25f);
        document.add(table8);


        Table table9 = new Table(new float[] {350f,100f,100f,100f,100f});
        for (int i = 0; i < 3; i++) {
            //Checking if student took the course
            if (5 + i < student.getElectiveCourses().size()) {
                table9.addCell(student.getElectiveCourses().get(i).getSemesterTaken());//Course Description
                table9.addCell(student.getElectiveCourses().get(i).getDescription()) ;
                //Course Name
                table9.addCell(student.getElectiveCourses().get(i).getPrefix() + student.getElectiveCourses().get(i).getNumber());
                //Course Semester Take
                table9.addCell(String.valueOf(student.getElectiveCourses().get(i).creds));//Course Credits
                table9.addCell(student.getElectiveCourses().get(i).getGrade());//Course Grades
            }else {
                table9.addCell(" ");
                table9.addCell(" ");
                table9.addCell(" ");
                table9.addCell(" ");
                table9.addCell(" ");
            }
                table9.startNewRow();
        }
        table9.startNewRow();
        document.add(table9);


        Table table10 = new Table(new float[] {1050f});
        table10.addCell("Other Requirements").setTextAlignment(TextAlignment.CENTER).setBold().setBackgroundColor(myColor,0.25f);
        document.add(table10);

        Table table11 = new Table(new float[] {350f,100f,100f,100f,100f});
        for (int i = 0; i < 2; i++) {
            //Checking if student took the course
            if (i < student.getElectiveCourses().size()) {
                table11.addCell(student.getElectiveCourses().get(i).getDescription()) ;
                table11.addCell(student.getElectiveCourses().get(i).getSemesterTaken());//Course Description
                //Course Name
                table11.addCell(student.getElectiveCourses().get(i).getPrefix() + student.getElectiveCourses().get(i).getNumber());
                //Course Semester Take
                table11.addCell(String.valueOf(student.getElectiveCourses().get(i).creds));//Course Credits
                table11.addCell(student.getElectiveCourses().get(i).getGrade());//Course Grades
            }else {
                table11.addCell(" ");
                table11.addCell(" ");
                table11.addCell(" ");
                table11.addCell(" ");
                table11.addCell(" ");
            }
            table11.startNewRow();
        }
        table11.startNewRow();
        document.add(table11);

        Table table12 = new Table(new float[] {350f,100f,100f,100f,100f}).setTextAlignment(TextAlignment.CENTER).setBold().setBackgroundColor(myColor,0.25f);
        table12.addCell("Admission Prerequisites").setBorder(Border.NO_BORDER);
        table12.addCell("Course").setBorder(Border.NO_BORDER);
        table12.addCell("UTD Semester").setBorder(Border.NO_BORDER);
        table12.addCell("Waiver").setBorder(Border.NO_BORDER);
        table12.addCell("Grades").setBorder(Border.NO_BORDER);
        document.add(table12);

        Table table13 = new Table(new float[] {350f,100f,100f,100f,100f});
        for (int i = 0; i < plan.getPreRequisiteCourses().size(); i++) {
            //Course Description
            table13.addCell(plan.getPreRequisiteCourses().get(i).getDescription());
            //Course Name
            table13.addCell(plan.getPreRequisiteCourses().get(i).getPrefix() + plan.getPreRequisiteCourses().get(i).getNumber());

            //Checking if student took the course
            if (plan.getPreRequisiteCourses().get(i).getSemesterTaken() != null) {
                table13.addCell(plan.getPreRequisiteCourses().get(i).getSemesterTaken());//Course Semester Take
                table13.addCell(String.valueOf(plan.getPreRequisiteCourses().get(i).creds));//Course Credits
                table13.addCell(plan.getPreRequisiteCourses().get(i).getGrade());//Course Grades
            }else {
                table13.addCell(" ");
                table13.addCell(" ");
                table13.addCell(" ");
            }

            table13.startNewRow();
        }
        table5.startNewRow();
        document.add(table5);

        document.add(new Paragraph("*May include any 6000 or 7000 level CS course without prior permission").setTextAlignment(TextAlignment.CENTER));

        document.add(createEntry("Academic Advisor", "             "));
        document.add(createEntry("Date Submitted", "             "));


        document.close();
    }

    public static Paragraph createEntry(String key, String value){
        Paragraph p = new Paragraph();
        Paragraph p2 = new Paragraph();
        Text label = new Text(key);
        p.add(label);
        p.add(": ");
        p2.add(value).setUnderline();
        p.setFixedLeading(10);
        p2.setFixedLeading(10);
        p.add(p2);
        return p;
    }


    //Initializes the list of all Degree Plans and returns it
    public static ArrayList<DegreePlan> initializeDegreePlans() {
      ArrayList<DegreePlan> degreePlans = new ArrayList<DegreePlan>();

      ArrayList<Course> SEselectCore = new ArrayList<Course>();
      ArrayList<Course> DSselectCore = new ArrayList<Course>();
      ArrayList<Course> sysSelectCore = new ArrayList<Course>();
      ArrayList<Course> cyberSelectCore = new ArrayList<Course>();
      ArrayList<Course> ISselectCore = new ArrayList<Course>();
      ArrayList<Course> ICselectCore = new ArrayList<Course>();
      ArrayList<Course> TraditionalselectCore = new ArrayList<Course>();
      ArrayList<Course> NTselectCore = new ArrayList<Course>();


      DSselectCore.add(new Course("CS", 6301, "Social Network Analytics"));
      DSselectCore.add(new Course("CS", 6320, "Natural Language Processing"));
      DSselectCore.add(new Course("CS", 6327, "Video Analytics"));
      DSselectCore.add(new Course("CS", 6347, "Statistics for Machine Learning"));
      DSselectCore.add(new Course("CS", 6360, "Database Design"));

      sysSelectCore.add(new Course("CS", 6349, "Network Security"));
      sysSelectCore.add(new Course("CS", 6376, "Parallel Processing"));
      sysSelectCore.add(new Course("CS", 6380, "Distributed Computing"));
      sysSelectCore.add(new Course("CS", 6397, "Synthesis and Optimization of High-Performance Systems"));
      sysSelectCore.add(new Course("CS", 6399, "Parallel Architecture and Systems"));

      ICselectCore.add(new Course("CS", 6323, "Computer Animation and Gaming"));
      ICselectCore.add(new Course("CS", 6328, "Modeling and Simulation"));
      ICselectCore.add(new Course("CS", 6331, "Multimedia Systems"));
      ICselectCore.add(new Course("CS", 6334, "Virtual Reality"));
      ICselectCore.add(new Course("CS", 6366, "Computer Graphics"));

      cyberSelectCore.add(new Course("CS", 6332, "System Security & Malicious Code Analysis"));
      cyberSelectCore.add(new Course("CS", 6348, "Data and Applications Security"));
      cyberSelectCore.add(new Course("CS", 6349, "Network Security"));
      cyberSelectCore.add(new Course("CS", 6377, "Introduction To Cryptography"));

      ISselectCore.add(new Course("CS", 6360, "Database Design"));
      ISselectCore.add(new Course("CS", 6378, "Advanced Operating Systems"));

      TraditionalselectCore.add(new Course("CS", 6353, "Compiler Construction"));
      TraditionalselectCore.add(new Course("CS", 6360, "Database Design"));
      TraditionalselectCore.add(new Course("CS", 6371, "Advanced Programming Languages"));


      //Initializing the Degree Plans
      DegreePlan SoftwareEngineering = new DegreePlan("Software Engineering", setSECoreCourses(), SEselectCore, 15.0, setSEpreReq(), 0);
      DegreePlan DataScience = new DegreePlan("Data Science", setDSCoreCourses(), DSselectCore, 15.0, setDSpreReq(), 1);
      DegreePlan Systems = new DegreePlan("Systems", setSysCoreCourses(), sysSelectCore, 15.0, setSyspreReq(), 1);
      DegreePlan CyberSecurity = new DegreePlan("Cyber Security", setCyberCoreCourses(), cyberSelectCore, 12.0, setCyberpreReq(), 2);
      DegreePlan IntelligentSystems = new DegreePlan("Intelligent Systems", setISCoreCourses(), ISselectCore, 15.0, setISpreReq(), 1);
      DegreePlan InteractiveComputing = new DegreePlan("Interactive Computing", setICCoreCourses(), ICselectCore, 15.0, setICpreReq(), 3);
      DegreePlan TraditionalCS = new DegreePlan("Traditional CS", setTCCoreCourses(), TraditionalselectCore, 15.0, setTCpreReq(), 2);
      DegreePlan NetworksTelecommunication = new DegreePlan("Networks and Telecommunication", setNTCoreCourses(), NTselectCore, 15.0, setNTpreReq(), 0);

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
      return ICCourses;
    }

    public static ArrayList<Course> setTCCoreCourses() {
      ArrayList<Course> TCCourses = new ArrayList<Course>();
      TCCourses.add(new Course("CS", 6363, "Design and Analysis of Computer Algorithms"));
      TCCourses.add(new Course("CS", 6378, "Advanced Operating Systems"));
      TCCourses.add(new Course("CS", 6390, "Advanced Computer Networks"));
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
