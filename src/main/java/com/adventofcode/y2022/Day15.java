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
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class Day15 {
   static final String SX = "Sensor at x=";
   static final String Y = "y=";
   static final String BX = "closest beacon is at x=";
   
   static final int XY_MIN = 0;
   static final int XY_MAX = 4_000_000;
   static final Predicate<Integer> IN_BOUNDARY = xy -> xy >= XY_MIN && xy <= XY_MAX;
   
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
      
      @Override
      public String toString() {
         return "[" + x + "," + y + "]";
      }
   }
   
   static class Line {
      int x, y, m, c;
      
      Line(int x, int y, int m) {
         this(x, y, m, y - (m * x));
      }

      Line(int x, int y, int m, int c) {
         this.x = x;
         this.y = y;
         this.m = m;
         this.c = c;
      }

      @Override
      public int hashCode() {
         return Objects.hash(x, y, m, c);
      }

      @Override
      public boolean equals(Object object) {
         if (this == object) return true;
         if (!(object instanceof Line)) return false;
         Line other = (Line) object;
         return this.c == other.c && this.m == other.m && this.x == other.x && this.y == other.y;
      }
      
      @Override
      public String toString() {
         return "y(" + y + ") = m(" + m + ").x(" + x + ") + c(" + c + ")";
      }
   }
   
   BufferedReader createReader() throws IOException, URISyntaxException {
      ClassLoader classLoader = getClass().getClassLoader();
      URL resource = classLoader.getResource("2022/day15.input");
      Reader reader = Files.newBufferedReader(Paths.get(resource.toURI()));
      return new BufferedReader(reader);
   }
   
   void loadCoordinates(Map<Coordinate, Coordinate> sensorBeaconMap, Map<Coordinate, Integer> sensorDistanceMap) 
         throws IOException, URISyntaxException {
      try (BufferedReader reader = createReader()) {
         String line = reader.readLine();
         
         while (line != null) {
            readSensorBeacon(line, sensorBeaconMap, sensorDistanceMap);
            line = reader.readLine();
         }
      }
   }
   
   void readSensorBeacon(String line, 
         Map<Coordinate, Coordinate> sensorBeaconMap, 
         Map<Coordinate, Integer> sensorDistanceMap) {
      int sx = Integer.parseInt(line.substring(SX.length(), line.indexOf(',')));
      int sy = Integer.parseInt(line.substring(line.indexOf(Y) + Y.length(), line.indexOf(':')));
      int bxIndex = line.indexOf(BX);
      int bx = Integer.parseInt(line.substring(bxIndex + BX.length(), line.indexOf(',', bxIndex)));
      int by = Integer.parseInt(line.substring(line.indexOf(Y, bxIndex) + Y.length()));
      int distance = Math.abs(sx - bx) + Math.abs(sy - by);
      
      Coordinate coordinate = new Coordinate(sx, sy);
      sensorBeaconMap.put(coordinate, new Coordinate(bx, by));
      sensorDistanceMap.put(coordinate, distance);
   }
   
   public int part1() throws IOException, URISyntaxException {
      final int row = 2_000_000;
      Map<Coordinate, Coordinate> sensorBeaconMap = new HashMap<>();
      Map<Coordinate, Integer> sensorDistanceMap = new HashMap<>();
      loadCoordinates(sensorBeaconMap, sensorDistanceMap);
      
      IntSummaryStatistics statistics = sensorBeaconMap.keySet().stream()
            .mapToInt(coordinate -> coordinate.x)
            .summaryStatistics();
      int maxDistance = sensorDistanceMap.values().stream().mapToInt(Integer::intValue).max().orElse(0);
      
      int count = 0;
      
      for (int x = statistics.getMin() - maxDistance; x <= statistics.getMax() + maxDistance; x++) {
         final Coordinate coordinate = new Coordinate(x, row);
         if (sensorBeaconMap.values().contains(coordinate)) continue;
         
         boolean covered = sensorDistanceMap.entrySet().stream()
               .anyMatch(entry -> isCovered(entry.getKey(), entry.getValue(), coordinate));
         
         if (covered) count++;
      }
      
      return count;
   }
   
   public long part2() throws IOException, URISyntaxException {
      Map<Coordinate, Coordinate> sensorBeaconMap = new HashMap<>();
      Map<Coordinate, Integer> sensorDistanceMap = new HashMap<>();
      loadCoordinates(sensorBeaconMap, sensorDistanceMap);
      Map<Coordinate, List<Line>> uncoveredMap = mapUncovered(sensorDistanceMap);
      List<List<Line>> uncoveredLines = uncoveredMap.values().stream().toList();
      
      for (int index = 0, count = uncoveredMap.size(); index < count; index++) {
         for (int outdex = index + 1; outdex < count; outdex++) {
            List<Line> hackNslash = uncoveredLines.get(index);
            List<Line> slashAhack = uncoveredLines.get(outdex);
            Optional<Coordinate> location = locateBeacon(hackNslash, slashAhack, sensorDistanceMap);
            
            if (location.isPresent()) {
               Coordinate beacon = location.get();
               
               return ((long) beacon.x) * XY_MAX + beacon.y;
            }
         }
      }
      
      return -1L;
   }
   
   boolean isCovered(Coordinate sensor, int beaconDistance, Coordinate coordinate) {
      int distance = Math.abs(sensor.x - coordinate.x) + Math.abs(sensor.y - coordinate.y);
      return distance <= beaconDistance;
   }
   
   /**
    * y = mx + c
    */
   Map<Coordinate, List<Line>> mapUncovered(Map<Coordinate, Integer> sensorDistanceMap) {
      Map<Coordinate, List<Line>> uncoveredMap = new HashMap<>();
      
      for (Map.Entry<Coordinate, Integer> entry : sensorDistanceMap.entrySet()) {
         uncoveredMap.put(entry.getKey(), mapUncovered(entry.getKey(), entry.getValue().intValue()));
      }
      
      return uncoveredMap;
   }
   
   List<Line> mapUncovered(Coordinate sensor, int distance) {
      List<Line> lines = new ArrayList<>();
      boolean west = sensor.x >= XY_MIN;
      boolean east = sensor.x <= XY_MAX;
      boolean north = sensor.y >= XY_MIN;
      boolean south = sensor.y <= XY_MAX;
      int toofar = distance + 1;
      
      if (north && west) lines.add(new Line(sensor.x - toofar, sensor.y, -1));
      if (north && east) lines.add(new Line(sensor.x + toofar, sensor.y, 1));
      if (south && west) lines.add(new Line(sensor.x - toofar, sensor.y, 1));
      if (south && east) lines.add(new Line(sensor.x + toofar, sensor.y, -1));
      
      return lines;
   }
   
   Optional<Coordinate> locateBeacon(List<Line> hackNslash, List<Line> slashAhack,
         Map<Coordinate, Integer> sensorDistanceMap) {
      
      for (Line hack : hackNslash) {
         for (Line slash : slashAhack) {
            Optional<Coordinate> intersection = intersection(hack, slash);
            if (intersection.isEmpty()) continue;
            
            Coordinate coordinate = intersection.get();
            boolean located = sensorDistanceMap.entrySet().stream()
                  .noneMatch(entry -> isCovered(entry.getKey(), entry.getValue(), coordinate));
            
            if (located) return intersection;
         }
      }
      
      return Optional.empty();
   }
   
   Optional<Coordinate> intersection(Line hack, Line slash) {
      if (hack.m == slash.m) return Optional.empty();
      
      int x = slash.m == -1 ? (slash.c - hack.c) / 2 : (hack.c - slash.c) / 2;
      int y = hack.m * x + hack.c;
      
      if (IN_BOUNDARY.test(x) && IN_BOUNDARY.test(y)) return Optional.of(new Coordinate(x, y));
      return Optional.empty();
   }
}
