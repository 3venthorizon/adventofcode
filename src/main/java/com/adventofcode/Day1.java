package com.adventofcode;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.opencsv.CSVReader;

public class Day1 {

   /**
    * Part 1
    * 
    * @return maxCount
    * @throws IOException
    * @throws URISyntaxException
    */
   public long maxSum() throws IOException, URISyntaxException {
      ClassLoader classLoader = getClass().getClassLoader();
      URL resource = classLoader.getResource("day1.input");
      Reader reader = Files.newBufferedReader(Paths.get(resource.toURI()));
      long sum = 0L;
      long max = -1L;
      
      try (CSVReader csvReader = new CSVReader(reader)) {
         Iterator<String[]> iterator = csvReader.iterator();
         
         while (iterator.hasNext()) {
            String[] fields = iterator.next();
            
            if (fields.length == 0 || fields[0].isBlank()) {
               max = Math.max(max, sum);
               sum = 0L;
               continue;
            }
            
            sum += Long.parseLong(fields[0]);
         }
         
         max = Math.max(max, sum);
      }
      
      return max;
   }
   
   /**
    * Part 2
    * 
    * @return sum
    * @throws IOException
    * @throws URISyntaxException
    */
   public long top3Sum() throws IOException, URISyntaxException {
      ClassLoader classLoader = getClass().getClassLoader();
      URL resource = classLoader.getResource("day1.input");
      Reader reader = Files.newBufferedReader(Paths.get(resource.toURI()));
      long sum = 0L;
      List<Long> top3 = new ArrayList<>();
      top3.add(-1L);
      top3.add(-2L);
      top3.add(-3L);
      
      try (CSVReader csvReader = new CSVReader(reader)) {
         Iterator<String[]> iterator = csvReader.iterator();
         
         while (iterator.hasNext()) {
            String[] fields = iterator.next();
            
            if (fields.length == 0 || fields[0].isBlank()) {
               retainTop3(top3, sum);
               sum = 0L;
               continue;
            }
            
            sum += Long.parseLong(fields[0]);
         }
         
         retainTop3(top3, sum);
      }
      
      return top3.stream().reduce(0L, Math::addExact);
   }
   
   void retainTop3(List<Long> top3, Long sum) {
      for (int x = 0, count = top3.size(); x < count; x++) {
         Long max = top3.get(x);
         if (sum.compareTo(max) < 0) continue;
         top3.add(x, sum);
         top3.remove(count);
         break;
      }
   }
}
