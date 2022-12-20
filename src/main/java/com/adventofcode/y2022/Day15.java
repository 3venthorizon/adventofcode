package com.adventofcode.y2022;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.Map;
import java.util.Objects;

import com.opencsv.stream.reader.LineReader;

public class Day15 {
   static final String SX = "Sensor at x=";
   static final String Y = "y=";
   static final String BX = "closest beacon is at x=";
   
   static class Coordinate {
      int x;
      int y;
      
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
   
   BufferedReader createReader() throws IOException, URISyntaxException {
      ClassLoader classLoader = getClass().getClassLoader();
      URL resource = classLoader.getResource("2022/day15.input");
      Reader reader = Files.newBufferedReader(Paths.get(resource.toURI()));
      return new BufferedReader(reader);
   }
   
   void loadCoordinates(Map<Coordinate, Coordinate> sensorBeaconMap, Map<Coordinate, Integer> sensorDistanceMap) 
         throws IOException, URISyntaxException {
      try (BufferedReader reader = createReader()) {
         LineReader lineReader = new LineReader(reader, false);
         String line = lineReader.readLine();
         
         while (line != null) {
            readSensorBeacon(line, sensorBeaconMap, sensorDistanceMap);
            line = lineReader.readLine();
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
      
      return 0L;
   }
   
   boolean isCovered(Coordinate sensor, int beaconDistance, Coordinate coordinate) {
      int distance = Math.abs(sensor.x - coordinate.x) + Math.abs(sensor.y - coordinate.y);
      return distance <= beaconDistance;
   }
}
