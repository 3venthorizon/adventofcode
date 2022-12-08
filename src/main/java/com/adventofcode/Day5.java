package com.adventofcode;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.opencsv.CSVReader;

public class Day5 {
   
   CSVReader createCsvReader() throws IOException, URISyntaxException {
      ClassLoader classLoader = getClass().getClassLoader();
      URL resource = classLoader.getResource("day5.input");
      Reader reader = Files.newBufferedReader(Paths.get(resource.toURI()));
      return new CSVReader(reader);
   }

   /**
              [C] [B] [H]                
      [W]     [D] [J] [Q] [B]            
      [P] [F] [Z] [F] [B] [L]            
      [G] [Z] [N] [P] [J] [S] [V]        
      [Z] [C] [H] [Z] [G] [T] [Z]     [C]
      [V] [B] [M] [M] [C] [Q] [C] [G] [H]
      [S] [V] [L] [D] [F] [F] [G] [L] [F]
      [B] [J] [V] [L] [V] [G] [L] [N] [J]
       1   2   3   4   5   6   7   8   9 
    */
   Map<Integer, List<String>> initState() {
      Map<Integer, List<String>> state = new LinkedHashMap<>();
      
      state.put(1, Stream.of("W", "P", "G", "Z", "V", "S", "B").collect(Collectors.toList()));
      state.put(2, Stream.of("F", "Z", "C", "B", "V", "J").collect(Collectors.toList()));
      state.put(3, Stream.of("C", "D", "Z", "N", "H", "M", "L", "V").collect(Collectors.toList()));
      state.put(4, Stream.of("B", "J", "F", "P", "Z", "M", "D", "L").collect(Collectors.toList()));
      state.put(5, Stream.of("H", "Q", "B", "J", "G", "C", "F", "V").collect(Collectors.toList()));
      state.put(6, Stream.of("B", "L", "S", "T", "Q", "F", "G").collect(Collectors.toList()));
      state.put(7, Stream.of("V", "Z", "C", "G", "L").collect(Collectors.toList()));
      state.put(8, Stream.of("G", "L", "N").collect(Collectors.toList()));
      state.put(9, Stream.of("C", "H", "F", "J").collect(Collectors.toList()));
      
      return state;
   }
   
   public String part1() throws IOException, URISyntaxException {
      Map<Integer, List<String>> state = initState();

      try (CSVReader csvReader = createCsvReader()) {
         csvReader.skip(10);
         Iterator<String[]> iterator = csvReader.iterator();
            
         while (iterator.hasNext()) {
            String[] fields = iterator.next();
            if (fields.length < 1 || fields[0].isBlank()) continue;
            
            moveCrates(state, fields[0]);
         }
      }
      
      String containers = "";
      
      for (List<String> stack : state.values()) {
         if (stack.isEmpty()) continue;
         containers += stack.get(0);
      }
      
      return containers;
   }
   
   void moveCrates(Map<Integer, List<String>> state, String instruction) {
      int fIndex = instruction.indexOf("from");
      int tIndex = instruction.indexOf("to");
      int move = Integer.parseInt(instruction.substring(5, fIndex).trim());
      int from = Integer.parseInt(instruction.substring(fIndex + 5, tIndex).trim());
      int to = Integer.parseInt(instruction.substring(tIndex + 3).trim());
      
      List<String> fromStack = state.get(from);
      List<String> toStack = state.get(to);
      
      for (int index = 0; index < move; index++) {
         toStack.add(0, fromStack.remove(0));
      }
   }
}
