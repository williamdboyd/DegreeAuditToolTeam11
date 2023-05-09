package org.example;


import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TabAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.pdf.colorspace.PdfColorSpace;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.bouncycastle.cert.crmf.ValueDecryptorGenerator;

import javax.sql.RowSet;
import javax.swing.text.TableView;
import java.util.ArrayList;
import java.util.List;


//just tests the transcript reader on a specified pdf

public class App
{
    public static String[] AdmissionLetter;
    public static void main( String[] args ) throws FileNotFoundException {
        //Creating an empty student to be used
        Student student = new Student();
        //Creating the list of degree plans
        ArrayList<DegreePlan> degreePlans = initializeDegreePlans();
        DegreePlan DP = new DegreePlan();

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
                    student.initializeCourses();

                    System.out.println("Pre-Reqs:");
                    for (Course course : student.getPreReqCourses()) {
                        System.out.println(course.toString());
                    }

                    invalidInput = false;
                    boolean nestedInput = true;
                    int count = 0;
                    System.out.println(student.getDegreePlan().getDPname() + " requires the following pre-requisites: ");
                    for(Course course : student.getDegreePlan().getPreRequisiteCourses()) {
                        System.out.println(String.valueOf(count) + "." + course.toString());
                        count += 1;
                    }
                    System.out.println("Select Pre-Reqs to be included or leave empty if none. EX:(0,1,3):");
                    while (nestedInput) {
                        String preReqs = sc.nextLine();
                        if(parsePreReqsInput(preReqs, student)) {
                            nestedInput = false;
                        } else {
                            System.out.println("Invalid input, please try again:");
                        }
                    }

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
                System.out.println("Would you like to move a course? If not enter Cancel.(Move/Cancel):");
                while(nestedInput) {
                    String operation = sc.nextLine();
                    if (operation.equals("Add")) {
                        //System.out.println("Please enter the course you would like to add EX:(CS 63** Artificial Intelligence A-) ");
                    } else if (operation.equals("Delete")) {
                        //
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
                createReport(student, student.degreePlan);
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

        for (Course course: DisposedPreReqs) {
            System.out.println(course.toString());
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
        document.add(diplayAdmissionLetter(AdmissionLetter));

        //Outstanding Requirements
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Outstanding Requirements").setBold());



        Paragraph paragraph = new Paragraph();
        document.add(paragraph);
        document.close();
    }

    public static void createReport(Student passStudent, DegreePlan plan) throws FileNotFoundException {
        Student student = passStudent;
        String path = "FirstReport.pdf";
        PdfWriter pdfWriter = new PdfWriter(path);
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
                table7.addCell(student.getElectiveCourses().get(i).getPrefix() + plan.getCoreCourses().get(i).getNumber());
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
                table9.addCell(student.getElectiveCourses().get(i).getPrefix() + plan.getCoreCourses().get(i).getNumber());
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
                table11.addCell(student.getElectiveCourses().get(i).getPrefix() + plan.getCoreCourses().get(i).getNumber());
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
            table13.addCell(plan.getPreRequisiteCourses().get(i).getPrefix() + plan.getCoreCourses().get(i).getNumber());

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
    public static boolean parsePreReqsInput(String input, Student student) {
        if(input.equals("")) {
            ArrayList<Course> empty = new ArrayList<Course>();
            student.getDegreePlan().setPreRequisiteCourses(empty);
            return true;
        }
        try {
            String[] preReqs = input.split(",");
            ArrayList<Course> coursesToKeep = new ArrayList<Course>();
            for (String preReq : preReqs) {
                int position = Integer.valueOf(preReq);
                coursesToKeep.add(student.getDegreePlan().getPreRequisiteCourses().get(position));
            }
            student.getDegreePlan().setPreRequisiteCourses(coursesToKeep);
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

}
