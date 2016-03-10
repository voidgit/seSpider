package com.company;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        // Define filters
        HashMap<String, Integer> filters = new HashMap<>();
        filters.put(".+", 1);
        filters.put(".+docs\\..+", 10);

        // Create priority queue and add start URL's
        PriorityQueue<seURL> Table = new PriorityQueue<>();
        Table.add(new seURL("http://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html", filters));
        Table.add(new seURL("http://www.example321.com", filters));

        System.out.println("Filters: " + filters);
        while ( ! Table.isEmpty() ){     // there are queued seURLS
            seURL seurl = Table.poll();
          //System.out.println(seurl.getUrl() + " - prio=" + seurl.getWeight());
            seurl.Retrieve();
            ArrayList<String> list = seurl.Parse();
            for ( String l : list ) {
                Table.add(new seURL(l, filters));
            }
        }
    }
}
