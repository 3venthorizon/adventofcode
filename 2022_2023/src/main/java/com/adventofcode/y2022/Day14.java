package com.adventofcode.y2022;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.opencsv.stream.reader.LineReader;

public class Day14 {
   static final String RANGE = " -> ";
   static final String COMMA = ",";
   
   BufferedReader createReader() throws IOException, URISyntaxException {
      ClassLoader classLoader = getClass().getClassLoader();
      URL resource = classLoader.getResource("2022/day14.input");
      Reader reader = Files.newBufferedReader(Paths.get(resource.toURI()));
      return new BufferedReader(reader);
   }
   
   Map<Integer, SortedSet<Integer>> loadMap() throws IOException, URISyntaxException {
      Map<Integer, SortedSet<Integer>> map = new HashMap<>();
      
      try (BufferedReader reader = createReader()) {
         LineReader lineReader = new LineReader(reader, false);
         String line = lineReader.readLine();
         
         while (line != null) {
            populateMap(line, map);
            line = lineReader.readLine();
         }
      }
      
      return map;
   }
   
   void populateMap(String line, Map<Integer, SortedSet<Integer>> map) {
      String[] ranges = line.split(RANGE);
      
      for (int start = 0, end = 1; end < ranges.length; start++, end++) {
         populateRange(ranges[start], ranges[end], map);
      }
   }
   
   void populateRange(String start, String end, Map<Integer, SortedSet<Integer>> map) {
      String[] srange = start.split(COMMA);           String[] erange = end.split(COMMA);
      int sx = Integer.parseInt(srange[0]);           int sy = Integer.parseInt(srange[1]);
      int ex = Integer.parseInt(erange[0]);           int ey = Integer.parseInt(erange[1]);
      if (sx > ex) { int x = sx; sx = ex; ex = x; }   if (sy > ey) { int y = sy; sy = ey; ey = y; }
      int xinc = sx < ex ? 1 : 0;                     int yinc = sy < ey ? 1 : 0;
      
      for (int x = sx, y = sy; x <= ex && y <= ey; x += xinc, y += yinc) {
         map.computeIfAbsent(x, k -> new TreeSet<>()).add(y);
      }
   }
   
   public int part1() throws IOException, URISyntaxException {
      Map<Integer, SortedSet<Integer>> map = loadMap();
      
      int count = 0;
      while (dropSand(500, 0, map)) count++;
      return count;
   }
   
   public int part2() throws IOException, URISyntaxException {
      Map<Integer, SortedSet<Integer>> map = loadMap();
      
      int maxy = map.values().stream().flatMap(SortedSet::stream).mapToInt(Integer::intValue).max().orElse(0) + 2;
      int minx = 500 - maxy;
      int maxx = 500 + maxy;
      String start = String.valueOf(minx) + COMMA + String.valueOf(maxy);
      String end = String.valueOf(maxx) + COMMA + String.valueOf(maxy);
      populateRange(start, end, map);
      
      int count = 0;
      while (dropSand(500, 0, map)) count++;
      return count;
   }
   
   boolean dropSand(int x, int y, Map<Integer, SortedSet<Integer>> map) {
      SortedSet<Integer> obstacle = map.get(x);
      if (obstacle == null) return false; //the abyss
      Integer depth = obstacle.stream().filter(value -> value >= y).findFirst().orElse(null);
      if (depth == null) return false; //the abyss
      if (depth == y) return false; //full
      
      //left
      SortedSet<Integer> drop = map.get(x - 1);
      if (drop == null) return false; //the abyss
      if (!drop.contains(depth)) return dropSand(x - 1, depth, map);
      
      //right
      drop = map.get(x + 1);
      if (drop == null) return false; //the abyss
      if (!drop.contains(depth)) return dropSand(x + 1, depth, map);
      
      //rest
      obstacle.add(depth - 1);
      return true;
   }
}
