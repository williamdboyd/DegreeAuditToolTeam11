package com.example;

import java.io.FileWriter;
import java.io.IOException;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class TranscriptReader 
{
    public static Student parsePDF(String file){
        String output = "";
        String textFromPage = "";
        int count = 1;
        PdfReader p;
        try {
            p = new PdfReader(file);

            while(true){
                try {
                    textFromPage = PdfTextExtractor.getTextFromPage(p, count);
            
                    //System.out.println(textFromPage);
                    output = output + textFromPage;
                    count++;
                }
                catch (NullPointerException e)
                {
                    e.printStackTrace();
                    break;
                }
            }
            p.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String line;
        String output2 = output;
        int countL = 0;

        String sem = "";
        String fsem;
        boolean masterf = false;
        String n;
        Student hold = new Student();


        for(int i=0; i < output.length(); i++)
        {    if(output.charAt(i) == '\n')
                countL++;
        }
        
        for(int i = 0; i < countL; i++){
            line = output2.substring(0, output2.indexOf("\n"));
            output2 = output2.substring(output2.indexOf("\n") + 1);
            if(line.indexOf("202") == 0){
                if(line.contains("Fall") || line.contains("Spring") || line.contains("Summer")){
                    sem = line;
                    //System.out.println(sem + " is the semester");
                    if(masterf){
                        hold.setSem1(sem);
                        //System.out.println("This is the first semester");
                        fsem = sem;
                        masterf = false;
                    }
                    //Make sure fall spring and summer are the only types of semester
                }
            }
            if(line.indexOf("Name:") == 0){
                char h = line.charAt(5);
                int hh = 5;
                while(h == ' '){
                    hh++;
                    h = line.charAt(hh);
                }
                n = line.substring(hh);
                hold.setName(n);
                //System.out.println(n + " is the name");
            }
            if(line.indexOf("Program: Master") == 0){
                masterf = true;
            }
            if(line.contains("Major")){
                int hh = 0;
                char h = ' ';
                while(!Character.isLetter(h)){
                    hh++;
                    if(hh >= line.length()){
                        break;
                    }
                    h = line.charAt(hh);
                }
                hold.setMajor(line.substring(hh, line.indexOf("Major")));
            }
            if(line.contains("Combined Cum")){
                hold.setCcGPA(Float.parseFloat(line.substring(17,22)));
                //System.out.println("GPA is " + hold.getCcGPA());
            }
            if(line.contains("Student ID:")){
                hold.setID(Integer.parseInt(line.substring(line.lastIndexOf(" ") + 1, line.lastIndexOf(" ") + 11)));
                //make sure all ids are 10 length
                //System.out.println(hold.getID());
            }
            if(line.indexOf("CS") == 0 || line.indexOf("CE") == 0){
                if(Integer.parseInt(line.substring(3, 4)) > 4){
                    //System.out.println("This is a  course");
                    hold.addCourse(parseCourse(line, sem));
                }
            }
            //System.out.println(line + "\n");
        }
        return hold;
    }

    public static Course parseCourse(String line, String sem){
        Course ret = new Course();
        ret.setSemester(sem);
        ret.setPrefix(line.substring(0, line.indexOf(" ")));
        //System.out.println(ret.getPrefix() + " is the pre");
        line = line.substring(line.indexOf(" ") + 1);
        ret.setNumber(Integer.parseInt(line.substring(0, line.indexOf(" "))));
        line = line.substring(line.indexOf(" ") + 1);
        //System.out.println(ret.getNumber());
        int hh = 0;
        char h = ' ';
        while(!Character.isDigit(h)){
            hh++;
            h = line.charAt(hh);
        }
        ret.setDescription(line.substring(0, hh - 1));
        //System.out.println(ret.getDescription() + " is desc");
        line = line.substring(hh);
        ret.setCreds(Float.parseFloat(line.substring(0, 5)));
        //System.out.println(ret.getCreds());
        hh = 0;
        h = '0';
        while(!Character.isLetter(h)){
            hh++;
            if(hh >= line.length()){
                ret.setGrade("none");
                break;
            }
            h = line.charAt(hh);
        }
        line = line.substring(hh);
        if(line.length() != 0){
            ret.setGrade(line.substring(0, line.indexOf(" ")));
        }
        //System.out.println(ret.getGrade());
        return ret;
    }
}