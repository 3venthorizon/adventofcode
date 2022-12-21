package com.adventofcode.y2022;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import com.opencsv.stream.reader.LineReader;

public class Day9 {
   
   static final String SPACE = " ";
   static final Map<String, BiFunction<Coordinate, List<Coordinate>, Coordinate>> COMMAND_MAP;
   
   static {
      Map<String, BiFunction<Coordinate, List<Coordinate>, Coordinate>> map = new HashMap<>();
      
      map.put("U", (head, tail) -> up(head, tail));
      map.put("D", (head, tail) -> down(head, tail));
      map.put("L", (head, tail) -> left(head, tail));
      map.put("R", (head, tail) -> right(head, tail));
      
      COMMAND_MAP = Collections.unmodifiableMap(map);
   }
   
   static class Coordinate {
      int x, y;
      
      Coordinate(int x, int y) {
         this.x = x;
         this.y = y;
      }
      
      Coordinate(Coordinate coordinate) {
         this.x = coordinate.x;
         this.y = coordinate.y;
      }

      @Override
      public int hashCode() {
         return Objects.hash(x, y);
      }

      @Override
      public boolean equals(Object object) {
         if (this == object) return true;
         if (!(object instanceof Coordinate)) return false;
         Coordinate other = (Coordinate) object;
         return this.x == other.x && this.y == other.y;
      }
   }
   
   public static Coordinate up(Coordinate head, List<Coordinate> tail) {
      head.y += 1;
      return tail(head, tail);
   }
   
   public static Coordinate down(Coordinate head, List<Coordinate> tail) {
      head.y -= 1;
      return tail(head, tail);
   }
   
   public static Coordinate left(Coordinate head, List<Coordinate> tail) {
      head.x -= 1;
      return tail(head, tail);
   }
   
   public static Coordinate right(Coordinate head, List<Coordinate> tail) {
      head.x += 1;
      return tail(head, tail);
   }
   
   public static boolean tail(Coordinate head, Coordinate tail) {
      int xdifference = Math.abs(head.x - tail.x);
      int ydifference = Math.abs(head.y - tail.y);
      
      if (xdifference > 1 && ydifference > 1) {
         tail.x = head.x + Integer.compare(tail.x, head.x);
         tail.y = head.y + Integer.compare(tail.y, head.y);
         return true;
      }
      
      if (xdifference > 1) {
         tail.y = head.y;
         tail.x = head.x + Integer.compare(tail.x, head.x);
         return true;
      }
      
      if (ydifference > 1) {
         tail.x = head.x;
         tail.y = head.y + Integer.compare(tail.y, head.y);
         return true;
      }
      
      return false;
   }
   
   public static Coordinate tail(Coordinate head, List<Coordinate> tail) {
      Coordinate headX = head;
      
      for (Coordinate tailX : tail) {
         if (!tail(headX, tailX)) break;
         headX = tailX;
      }
      
      return new Coordinate(tail.get(tail.size() - 1));
   }

   BufferedReader createReader() throws IOException, URISyntaxException {
      ClassLoader classLoader = getClass().getClassLoader();
      URL resource = classLoader.getResource("2022/day9.input");
      Reader reader = Files.newBufferedReader(Paths.get(resource.toURI()));
      return new BufferedReader(reader);
   }

   public int part1() throws IOException, URISyntaxException {
      Coordinate head = new Coordinate(0, 0);
      List<Coordinate> tail = List.of(new Coordinate(0, 0));
      Set<Coordinate> trails = new HashSet<>();

      try (BufferedReader reader = createReader()) {
         LineReader lineReader = new LineReader(reader, false);
         String line = lineReader.readLine();
         
         while (line != null) {
            String[] data = line.split(SPACE);
            line = lineReader.readLine();
            
            instruction(head, tail, trails, data);
         }
      }
      
      return trails.size();
   }
   
   public int part2() throws IOException, URISyntaxException {
      Coordinate head = new Coordinate(0, 0);
      List<Coordinate> tail = IntStream.rangeClosed(1, 9)
            .mapToObj(i -> new Coordinate(0, 0))
            .toList();
      Set<Coordinate> trails = new HashSet<>();

      try (BufferedReader reader = createReader()) {
         LineReader lineReader = new LineReader(reader, false);
         String line = lineReader.readLine();
         
         while (line != null) {
            String[] data = line.split(SPACE);
            line = lineReader.readLine();
            
            instruction(head, tail, trails, data);
         }
      }
      
      return trails.size();
   }

   void instruction(Coordinate head, List<Coordinate> tail, Set<Coordinate> trails, String[] data) {
      String instruction = data[0];
      int distance = Integer.parseInt(data[1]);
      BiFunction<Coordinate, List<Coordinate>, Coordinate> command = COMMAND_MAP.get(instruction);
      if (command == null) return;

      for (int move = 0; move < distance; move++) {
         trails.add(command.apply(head, tail));
      }
   }
}
