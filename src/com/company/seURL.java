package com.company;

import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.util.regex.*;
import java.util.*;



/**
 * Created by serhiy on 3/9/16.
 **/
public class seURL implements Comparable<seURL> {
    private boolean retrieved = false;
    private boolean saved = false;
    private String Url = "";
    private int Weight = 0;
    private String FileName = "";

    // Default Constructor
    seURL(String Url, HashMap<String, Integer> filters){
        this.Url = Url;
        this.setWeightByFilters(filters);
    }

    // Constructor
    seURL(String Url){
        this.Url = Url;
    }


    public boolean Retrieve(){
        try{
            URL website = new URL(Url);
            try {
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream(URLtoFileName(Url));
                if ( fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE) > 0 ) {
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


    public void setWeight(int weight) {
        Weight = weight>0?weight:0;
    }


    public void setWeightByFilters(HashMap<String, Integer> filters) {
        filters.keySet().stream()
                .filter(f -> Pattern.matches(f, Url))
                .forEach(f -> Weight += filters.get(f));
    }


    public int getWeight() {
        return Weight;
    }

    @Override
    public int compareTo(seURL o) {
        return o.Weight - Weight;
    }

    private String URLtoFileName(String Url){
        String f = Url.split("/")[Url.split("/").length-1];
        System.out.println(Url + " -> " + f);
        return f;
    }

    public ArrayList<String> Parse() {
        ArrayList<String> list = new ArrayList<>();
        if ( Url.equals("http://www.example321.com") )
            list.add("http://www.i.ua");
        return list;
    }
}
