package com.adventofcode.y2022;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.opencsv.stream.reader.LineReader;

public class Day16 {
   static final String MARKER_VALVE = "Valve ";
   static final String MARKER_HAS = " has";
   static final String MARKER_TUNNELS = "valve";
   static final String DELIMITER = ", ";
   
   static class Valve {
      final String name;
      final int flowrate;
      final List<String> tunnels;
      
      Valve(String name, int flowrate, List<String> tunnels) {
         this.name = name;
         this.flowrate = flowrate;
         this.tunnels = tunnels;
      }
      
      @Override
      public int hashCode() {
         return Objects.hash(name);
      }

      @Override
      public boolean equals(Object object) {
         if (this == object) return true;
         if (!(object instanceof Valve)) return false;
         Valve other = (Valve) object;
         return Objects.equals(this.name, other.name);
      }
      
      @Override
      public String toString() {
         return name + "(" + flowrate + ") -> " + tunnels;
      }
   }
   
   BufferedReader createReader() throws IOException, URISyntaxException {
      ClassLoader classLoader = getClass().getClassLoader();
      URL resource = classLoader.getResource("2022/day16.input");
      Reader reader = Files.newBufferedReader(Paths.get(resource.toURI()));
      return new BufferedReader(reader);
   }
   
   Map<String, Valve> loadMap() throws IOException, URISyntaxException {
      Map<String, Valve> map = new HashMap<>();
      
      try (BufferedReader reader = createReader()) {
         LineReader lineReader = new LineReader(reader, false);
         String line = lineReader.readLine();
         
         while (line != null) {
            readValve(line, map);
            line = lineReader.readLine();
         }
      }
      
      return map;
   }
   
   void readValve(String line, Map<String, Valve> map) {
      String valveName = line.substring(MARKER_VALVE.length(), line.indexOf(MARKER_HAS));
      int flowrate = Integer.parseInt(line.substring(line.indexOf('=') + 1, line.indexOf(';')));
      String tunnels = line.substring(line.indexOf(' ', line.indexOf(MARKER_TUNNELS)) + 1);
      List<String> destinations = List.of(tunnels.split(DELIMITER));
      
      map.put(valveName, new Valve(valveName, flowrate, destinations));
   }

   public int part1() throws IOException, URISyntaxException {
      Map<String, Valve> map = loadMap();
      List<Valve> flowRankList = map.values().stream()
            .filter(valve -> valve.flowrate > 0)
            .sorted((left, right) -> Integer.compare(right.flowrate, left.flowrate)).toList();
      Valve start = map.get("AA");
      Map<String, String> dsMap = start.tunnels.stream()
            .collect(Collectors.toMap(Function.identity(), tunnel -> "AA"));
      
      for (Valve rank : flowRankList) {
         Map<String, String> route = new HashMap<>();
         route.put("AA", "AA");
         List<String> trace = breadthFirstSearch(rank.name, dsMap, route, map);
         System.out.println(rank.name + "(" + rank.flowrate + ")\t" + trace);
      }
      
      return 0;
   }
   
   List<String> breadthFirstSearch(String destination, Map<String, String> breadthMap, Map<String, String> route, 
         Map<String, Valve> legend) {
      Map<String, String> nextMap = new HashMap<>();
      
      for (Map.Entry<String, String> entry : breadthMap.entrySet()) {
         String breadth = entry.getKey();
         String existing = route.putIfAbsent(breadth, entry.getValue());
         
         if (existing != null) continue;
         if (destination.equals(breadth)) return trace(route, destination);
         
         Valve valve = legend.get(breadth);
         valve.tunnels.stream().forEach(next -> nextMap.putIfAbsent(next, breadth));
      }
   
      if (nextMap.isEmpty()) return Collections.emptyList();
      return breadthFirstSearch(destination, nextMap, route, legend);
   }
   
   List<String> trace(Map<String, String> route, String destination) {
      List<String> trace = new ArrayList<>();
      String source = route.get(destination);
      if (source == null || source == destination) return trace;
         
      trace.add(destination);

      while (source != null && source != destination) {
         trace.add(0, source);
         destination = source;
         source = route.get(destination);
      }
      
      trace.remove(0); //remove the origin
      return trace;
   }
   
   int score(int total, List<String> route, String destination, Map<String, Valve> legend) {
      return 0;
   }
}
