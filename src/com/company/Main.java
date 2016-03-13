package com.company;

import java.util.*;

public class Main {

    private static final HashSet<seURL> Table = new HashSet<>();
    private static final PriorityQueue<seURL> Queue = new PriorityQueue<>();
    private static final HashMap<String, Integer> FiltersPrio = new HashMap<>();
    private static final HashMap<String, String> FiltersPath = new HashMap<>();

    public static void main(String[] args) {
        // Define filters for the priority change
        addFilterPrio(".+Map.+", +2);
        addFilterPrio(".+Enum.+", +1);

        // Define filters for the url to path conversion
        addFilterPath("docs\\.oracle\\.com", "DOCS");
        addFilterPath("\\?", "_");

        // Add start URL's
        addUrl("http://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html", 0);
        addUrl("http://www.example321.com", 0);
        addUrl("http://i.ua", 0);

        System.out.println("Filters: " + FiltersPrio);
        System.out.println("prio dist  URL   status");
        while ( ! Queue.isEmpty() ){        // there are queued seURLS
            seURL su = Queue.poll();        // get obj with highest priority
            System.out.print("   " + su.getWeight() + "  .." + su.getDistance() + "  " + su.getUrl());
            if ( su.getWeight() <= 0 ) {
                System.out.println();
                continue;
            }
            su.Retrieve(FiltersPath);
            System.out.println(" -> " + su.getFileName() + (su.getSaved()?" saved":""));
            HashSet<String> newUrls = su.Parse();
            for ( String url : newUrls ) {
                addUrl(url, su.getDistance()+1);
            }
        }
    }

    public static void addFilterPrio(String regexp, Integer i) {
        Main.FiltersPrio.put(regexp, i);
    }

    public static void addFilterPath(String regexp, String newValue) {
        Main.FiltersPath.put(regexp, newValue);
    }

    public static void addUrl(String Url, int distance) {
        seURL seurl = new seURL(Url, FiltersPrio, distance);
        // TODO decide whether to ignore zero weight URL's here
        boolean added = Table.add(seurl);
        if (added)              // if added to Table = it is unique url
            Queue.add(seurl);   // then add it download queue too
    }

}
