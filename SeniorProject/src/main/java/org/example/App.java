package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

//just tests the transcript reader on a specified pdf

public class App 
{
    public static void main( String[] args )
    {
        Student hold = TranscriptReader.parsePDF("C.pdf");

        try {
            FileWriter myWriter = new FileWriter("filename.txt");
            myWriter.write(hold.toString());
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
          
          
          //System.out.println("\n\n\n" + hold.toString());
          saveStudent(hold, hold.getName());
          hold = loadStudent(hold.getName());
    }

    public static void saveStudent(Student student, String fileName) {
      //Saves the student object as a file in the savedStudents folder
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

    public static Student loadStudent(String fileName) {
        //Loads a student object from a file in the savedStudents folder
        Student student = new Student();
        ArrayList<Course> courses = new ArrayList<Course>();
        File myFile = new File("savedStudents\\" + fileName + ".txt");
        try {
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

        System.out.println(student.toString());
        return student;
    }

}
