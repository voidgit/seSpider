package com.company;

import java.util.*;

public class Main {

    private static HashSet<seURL> Table = new HashSet<>();
    private static PriorityQueue<seURL> Queue = new PriorityQueue<>();
    private static HashMap<String, Integer> Filters = new HashMap<>();

    public static void main(String[] args) {
        // Define filters
        addFilter(".+Map.+", +2);
        addFilter(".+Enum.+", +1);

        // Add start URL's
        addUrl("http://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html", 0);
        addUrl("http://www.example321.com", 0);
        addUrl("http://i.ua", 0);

        System.out.println("Filters: " + Filters);
        System.out.println("prio dist  URL   status");
        while ( ! Queue.isEmpty() ){        // there are queued seURLS
            seURL su = Queue.poll();        // get obj with highest priority
            System.out.print("   " + su.getWeight() + "  .." + su.getDistance() + "  " + su.getUrl());
            if ( su.getWeight() <= 0 ) {
                System.out.println();
                continue;
            }
            su.Retrieve();
            System.out.println(" -> " + su.getFileName() + (su.getSaved()?" saved":""));
            HashSet<String> newUrls = su.Parse();
            for ( String url : newUrls ) {
                addUrl(url, su.getDistance()+1);
            }
        }
    }

    public static void addFilter(String s, Integer i) {
        Main.Filters.put(s, i);
    }

    public static void addUrl(String Url, int distance) {
        seURL seurl = new seURL(Url, Filters, distance);
        // TODO decide whether to ignore zero weight URL's here
        boolean added = Table.add(seurl);
        if (added)              // if added to Table = it is unique url
            Queue.add(seurl);   // then add it download queue too
    }

}
