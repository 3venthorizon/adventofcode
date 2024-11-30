package com.adventofcode.y2022;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.opencsv.stream.reader.LineReader;

public class Day12 {
   
   int start = 0;
   int end = 0;
   int width = 0;
   int height = 0;
   byte[] grid;

   BufferedReader createReader() throws IOException, URISyntaxException {
      ClassLoader classLoader = getClass().getClassLoader();
      URL resource = classLoader.getResource("2022/day12.input");
      Reader reader = Files.newBufferedReader(Paths.get(resource.toURI()));
      return new BufferedReader(reader);
   }
   
   void loadGrid() throws IOException, URISyntaxException {
      StringBuilder sb = new StringBuilder();
      
      try (BufferedReader reader = createReader()) {
         LineReader lineReader = new LineReader(reader, false);
         String line = lineReader.readLine();
         
         while (line != null) {
            width = line.length();
            int start = line.indexOf('S');
            int end = line.indexOf('E');
            if (start >= 0) this.start = height * width + start;
            if (end >= 0) this.end = height * width + end;
            
            sb.append(line);
            height++;
            line = lineReader.readLine();
         }
      }
      
      grid = sb.toString().getBytes();
   }
   
   List<Integer> options(int index) {
      List<Integer> options = new ArrayList<>();
      
      int x = index % width;
      int y = index / width;
      int north = (y - 1) * width + x;
      int south = (y + 1) * width + x;
      int west = y * width + (x - 1);
      int east = y * width + (x + 1);
      
      if (north > 0           && grid[north] - grid[index] <= 1) options.add(north);
      if (south < grid.length && grid[south] - grid[index] <= 1) options.add(south);
      if (x > 0               && grid[west]  - grid[index] <= 1) options.add(west);
      if (x < width - 1       && grid[east]  - grid[index] <= 1) options.add(east);
      
      return options;
   }
   
   Set<Integer> topoMap() {
      Set<Integer> topoMap = new HashSet<>();
      
      for (int index = 0; index < grid.length; index++) {
         if (grid[index] != 'a') continue;
         
         final int idx = index;
         List<Integer> boundaries = options(idx);
         boundaries.removeIf(boundary -> grid[boundary] == grid[idx]);
         
         if (!boundaries.isEmpty()) topoMap.add(index);
      }
      
      return topoMap;
   }
   
   public int part1() throws IOException, URISyntaxException {
      loadGrid();
      grid[start] = 'a';
      grid[end] = 'z';
      
      Map<Integer, Integer> routeMap = new HashMap<>();
      routeMap.put(start, -1);
      
      pathFinder(Set.of(start), routeMap);
      
      List<Integer> traceRoute = trace(routeMap);
      printGrid();
      
      return traceRoute.size();
   }
   
   void pathFinder(Set<Integer> optionsSet, Map<Integer, Integer> route) {
      Set<Integer> nextSet = new HashSet<>();
      
      for (Integer location : optionsSet) {
         for (Integer next : options(location)) {
            Integer existing = route.putIfAbsent(next, location);
            
            if (existing != null) continue;
            if (end == next.intValue()) return;
            
            nextSet.add(next);
         }
      }
      
      if (nextSet.isEmpty()) return;
      pathFinder(nextSet, route);
   }
   
   List<Integer> trace(Map<Integer, Integer> routeMap) {
      List<Integer> trace = new ArrayList<>();
      Integer source = routeMap.get(end);
      
      while (source != null && source != -1) {
         grid[source] = (byte) Character.toUpperCase(grid[source]);
         trace.add(0, source);
         source = routeMap.get(source);
      }
      
      if (!trace.isEmpty()) {
         grid[end] = (byte) Character.toUpperCase(grid[end]);
         trace.add(end);
         trace.remove(0); //don't count the start position.
      }
      
      return trace;
   }
   
   void printGrid() {
      System.out.println();
      for (int offset = 0; offset < grid.length; offset += width) {
         System.out.println(new String(grid, offset, width));
      }
      System.out.println();
   }
   
   public int part2() throws IOException, URISyntaxException {
      loadGrid();
      grid[end] = 'z';
      byte[] backup = new byte[grid.length];
      System.arraycopy(grid, 0, backup, 0, grid.length);
      
      int min = Integer.MAX_VALUE;
      Set<Integer> scene = topoMap();
      
      for (Integer a : scene) {
         System.arraycopy(backup, 0, grid, 0, grid.length);
         
         Map<Integer, Integer> routeMap = new HashMap<>();
         routeMap.put(a, -1);
         
         pathFinder(Set.of(a), routeMap);
         
         List<Integer> traceRoute = trace(routeMap);
         if (traceRoute.isEmpty()) continue;
         min = Math.min(min, traceRoute.size());
      }
      
      printGrid();
      
      return min;
   }
}
