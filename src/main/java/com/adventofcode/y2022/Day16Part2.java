package com.adventofcode.y2022;

import java.io.BufferedReader;
import java.io.IOException;
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

public class Day16Part2 {
   static final String MARKER_VALVE = "Valve ";
   static final String MARKER_HAS = " has";
   static final String MARKER_ROUTE = " -> ";
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
      BufferedReader reader = Files.newBufferedReader(Paths.get(resource.toURI()));
      return reader;
   }
   
   Map<String, Valve> loadMap() throws IOException, URISyntaxException {
      Map<String, Valve> map = new HashMap<>();
      
      try (BufferedReader reader = createReader()) {
         String line = reader.readLine();
         
         while (line != null) {
            readValve(line, map);
            line = reader.readLine();
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
   
   List<String> route(String departure, String destination, Map<String, Valve> legend) {
      Valve start = legend.get(departure);
      Map<String, String> searchMap = start.tunnels.stream()
            .collect(Collectors.toMap(Function.identity(), tunnel -> departure));
      Map<String, String> routeMap = new HashMap<>();
      routeMap.put(departure, departure);
      
      return breadthFirstSearch(destination, searchMap, routeMap, legend);
   }
   
   List<String> breadthFirstSearch(String destination, Map<String, String> searchMap, Map<String, String> routeMap, 
         Map<String, Valve> legend) {
      Map<String, String> nextMap = new HashMap<>();
      
      for (Map.Entry<String, String> entry : searchMap.entrySet()) {
         String breadth = entry.getKey();
         String existing = routeMap.putIfAbsent(breadth, entry.getValue());
         
         if (existing != null) continue;
         if (destination.equals(breadth)) return traceRoute(routeMap, destination);
         
         Valve valve = legend.get(breadth);
         valve.tunnels.stream().forEach(next -> nextMap.putIfAbsent(next, breadth));
      }
   
      if (nextMap.isEmpty()) return Collections.emptyList();
      return breadthFirstSearch(destination, nextMap, routeMap, legend);
   }
   
   List<String> traceRoute(Map<String, String> routeMap, String destination) {
      String source = routeMap.get(destination);
      List<String> trace = new ArrayList<>();
      trace.add(destination);

      while (source != null && source != destination) {
         trace.add(0, source);
         destination = source;
         source = routeMap.get(destination);
      }
      
      return trace;
   }
   
   public int part2() throws IOException, URISyntaxException {
      Map<String, Valve> legendMap = loadMap();
      List<String> workingList = legendMap.values().stream()
            .filter(valve -> valve.flowrate > 0)
            .sorted((left, right) -> Integer.compare(right.flowrate, left.flowrate))
            .map(valve -> valve.name)
            .toList();
      Map<String, List<String>> routeMap = new HashMap<>();
      
      for (int index = 0, count = workingList.size(); index < count; index++) {
         String start = workingList.get(index);
         String routeId = "AA" + MARKER_ROUTE + start;
         List<String> route = route("AA", start, legendMap);
         routeMap.put(routeId, route);
         
         for (int outdex = 0; outdex < count; outdex++) {
            if (index == outdex) continue;
            
            String destination = workingList.get(outdex);
            routeId = start + MARKER_ROUTE + destination;
            route = route(start, destination, legendMap);
            routeMap.put(routeId, route);
         }
      }
      
      List<String> myRoute = List.of("AA");
      List<String> elephantRoute = List.of("AA");
      
      return elephantRoute(myRoute, elephantRoute, workingList, routeMap, legendMap);
   }
   
   int elephantRoute(List<String> myRoute, List<String> elephantRoute, List<String> workingList,
         Map<String, List<String>> routeMap, Map<String, Valve> legendMap) {
      List<String> unexplored = new ArrayList<>(workingList);
      
      int myPressure = pressure(myRoute, routeMap, legendMap);
      int elephantPressure = pressure(elephantRoute, routeMap, legendMap);
      int totalPressure = myPressure + elephantPressure;
      
      myRoute = explore(unexplored, myRoute, routeMap, legendMap);
      unexplored.removeAll(myRoute);
//      elephantRoute = explore(unexplored, elephantRoute, routeMap, legendMap);
//      unexplored.removeAll(elephantRoute);
      
      int pressure = pressure(myRoute, routeMap, legendMap);
      if (pressure > myPressure) printPressure("Me      ", myRoute, routeMap, legendMap);
      myPressure = pressure;
      
//      pressure = pressure(elephantRoute, routeMap, legendMap);
//      if (pressure > elephantPressure) printPressure("Elephant", elephantRoute, routeMap, legendMap);
//      elephantPressure = pressure;
      
      if (totalPressure == myPressure + elephantPressure) return totalPressure;
      return elephantRoute(myRoute, elephantRoute, unexplored, routeMap, legendMap);
   }
   
   List<String> explore(List<String> unexplored, List<String> openValves, 
         Map<String, List<String>> routeMap, Map<String, Valve> legendMap) {
      List<String> bestRoute = openValves;
      int pressure = pressure(openValves, routeMap, legendMap);
      
      for (String explore : unexplored) {
         for (int index = 1, count = openValves.size(); index <= count; index++) {
            List<String> detour = new ArrayList<>(openValves);
            detour.add(index, explore);
            int detourPressure = pressure(detour, routeMap, legendMap);
            
            if (detourPressure <= pressure) continue;
            
            pressure = detourPressure;
            bestRoute = detour;
         }
      }
      
      return bestRoute;
   }
   
   int pressure(List<String> openValves, Map<String, List<String>> routeMap, Map<String, Valve> legendMap) {
      int minutes = 0;
      int pressure = 0;
      String start = openValves.get(0);
      
      for (int index = 1, count = openValves.size(); index < count; index++) {
         String opened = openValves.get(index);
         String routeId = start + MARKER_ROUTE + opened;
         start = opened;
         List<String> route = routeMap.get(routeId);
         minutes += route.size();
         if (minutes >= 30) break;
         
         Valve valve = legendMap.get(opened);
         pressure += (30 - minutes) * valve.flowrate;
      }
      
      return pressure;
   }
   
   void printPressure(String whom, List<String> openValves, 
         Map<String, List<String>> routeMap, Map<String, Valve> legendMap) {
      int minutes = 0;
      int pressure = 0;
      String start = openValves.get(0);
      
      for (int index = 1, count = openValves.size(); index < count; index++) {
         String opened = openValves.get(index);
         String routeId = start + MARKER_ROUTE + opened;
         start = opened;
         List<String> route = routeMap.get(routeId);
         minutes += route.size();
         if (minutes >= 30) break;
         
         Valve valve = legendMap.get(opened);
         pressure += (30 - minutes) * valve.flowrate;
         
         System.out.println(opened + " " + whom + " Pressure: " + pressure + "\t" + route);
      }
      
      System.out.println(whom + " Total Pressure: " + pressure);
   }
}
