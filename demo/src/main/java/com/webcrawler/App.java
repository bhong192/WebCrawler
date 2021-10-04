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



public class App 
{
    
    private static final int MAX_DEPTH = 2;
    //public static String titleOne = "URL";
    //public static int titleTwo = 25; 
    private HashMap<String, Integer> links; 
    private String[] removedTags = {"audio", "button", "img", "input", "nav", "video", "script", "style", "a", "link", "footer", "object", "figure", "track", "noscript", "form" };

    public App()
    {
        links = new HashMap<String, Integer>();

        //int titleTwo = 25;
        //int titleTwo = Integer.parseInt(title);
    
    }

    public void getLinks(String URL, int depth) throws IOException{

        //links.put(titleOne, titleTwo);

        if(!links.containsKey(URL) && (depth < MAX_DEPTH)){
            System.out.println(">> Depth: " + depth + " [" + URL + "]");

             
            try{
                Document document = Jsoup.connect(URL).get(); 
                Elements linksOnPage = document.select("a[href]"); 



                int outlinks = Integer.valueOf(linksOnPage.size());
                String pathToCsv = "./report.csv";
                File csvFile = new File(pathToCsv);
                //FileWriter csvWriter = new FileWriter(csvFile, true);

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
                if (!lang.equals("English")){
                    System.out.println("Not English");
                    System.out.println(lang);
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

        String languageLayerEndPoint = "http://api.languagelayer.com/detect?access_key=%s&query=%s";
        String APIKey= "1ca250f0a2bbcb6ed2da2a9e17e0b720";

        String newSampleText = "";

        try{
            newSampleText = URLEncoder.encode(sampleText, "UTF-8");
        } catch(Exception e) {
            e.getMessage();
        }

        // need to format a string to use as API request
        String request = String.format(languageLayerEndPoint, APIKey, newSampleText); 
        String language = "";
        
        try{
            URL url = new URL(request);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
            InputStream is = connection.getInputStream(); 

            String text = null; 
            try(Scanner scanner = new Scanner(is)){
                text = scanner.useDelimiter("\\A").next(); 
            }
            is.close(); 
            connection.disconnect();

            // we have a response
            System.out.println(text); 
            String regex = "\"language_name\":\\s*\"\\w+\""; 
            Pattern pattern = Pattern.compile(regex); 
            Matcher matcher = pattern.matcher(text); 

            while(matcher.find()){
                System.out.println("Start index: " + matcher.start()); 
                System.out.println("End index: " + matcher.end());
                System.out.println("Found: " + matcher.group());
                String lang1 = text.substring(matcher.start(), matcher.end()); 
                language = (lang1.replaceAll("(\"|\\s)","").split(":")[1]);
            }
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage()); 
            language = "Unknown"; 
        }
        return language; 
    }

    public static void main( String[] args ) throws IOException
    {
        App webCrawler = new App();
        
        webCrawler.getLinks("https://www.cpp.edu/", 0);

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