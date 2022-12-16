package com.adventofcode.y2022;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.opencsv.stream.reader.LineReader;

public class Day12 {
   int start = 0;
   int end = 0;
   int width = 0;
   int height = 0;

   BufferedReader createReader() throws IOException, URISyntaxException {
      ClassLoader classLoader = getClass().getClassLoader();
      URL resource = classLoader.getResource("day12.input");
      Reader reader = Files.newBufferedReader(Paths.get(resource.toURI()));
      return new BufferedReader(reader);
   }
   
   byte[] loadGrid() throws IOException, URISyntaxException {
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
      
      return sb.toString().getBytes();
   }
   
   Map<Byte, List<Integer>> beaconMap(byte[] grid) {
      Map<Byte, List<Integer>> topoMap = IntStream.range('a', 'z').boxed()
            .collect(Collectors.toMap(Integer::byteValue, ArrayList::new));
      
      for (int index = 0; index < grid.length; index++) {
         if (grid[index] == 'z') continue;
         
         List<Integer> boundaries = boundaries(grid, index);
         topoMap.get(grid[index]).addAll(boundaries);
      }
      
      return topoMap;
   }
   
   List<Integer> boundaries(byte[] grid, int index) {
      List<Integer> boundaries = options(grid, index);
      boundaries.removeIf(boundary -> grid[boundary] == grid[index]);
      
      return boundaries;
   }
   
   List<Integer> options(byte[] grid, int index) {
      List<Integer> options = new ArrayList<>();
      
      int x = index % width;
      int y = index / width;
      int north = (y - 1) * width + x;
      int south = (y + 1) * width + x;
      int west = y * width + (x - 1);
      int east = y * width + (x + 1);
      //find step + 1
      int step = grid[index] + 1; //next level
      if (north > 0 && Math.abs(step - grid[north]) <= 1) options.add(north);
      if (south < grid.length && Math.abs(step - grid[south]) <= 1) options.add(south);
      if (x > 0 && Math.abs(step - grid[west]) <= 1) options.add(west);
      if (x < width - 1 && Math.abs(step - grid[east]) <= 1) options.add(east);
      
      return options;
   }
   
   int distance(int left, int right) {
      int lx = left % width;
      int ly = left / width;
      int rx = right % width;
      int ry = right / width;
      
      return Math.abs(lx - rx) + Math.abs(ly - ry);
   }
   
   public int part1() throws IOException, URISyntaxException {
      byte[] grid = loadGrid();
      grid[start] = 'a';
      grid[end] = 'z';
      Map<Byte, List<Integer>> beaconMap = beaconMap(grid);
      HashMap<Integer, List<Integer>> optionsMap = new LinkedHashMap<>();
      Set<Integer> deadends = new HashSet<>();
      Deque<Integer> trail = new ArrayDeque<>();
      
      pathFinder(grid, beaconMap, start, trail, optionsMap, deadends);
      
      return 0;
   }
   
   void pathFinder(byte[] grid, Map<Byte, List<Integer>> beaconMap, int location,
         Deque<Integer> trail, Map<Integer, List<Integer>> optionsMap, Set<Integer> deadends) {
      while (location != end) {
         location = move(grid, beaconMap, location, trail, optionsMap, deadends);
      }
   }
   
   /**
    * @param grid
    * @param beaconMap
    * @param location
    * @param trail
    * @param optionsMap
    * @param deadends
    * @return new location
    */
   int move(byte[] grid, Map<Byte, List<Integer>> beaconMap, int location,
         Deque<Integer> trail, Map<Integer, List<Integer>> optionsMap, Set<Integer> deadends) {
      List<Integer> options = options(grid, location);
      
      options.removeAll(deadends);
      options.remove(trail.peek());
      
      if (options.isEmpty()) {
         deadends.add(location);
         return trail.pop();
      }
      
      return 0;
   }
   
   void backtrack() {
      
   }
}
