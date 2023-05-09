package org.example;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TabAlignment;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.FileNotFoundException;

public class AuditReportGenerator {
    public static final String[] DATA = {
            "Taylor Swift", "2021012398",
            "Master", "Computer Science",
            "Data Science"
    };

    public static final String[] GPA_Data = {
            "4.000", "3.918", "3.963"
    };

    public static final String[] Core_Courses = {
            "CS 6313", "CS 6350", "CS 6363", "CS 6375", "CS 6360"
    };

    public static final String[] Electives = {
            "CS 5343", "CS 6301", "CS 6314", "CS 6320", "CS 6364", "CS 6385"
    };

    public static String[] AdmissionLetter;

    public AuditReportGenerator(String[] DATA, String[] GPA_Data, String[] Core_Courses, String[] Electives) throws FileNotFoundException {
        String path = "f:\\FirstPdf.pdf";
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
        document.add(createParagraphWithTab("Name: ","ID: ", DATA[0], DATA[1]));
        document.add(createParagraphWithTab("Plan: ","Major: ", DATA[2], DATA[3]));
        document.add(createParagraphWithTab("","Track: ", "", DATA[4]));

        //Creating the GPA Info
        document.add(new Paragraph("\n"));
        document.add(createDataEntry("Core GPA: ",GPA_Data[0]));
        document.add(createDataEntry("Elective GPA: ",GPA_Data[1]));
        document.add(createDataEntry("Combined GPA: ",GPA_Data[2]));

        //Display the Courses
        document.add(new Paragraph("\n"));
        document.add(displayCourses("Core",Core_Courses));
        document.add(displayCourses("Elective", Electives));

        //Pre-Req
        document.add(new Paragraph("\n"));

        document.add(new Paragraph().add(new Text("Leveling Courses and Pre-requisites from Admission Letter:").setBold()));
        document.add(diplayAdmissionLetter(AdmissionLetter));

        //Outstanding Requirements
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Outstanding Requirements: ").setBold());
        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Core completed"));
        document.add(new Paragraph("To maintain a 3.0 elective GPA: "));
        Paragraph req = new Paragraph().add(new Tab());
        document.add(req.add("The student must pass: "));
        document.add(new Paragraph("To maintain a 3.0 overall GPA: "));
        Paragraph req2 = new Paragraph().add(new Tab());
        document.add(req2.add("The student must pass: "));



        Paragraph paragraph = new Paragraph();
        document.add(paragraph);
        document.close();

        System.out.println("Hello world!");
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

    public static Paragraph displayCourses(String type, String[] list){
        Paragraph p = new Paragraph();
        Text label = new Text(type + " Course: ").setBold();
        p.add(label);

        for (int i = 0; i < list.length; i++) {
            if(i == list.length - 1)
                p.add(list[i]);
            else
                p.add(list[i] + ", ");
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
