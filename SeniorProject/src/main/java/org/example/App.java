package com.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class App 
{
    public static void main( String[] args )
    {
        Student hold = TranscriptReader.parsePDF("C.pdf");

        try {
            FileWriter myWriter = new FileWriter("filename.txt");
            myWriter.write(hold.printOut());
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
          System.out.println("\n\n\n" + hold.printOut());
    }
}
