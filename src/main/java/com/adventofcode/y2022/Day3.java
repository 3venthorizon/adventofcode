package com.adventofcode.y2022;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.opencsv.CSVReader;

public class Day3 {
   
   static final Map<Character, Long> MAP_PRIORITY;
   
   static {
      Map<Character, Long> map = new HashMap<>();
      
      for (long priority = 1, item = 'a'; item <= 'z'; item++, priority++) {
         map.put((char) item, priority);
      }
      
      for (long priority = 27, item = 'A'; item <= 'Z'; item++, priority++) {
         map.put((char) item, priority);
      }
      
      MAP_PRIORITY = Collections.unmodifiableMap(map);
   }

   CSVReader createCsvReader() throws IOException, URISyntaxException {
      ClassLoader classLoader = getClass().getClassLoader();
      URL resource = classLoader.getResource("2022/day3.input");
      Reader reader = Files.newBufferedReader(Paths.get(resource.toURI()));
      return new CSVReader(reader);
   }
   
   public long part1() throws IOException, URISyntaxException {
      long total = 0L;
      
      try (CSVReader csvReader = createCsvReader()) {
         Iterator<String[]> iterator = csvReader.iterator();
         
         while (iterator.hasNext()) {
            String[] fields = iterator.next();
            if (fields.length < 1) continue;
            
            total += priority(fields[0]);
         }
      }
      
      return total;
   }
   
   public long part2() throws IOException, URISyntaxException {
      long total = 0L;
      int count = 0;
      
      try (CSVReader csvReader = createCsvReader()) {
         Iterator<String[]> iterator = csvReader.iterator();
         String[] rucksacks = new String[3];
         
         while (iterator.hasNext()) {
            String[] fields = iterator.next();
            if (fields.length < 1) continue;
            
            rucksacks[count % 3] = fields[0];
            count++;
            
            if (count % 3 == 0) total += priority(rucksacks);
         }
      }
      
      return total;
   }
   
   long priority(String rucksack) {
      long priority = 0L;
      int size = rucksack.length() / 2;
      String left = rucksack.substring(0, size);
      String right = rucksack.substring(size);
      
      for (int index = 0; index < size; index++) {
         char item = left.charAt(index);
         if (right.indexOf(item) < 0) continue;
         
         priority += MAP_PRIORITY.get(item);
         break;
      }
      
      return priority;
   }
   
   long priority(String[] rucksacks) {
      long priority = 0L;
      
      for (int index = 0; index < rucksacks[0].length(); index++) {
         char badge = rucksacks[0].charAt(index);
         if (rucksacks[1].indexOf(badge) < 0) continue;
         if (rucksacks[2].indexOf(badge) < 0) continue;
         
         priority += MAP_PRIORITY.get(badge);
         break;
      }
      
      return priority;
   }
}
