package com.adventofcode.y2022;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.opencsv.stream.reader.LineReader;

public class Day13 {
   
   static final ObjectMapper MAPPER = new ObjectMapper();
   
   BufferedReader createReader() throws IOException, URISyntaxException {
      ClassLoader classLoader = getClass().getClassLoader();
      URL resource = classLoader.getResource("2022/day13.input");
      Reader reader = Files.newBufferedReader(Paths.get(resource.toURI()));
      return new BufferedReader(reader);
   }
   
   public int part1() throws IOException, URISyntaxException {
      int count = 0;
      int orderedIndexSum = 0;
      
      try (BufferedReader reader = createReader()) {
         LineReader lineReader = new LineReader(reader, false);
         String line = lineReader.readLine();
         String[] pair = new String[2];
         
         while (line != null) {
            if (line.isBlank()) {
               line = lineReader.readLine();
               continue;
            }
            
            pair[count % 2] = line;
            count++;
            
            if (count % 2 == 0) {
               orderedIndexSum += isInOrder(pair) ? count / 2 : 0;
               System.out.println("Index: " + count / 2 + " orderedIndexSum: " + orderedIndexSum + "\n");
            }
            
            line = lineReader.readLine();
         }
      }

      return orderedIndexSum;
   }
   
   public int part2() throws IOException, URISyntaxException {
      JsonNode divider2 = MAPPER.readTree("[[2]]");
      JsonNode divider6 = MAPPER.readTree("[[6]]");
      List<JsonNode> list = new ArrayList<>();
      list.add(divider2);
      list.add(divider6);
      
      try (BufferedReader reader = createReader()) {
         LineReader lineReader = new LineReader(reader, false);
         String line = lineReader.readLine();
         
         while (line != null) {
            if (line.isBlank()) {
               line = lineReader.readLine();
               continue;
            }
            
            list.add(MAPPER.readTree(line));
            line = lineReader.readLine();
         }
      }
      
      list.sort(this::compare);

      return (list.indexOf(divider2) + 1) * (list.indexOf(divider6) + 1);
   }
   
   boolean isInOrder(String[] pair) throws JsonMappingException, JsonProcessingException {
      JsonNode left = MAPPER.readTree(pair[0]);          JsonNode right = MAPPER.readTree(pair[1]);
      System.out.println("Left: " + left + "\tRight: " + right);
      
      return compare(left, right) <= 0;
   }
   
   int compare(JsonNode left, JsonNode right) {
      if (left.isNumber() && right.isNumber()) return Integer.compare(left.asInt(), right.asInt());
      if (left.isArray() && left.isEmpty() && right.isArray() && right.isEmpty()) return 0;
      if (left.isArray() && left.isEmpty()) return -1;   if (right.isArray() && right.isEmpty()) return 1;
      
      if (!left.isArray()) left = new ArrayNode(MAPPER.getNodeFactory()).add(left);
      if (!right.isArray()) right = new ArrayNode(MAPPER.getNodeFactory()).add(right);
      Iterator<JsonNode> literator = left.iterator();    
      Iterator<JsonNode> riterator = right.iterator();
      int cmp = 0;
      
      while (literator.hasNext()) {
         if (!riterator.hasNext()) return 1;
         JsonNode litem = literator.next();              JsonNode ritem = riterator.next();
         
         cmp = compare(litem, ritem);
         if (cmp != 0) return cmp;
      }
      
      return cmp;
   }
}
