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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
   
   Map<Byte, List<Integer>> topoMap(byte[] grid) {
      Map<Byte, List<Integer>> topoMap = IntStream.range('a', 'z').boxed()
            .collect(Collectors.toMap(Integer::byteValue, ArrayList::new));
      
      for (int index = 0; index < grid.length; index++) {
         if (grid[index] == 'z') continue;
         
         try {
            List<Integer> boundaries = boundaries(grid, index);
            topoMap.get(grid[index]).addAll(boundaries);
         } catch (Exception e) {
            System.out.println(grid[index]);
            e.printStackTrace();
         }
      }
      
      return topoMap;
   }
   
   List<Integer> boundaries(byte[] grid, int index) {
      List<Integer> boundaries = new ArrayList<>();
      int x = index % width;
      int y = index / width;
      int north = (y - 1) * width + x;
      int south = (y + 1) * width + x;
      int west = y * width + (x - 1);
      int east = y * width + (x + 1);
      //find step + 1
      int step = grid[index] + 1; //next level
      if (north > 0 && step == grid[north]) boundaries.add(north);
      if (south < grid.length && step == grid[south]) boundaries.add(south);
      if (x > 0 && step == grid[west]) boundaries.add(west);
      if (x < width - 1 && step == grid[east]) boundaries.add(east);
      
      return boundaries;
   }
   
   public int part1() throws IOException, URISyntaxException {
      byte[] grid = loadGrid();
      grid[start] = 'a';
      grid[end] = 'z';
      Map<Byte, List<Integer>> topoMap = topoMap(grid);
      
      return 0;
   }
}
