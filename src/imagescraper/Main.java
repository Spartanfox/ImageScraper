/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagescraper;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Ben
 */
public class Main {
    static ArrayList<ImageScraper> scrapies = new ArrayList<>();
    static ImageScraper markedForDelete;
    public static void main(String[] arg) {
        try{
            //addUrl("http://boards.4chan.org/h/thread/4831079");
            addUrl("http://boards.4chan.org/e/thread/2034782");
            addUrl("http://boards.4chan.org/e/thread/2141406");
            //scrapies.add(new ImageScraper("dilly","http://boards.4chan.org/b/thread/758969576"));
            while(true){
                for(ImageScraper scrape : scrapies){
                    if(!scrape.scrape())
                        markedForDelete = scrape;
                }
                if(markedForDelete != null){
                    scrapies.remove(scrapies.indexOf(markedForDelete));
                    System.out.println("Removed dead link");
                    markedForDelete = null;
                }
                if(scrapies.size()==0){
                    System.out.println("All links are dead closing");
                    System.exit(0);
                }
            }
        }catch(Exception e){
            System.err.println(e);
        }
    }
    public static void addUrl(String url)throws Exception{
       // scrapies.add(new ImageScraper(url.substring(url.length()-8,url.length()-1),url));
        scrapies.add(new ImageScraper("ecchi",url));
    }
}
