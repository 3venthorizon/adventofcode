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
   
   static final int MINUTES = 30;
   
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
      Map<String, Valve> legend = loadMap();
      List<Valve> flowRankList = legend.values().stream()
            .filter(valve -> valve.flowrate > 0)
            .sorted((left, right) -> Integer.compare(right.flowrate, left.flowrate))
            .toList();
      Map<String, List<String>> valveRouteMap = flowRankList.stream()
            .collect(Collectors.toMap(valve -> valve.name, valve -> new ArrayList<>()));
      List<String> route = new ArrayList<>();
      route.add("AA");
      valveRouteMap.put("AA", Collections.emptyList());
      
      return route(route, valveRouteMap, legend);
   }
   
   int route(List<String> route, Map<String, List<String>> valveRouteMap, Map<String, Valve> legend) {
      List<String> openValves = new ArrayList<>(route);
      List<String> unexplored = new ArrayList<>(valveRouteMap.keySet());
      
      openValves.retainAll(valveRouteMap.keySet());
      unexplored.removeAll(route);

      int score = score(openValves, valveRouteMap, legend);
      int minutes = valveRouteMap.values().stream()
            .mapToInt(List::size)
            .sum();
      String best = null;
      List<String> bestRoute = null;
      Map<String, List<String>> bestRouteMap = null;
      
      for (String explore : unexplored) {
         for (int index = 1, count = openValves.size(); index <= count; index++) {
            List<String> traceRoute = route(openValves.get(index - 1), explore, legend);
            if (minutes + traceRoute.size() > MINUTES) continue;
            
            List<String> detour = new ArrayList<>(openValves);
            Map<String, List<String>> detourRouteMap = new HashMap<>(valveRouteMap);

            detour.add(index, explore);
            detourRouteMap.put(explore, traceRoute);
            
            //re-route
            if (index < count) { 
               List<String> reRoute = route(explore, openValves.get(index), legend);
               List<String> oldRoute = detourRouteMap.put(openValves.get(index), reRoute);
               
               if (minutes - oldRoute.size() + reRoute.size() + traceRoute.size() > MINUTES) continue;
            }
            
            int detourScore = score(detour, detourRouteMap, legend);
            if (detourScore <= score) continue;
            
            score = detourScore;
            best = explore;
            bestRoute = detour;
            bestRouteMap = detourRouteMap;
         }
      }
      
      if (best == null) return score;

      return route(bestRoute, bestRouteMap, legend);
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
   
   int score(List<String> openValves, Map<String, List<String>> valveRouteMap, Map<String, Valve> legend) {
      int minutes = 0;
      int score = 0;
      
      for (String opened : openValves) {
         List<String> route = valveRouteMap.get(opened);
         minutes += route.size();
         Valve valve = legend.get(opened);
         score += (MINUTES - minutes) * valve.flowrate;
      }
      
      return score;
   }
   
   void printRouteScore(List<String> openValves, Map<String, List<String>> valveRouteMap, Map<String, Valve> legend) {
      int minutes = 0;
      int score = 0;
      
      for (String opened : openValves) {
         List<String> route = valveRouteMap.get(opened);
         minutes += route.size();
         Valve valve = legend.get(opened);
         int pressure = (MINUTES - minutes) * valve.flowrate;
         score += pressure;

         System.out.println(opened + " Pressure: " + pressure + "\t" + route);
      }
      
      System.out.println("Total Pressure: " + score);
   }
}
