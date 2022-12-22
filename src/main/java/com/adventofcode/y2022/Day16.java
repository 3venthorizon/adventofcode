package com.adventofcode.y2022;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
      String location = "AA";
      Map<String, Valve> map = loadMap();
      List<Valve> flowRankList = map.values().stream()
            .filter(valve -> valve.flowrate > 0)
            .sorted((left, right) -> Integer.compare(right.flowrate, left.flowrate)).toList();
      Map<String, String> dsMap = new HashMap<>();
      
      for (Valve rank : flowRankList) {
      }
      
      return 0;
   }
   
   void breadthFirstSearch(Valve destination, Map<Valve, Valve> breadthMap, 
         Map<Valve, Valve> route, Map<String, Valve> map) {
      Map<Valve, Valve> nextMap = new HashMap<>();
      
      for (Map.Entry<Valve, Valve> entry : breadthMap.entrySet()) {
         Valve breadth = entry.getKey();
         
         if (destination.equals(breadth)) {
            route.put(destination, entry.getValue());
            break;
         }
         
         if (route.containsKey(breadth)) continue;
         
         breadth.tunnels.stream().map(map::get).forEach(next -> nextMap.put(next, breadth));
      }
   
      if (nextMap.isEmpty()) return;
      breadthFirstSearch(destination, nextMap, route, map);
   }
}
