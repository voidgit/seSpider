package com.company;

import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.util.regex.*;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * Created by serhiy on 3/9/16.
 **/
class seURL implements Comparable<seURL> {
    // Private block
    private boolean retrieved = false;
    private boolean saved = false;
    private String Url = "";
    private int Weight = 0;
    private int Distance = 0;
    private String FileName = "";

    // Default Constructor
    seURL(String Url, HashMap<String, Integer> filters, int distance){
        this.Url = Url;
        this.Distance = distance;
        this.setWeightByFilters(filters);
    }

    // Constructor
    seURL(String Url, int weight, int distance){
        this.Url = Url;
        this.Weight = weight;
        this.Distance = distance;
    }

    @Override       // needed for the HashSet
    public int hashCode() {
        return Url.hashCode();
    }

    @Override       // needed for the HashSet
    public boolean equals(Object obj) {
        return ( obj instanceof seURL && Url.equals(((seURL) obj).getUrl()));
    }

    @Override       // needed for the queue to be sorted automatically
    public int compareTo(seURL o) {
        return o.Weight - Weight;
    }

    public boolean Retrieve(HashMap<String, String> filters){
        try{
            URL website = new URL(Url);
            try {
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileName = URLtoFileName(Url, filters);
                FileOutputStream fos = new FileOutputStream(FileName);
                if (fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE) > 0) {
                    saved = true;
                    retrieved = true;
                }                
            } catch (IOException e) {
                System.err.println("Error: " + e.toString());
            }
        } catch (MalformedURLException e) {
            System.err.println("Error: incorrect URL: "+Url);
        }
        return retrieved;
    }

    public boolean isRetrieved(){ return retrieved; }

    public boolean Save(){ return saved; }

    public boolean isSaved(){ return saved; }

    public String getUrl(){ return Url; }

    public String getFileName() { return FileName; }

    public int getDistance() { return Distance; }

    private void setWeightByFilters(HashMap<String, Integer> filters) {
        filters.keySet().stream()
                .filter(f -> Pattern.matches(f, Url))
                .forEach(f -> Weight += filters.get(f));
        if ( Weight < 0 ) Weight = 0;
    }

    public int getWeight() {
        return Weight;
    }

    public HashSet<String> Parse() {
        HashSet<String> list = new HashSet<>();

        // Try to do real parsing
        try {
            if (saved){
                System.out.print("   Parsing " + FileName);
                File input = new File(FileName);
                Document doc = Jsoup.parse(input, "UTF-8", Url);
                Elements links = doc.getElementsByTag("a");
                for (Element link : links) {
                    String linkHref = link.attr("abs:href");
                    linkHref = linkHref.split("#")[0];      // TODO check whether it is safe
                    list.add(linkHref);
                  //String linkText = link.text();
                  //String text = list.add(linkHref)?" added":" exists";
                  //System.out.println("    Link: " + linkText + " => " + linkHref + text);
                }
                System.out.print(" - ok");
            }
        } catch ( Exception e ){
            System.err.println( "Error: " + e.toString() );
        } finally {
            System.out.println();
        }
        return list;
    }


    private String URLtoFileName(String Url, HashMap<String, String> filters){
        for (String f : filters.keySet())
            Url = Url.replaceAll(f, filters.get(f));
        //filters.keySet().stream().forEach(f -> Url.replaceAll(f, filters.get(f)));

        if ( Url.split("://").length > 0 )
            Url = Url.split("://")[1];
        //System.out.println("\nURL = " + Url);

        String path = "tmp/" + String.join("/", Arrays.copyOfRange(Url.split("/"), 0, Url.split("/").length-1));
        File p = new File(path);
        if ( ! p.exists())
            if (p.mkdirs()){
                System.out.println("\nCreated path " + path);
            } else {
                System.err.println("\nError: could not create path " + path);
            }
      /*String[] names = Url.split("/");
        String path = "tmp/"+names[0];
        for (int i=1; i<names.length; i++){
            File p = new File(path);
            if ( ! p.exists() )
                if (p.mkdir()){
                    System.out.println("Created path " + path);
                } else {
                    System.err.println("Error: could not create path " + path);
                }
            path += "/"+names[i];
            //System.out.println("Next Path = " + path);
        }*/
        return "tmp/" + Url;
    }

    public boolean getSaved() {
        return saved;
    }
}
