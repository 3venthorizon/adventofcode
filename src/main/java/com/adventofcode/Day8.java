package com.adventofcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.stream.reader.LineReader;

public class Day8 {

   BufferedReader createReader() throws IOException, URISyntaxException {
      ClassLoader classLoader = getClass().getClassLoader();
      URL resource = classLoader.getResource("day8.input");
      Reader reader = Files.newBufferedReader(Paths.get(resource.toURI()));
      return new BufferedReader(reader);
   }
   
   public long part1() throws IOException, URISyntaxException {
      List<byte[]> forrest = loadForrest();
      long north;
      
      
      
      return 0L;
   }
   
   List<byte[]> loadForrest() throws IOException, URISyntaxException {
      List<byte[]> forrest = new ArrayList<>(); 
      
      try (BufferedReader reader = createReader()) {
         LineReader lineReader = new LineReader(reader, false);
         String line = lineReader.readLine();
         
         while (line != null) {
            forrest.add(line.getBytes());
            line = lineReader.readLine();
         }
      }
      
      return forrest;
   }
}
