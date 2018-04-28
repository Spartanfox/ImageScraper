/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagescraper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector;

public class ImageScraper {
    public static final int MAX_THREADS = 10;
    public static int threads = 0;
    String title;
    String protocol;
    String url;
    ArrayList<String> downloaded;
    ArrayList<String> downloading;
    Document doc;
    public boolean scrape() {
        try{
            //System.out.println("Getting document: "+url);
            doc = Jsoup.connect(url).timeout(0).get();
            Elements links = doc.select("a[href]");
            for(Element link : links){
                String image = link.attr("href");
                if(image.isEmpty()){
                    image = link.attr("src");
                }
                if(image.contains(".png")||image.contains(".jpg")||image.contains(".jpeg")||image.contains(".gif")||image.contains(".webm")){
                    while(threads>=MAX_THREADS){
                        Thread.sleep(1000);
                    }
                    boolean download = true;
                    for(String d : downloading){
                        if(d.equals(image))download = false;
                    }
                    if(download){
                        getImage(image);
                    }
                }
            }
        }catch (Exception e){
            System.err.println(e);
            return false;
        }
        return true;
    }
    public ImageScraper(String file, String url) throws Exception{
        this.url = url;
        if(url.contains("https:")){
            protocol = "";         
        }else{
            protocol = "http:";
        }
        new File(file).mkdir();
        title = file;
        downloading = new ArrayList<String>();
    }
    public boolean getImage(String url){
        File outputfile = new File(title+"/"+url.split("/")[url.split("/").length-1]);
        if(!outputfile.exists()){
            downloading.add(url);
            Thread downloader = new Thread(){
                @Override
                public void run(){
                    System.out.println("Starting thread: "+threads);
                    try {
                        download(protocol+url,outputfile);
                        threads--;
                        System.out.println("Stopping thread: "+threads);

                    } catch (Exception ex) {
                        threads--;
                        System.err.println(ex);
                        System.out.println("Could not download : "+protocol+url);
                    }
                }
         };
         threads++;
         
         downloader.start();
         
         return true;
        }
       return false;
    }
    
        public static boolean download(String urlString, File destination) throws Exception{  
            URL website = new URL(urlString);
            ReadableByteChannel rbc;
            rbc = Channels.newChannel(website.openStream());
            try (FileOutputStream fos = new FileOutputStream(destination)) {
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            }
            rbc.close();
            //System.out.println("Bot: "+ID+" "+urlString);
            System.out.println("Added : "+urlString);

            return true;
    }
    
}
