package com.adventofcode.y2022;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day18 {
   static final String COMMA = ",";
   
   static class Coordinate {
      int x, y, z;
      
      Coordinate(int x, int y, int z) {
         this.x = x;
         this.y = y;
         this.z = z;
      }
      
      @Override
      public int hashCode() {
         return Objects.hash(x, y, z);
      }

      @Override
      public boolean equals(Object object) {
         if (this == object) return true;
         if (!(object instanceof Coordinate)) return false;
         Coordinate other = (Coordinate) object;
         return this.x == other.x && this.y == other.y && this.z == other.z;
      }
      
      @Override
      public String toString() {
         return "[" + x + "," + y + "," + z + "]";
      }
   }
   
   BufferedReader createReader() throws IOException, URISyntaxException {
      ClassLoader classLoader = getClass().getClassLoader();
      URL resource = classLoader.getResource("2022/day18.input");
      Reader reader = Files.newBufferedReader(Paths.get(resource.toURI()));
      return new BufferedReader(reader);
   }
   
   List<Coordinate> readCoordinates() throws IOException, URISyntaxException {
      List<Coordinate> coordinates = new ArrayList<>();
      
      try (BufferedReader reader = createReader()) {
         String line = reader.readLine();
         
         while (line != null) {
            int[] xyz = Stream.of(line.split(COMMA)).mapToInt(Integer::parseInt).toArray();
            coordinates.add(new Coordinate(xyz[0], xyz[1], xyz[2]));
            line = reader.readLine();
         }
      }
      
      return coordinates;
   }
   
   public long part1() throws IOException, URISyntaxException {
      List<Coordinate> coordinates = readCoordinates();
      Map<Integer, List<Coordinate>> xmap = coordinates.stream().collect(Collectors.groupingBy(c -> c.x));
      Map<Integer, List<Coordinate>> ymap = coordinates.stream().collect(Collectors.groupingBy(c -> c.y));
      long total = coordinates.size() * 6L;
      
      for (Coordinate coordinate : coordinates) {
         long xcount = ymap.get(coordinate.y).stream()
               .filter(adjacent -> adjacent.z == coordinate.z)
               .filter(adjacent -> Math.abs(adjacent.x - coordinate.x) == 1)
               .limit(2)
               .count();
         long ycount = xmap.get(coordinate.x).stream()
               .filter(adjacent -> adjacent.z == coordinate.z)
               .filter(adjacent -> Math.abs(adjacent.y - coordinate.y) == 1)
               .limit(2)
               .count();
         long zcount = xmap.get(coordinate.x).stream()
               .filter(adjacent -> adjacent.y == coordinate.y)
               .filter(adjacent -> Math.abs(adjacent.z - coordinate.z) == 1)
               .limit(2)
               .count();
         
         total = total - xcount - ycount - zcount;
      }
      
      return total;
   }
   
   public long part2() throws IOException, URISyntaxException {
      List<Coordinate> coordinates = readCoordinates();
      Map<Integer, List<Coordinate>> xmap = coordinates.stream().collect(Collectors.groupingBy(c -> c.x));
      Map<Integer, List<Coordinate>> ymap = coordinates.stream().collect(Collectors.groupingBy(c -> c.y));
      long total = 0L;
      
      for (Coordinate coordinate : coordinates) {
         IntSummaryStatistics xstats = ymap.get(coordinate.y).stream()
               .filter(adjacent -> adjacent.z == coordinate.z)
               .filter(adjacent -> adjacent.x != coordinate.x)
               .mapToInt(adjacent -> adjacent.x)
               .summaryStatistics();
         if (coordinate.x > xstats.getMax()) total++;
         if (coordinate.x < xstats.getMin()) total++;
         
         
         IntSummaryStatistics ystats = xmap.get(coordinate.x).stream()
               .filter(adjacent -> adjacent.z == coordinate.z)
               .filter(adjacent -> adjacent.y != coordinate.y)
               .mapToInt(adjacent -> adjacent.y)
               .summaryStatistics();
         if (coordinate.y > ystats.getMax()) total++;
         if (coordinate.y < ystats.getMin()) total++;
         
         IntSummaryStatistics zstats = xmap.get(coordinate.x).stream()
               .filter(adjacent -> adjacent.y == coordinate.y)
               .filter(adjacent -> adjacent.z != coordinate.z)
               .mapToInt(adjacent -> adjacent.z)
               .summaryStatistics();
         if (coordinate.z > zstats.getMax()) total++;
         if (coordinate.z < zstats.getMin()) total++;
      }
      
      return total;
   }
   
   int count(Coordinate coordinate, Map<Integer, List<Coordinate>> xmap, Map<Integer, List<Coordinate>> ymap) {
      int count = 0;
      
      return count;
   }
}
