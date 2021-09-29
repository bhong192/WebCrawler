package com.webcrawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.HashMap;
import java.io.FileWriter;
<<<<<<< HEAD
import java.io.File;
=======
import java.net.URLEncoder;
>>>>>>> 41a57278e3bf99361a49902b0afaf10ce4a241f8



public class App 
{
    
    private static final int MAX_DEPTH = 2;
    //private HashSet<String> links; 
    private HashMap<String, Integer> links; 

    public App()
    {
        links = new HashMap<String, Integer>();
    }

    public void getPageLinks(String URL, int depth){

        if(!links.containsKey(URL) && (depth < MAX_DEPTH)){
            System.out.println(">> Depth: " + depth + " [" + URL + "]");
            
            try{

                Document document = Jsoup.connect(URL).get(); 
                Elements linksOnPage = document.select("a[href]"); 

                depth++;

                Connection.Response response = Jsoup.connect(URL).execute();
                Document doc = response.parse();

                for(Element page: linksOnPage){
                    getPageLinks(page.attr("abs:href"), depth);
                }

                String path = "./repository/";
                FileWriter writer; 

                File directory_check = new File (path);

                if (!directory_check.isDirectory()){
                    directory_check.mkdir();
                }

                writer = new FileWriter(path + document.title() + ".html", true);
                writer.write(doc.outerHtml());
                writer.close();
                System.out.println("Wrote to the file!");

            } catch(IOException e){
                System.err.println("For '" + URL + "':" + e.getMessage());
            }

        }

<<<<<<< HEAD
    }
=======
    public String getLang(String sampleText){
        String language= ""; 

        String languageLayerEndPoint = "http://api.languagelayer.com/detect?access_key=%s&query=%s";
        String APIKey= "1ca250f0a2bbcb6ed2da2a9e17e0b720";

        String newSampleText = "";

        try{
            newSampleText = URLEncoder.encode(sampleText, "UTF-8");
        } catch(Exception e) {
            e.getMessage();
        }

        
        

        return language; 
    }

    /*public void writeToFile(String filename){
        Filewriter writer;
        try {
            writer = new FileWriter(filename); 
            //articles.forEach\
            String temp = " - Title: " + a.get(0) + " (link: " + a.get(1) + ")\n"; 
        }
        catch 
>>>>>>> 41a57278e3bf99361a49902b0afaf10ce4a241f8

    public static void main( String[] args )
    {
        new App().getPageLinks("https://mkyong.com/", 0);
    }
}
