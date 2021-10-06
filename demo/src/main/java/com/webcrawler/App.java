package com.webcrawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.File;
import java.io.File;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.lang.model.util.ElementScanner6;

import java.util.regex.Matcher; 
import com.detectlanguage.DetectLanguage;



public class App 
{
    
    private static final int MAX_DEPTH = 3;
    private HashMap<String, Integer> links; 
    private String[] removedTags = {"audio", "button", "img", "input", "nav", "video", "script", "style", "a", "link", "footer", "object", "figure", "track", "noscript", "form" };

    public App()
    {
        links = new HashMap<String, Integer>();
    
    }

    public void getLinks(String URL, int depth) throws IOException{

        if(!links.containsKey(URL) && (depth < MAX_DEPTH)){
            System.out.println(">> Depth: " + depth + " [" + URL + "]");

             
            try{
                Document document = Jsoup.connect(URL).get(); 
                Elements linksOnPage = document.select("a[href]"); 


                int outlinks = Integer.valueOf(linksOnPage.size());

                links.put(URL, outlinks);
                //System.out.println(links);
                /*csvWriter.append(links(i).toString());
                csvWriter.append("\n");
                System.out.println("Wrote to file! pog :)");
                csvWriter.close();*/

                depth++;

                Connection.Response response = Jsoup.connect(URL).execute(); 
                Document doc = response.parse(); 

                for(int i = 0; i < removedTags.length; i++){
                    doc.select(removedTags[i]).remove(); 
                }

                for(Element page: linksOnPage){
                    getLinks(page.attr("abs:href"), depth);
                }
                
                String lang = getLang(document.body().text().substring(0,Math.min(75, document.body().text().length())));
                if (lang == null){
                    lang = "Unknown";
                }

                if(!lang.equals("en")){
                    System.out.println("Not English, it's " + lang);
                }

                System.out.println(lang);

                String path = "./repository/" + lang;
                //String path = "./repository";
                FileWriter writer; 

                File directory_check = new File (path);

                if (!directory_check.isDirectory()){
                    directory_check.mkdir();
                }

                writer = new FileWriter(path + "/" + document.title() + ".html", true);
                writer.write(doc.outerHtml());
                writer.close();
            
            } catch(IOException e){
                System.err.println("For '" + URL + "':" + e.getMessage());
            }
        }

    }

    public HashMap<String, Integer> getPageLinks(){

        return links;

    }
    public String getLang(String sampleText){ 

        //DetectLanguage.apiKey = "0a6eac1e86ea2d44613723b79536da4d";
        //DetectLanguage.apiKey = "790684f253459b059afcda2c71fc6f99"; 
        //DetectLanguage.apiKey = "cfc686471352c74b16fa69a20ec9e854";
        //DetectLanguage.apiKey = "8cce4cd0813737d596ae3ed9c52fdc66";
        //DetectLanguage.apiKey = "46db3a0c729017582c944ee81c3513d1";
        //DetectLanguage.apiKey = "71719821431c712b5b76a0bb48c09490";
        DetectLanguage.apiKey = "c7c0afd2270b4b2922855deb7855c434";


        String newSampleText = "";

        try{
            newSampleText = URLEncoder.encode(sampleText, "UTF-8");
        } catch(Exception e) {
            e.getMessage();
        }

        // need to format a string to use as API request
        String language = "";
        try{            
            language = DetectLanguage.simpleDetect(newSampleText); 
        }catch (Exception e){
            language = "Unknown";
            e.printStackTrace();
            System.out.println(e.getMessage()); 
             
        }

        return language; 
        
    }

    public static void main( String[] args ) throws IOException
    {
        App webCrawler = new App();
        
        webCrawler.getLinks("https://www.yellowpages.com/", 0);

        String pathToCsv = "./report.csv";
        File csvFile = new File(pathToCsv);
        FileWriter csvWriter = new FileWriter(csvFile, true);
       
        HashMap<String, Integer> finishedlinks;
        finishedlinks = webCrawler.getPageLinks();
        
        for(HashMap.Entry<String, Integer> entry: finishedlinks.entrySet()){
            
            csvWriter.append(entry.getKey() + ", " + entry.getValue());
            csvWriter.append("\n");                       
        }

        csvWriter.close();

    }
}