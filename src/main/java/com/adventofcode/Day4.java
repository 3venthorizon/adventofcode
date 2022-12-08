package com.adventofcode;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

import com.opencsv.CSVReader;

public class Day4 {
   
   CSVReader createCsvReader() throws IOException, URISyntaxException {
      ClassLoader classLoader = getClass().getClassLoader();
      URL resource = classLoader.getResource("day4.input");
      Reader reader = Files.newBufferedReader(Paths.get(resource.toURI()));
      return new CSVReader(reader);
   }

   public long part1() throws IOException, URISyntaxException {
      long total = 0L;
      
      try (CSVReader csvReader = createCsvReader()) {
         Iterator<String[]> iterator = csvReader.iterator();
         
         while (iterator.hasNext()) {
            String[] fields = iterator.next();
            if (fields.length < 2) continue;
            
            if (totalOverlap(fields[0], fields[1])) total++;
         }
      }
      
      return total;
   }
   
   public long part2() throws IOException, URISyntaxException {
      long total = 0L;
      
      try (CSVReader csvReader = createCsvReader()) {
         Iterator<String[]> iterator = csvReader.iterator();
         
         while (iterator.hasNext()) {
            String[] fields = iterator.next();
            if (fields.length < 2) continue;
            
            if (overlap(fields[0], fields[1])) total++;
         }
      }
      
      return total;
   }
   
   boolean totalOverlap(String left, String right) {
      String[] leftHiLo = left.split("-");
      String[] rightHiLo = right.split("-");
      int llo = Integer.parseInt(leftHiLo[0]);
      int lhi = Integer.parseInt(leftHiLo[1]);
      int rlo = Integer.parseInt(rightHiLo[0]);
      int rhi = Integer.parseInt(rightHiLo[1]);
      
      if (llo < rlo) return lhi >= rhi;
      if (llo > rlo) return lhi <= rhi;
      return true;
   }
   
   boolean overlap(String left, String right) {
      String[] leftHiLo = left.split("-");
      String[] rightHiLo = right.split("-");
      int llo = Integer.parseInt(leftHiLo[0]);
      int lhi = Integer.parseInt(leftHiLo[1]);
      int rlo = Integer.parseInt(rightHiLo[0]);
      int rhi = Integer.parseInt(rightHiLo[1]);
      
      if (llo >= rlo && llo <= rhi) return true;
      if (lhi >= rlo && lhi <= rhi) return true;
      if (rlo >= llo && rlo <= lhi) return true;
      if (rhi >= llo && rhi <= lhi) return true;
      return false;
   }
}
