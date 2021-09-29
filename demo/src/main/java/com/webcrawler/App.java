package com.webcrawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.io.FileWriter;



public class App 
{
    
    private static final int MAX_DEPTH = 2;
    private HashSet<String> links; 

    public App()
    {
        links = new HashSet<>();
    }

    public void getPageLinks(String URL, int depth){

        if(!links.contains(URL) && (depth < MAX_DEPTH)){
            System.out.println(">> Depth: " + depth + " [" + URL + "]");
            
            try{

                Document document = Jsoup.connect(URL).get(); 
                Elements linksOnPage = document.select("a[href]"); 

                depth++;

                for(Element page: linksOnPage){
                    getPageLinks(page.attr("abs:href"), depth);
                }

            } catch(IOException e){
                System.err.println("For '" + URL + "':" + e.getMessage());
            }

        }
    }

    /*public void writeToFile(String filename){
        Filewriter writer;
        try {
            writer = new FileWriter(filename); 
            //articles.forEach\
            String temp = " - Title: " + a.get(0) + " (link: " + a.get(1) + ")\n"; 
        }
        catch 

    }*/
    public static void main( String[] args )
    {
        new App().getPageLinks("https://www.mkyong.com/", 0);
    }
}
