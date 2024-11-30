package com.adventofcode.y2023;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

public class Day10 {
   public record Result(List<Integer> path, Grid map) {}

   record Grid(byte[] locations, int width, int heigt, int startIndex) {}

   record Directions(int x, int y, int north, int south, int west, int east) {}

   record Orientation(int port, int starboard) {}

   public Result part1(String fileName) {
      StringBuilder builder = new StringBuilder();
      AtomicInteger counter = new AtomicInteger();
      Consumer<String> consumer = builder::append;
      consumer = consumer.andThen(line -> counter.incrementAndGet());
      FileLineStreamer.read(fileName).forEach(consumer);

      byte[] locations = builder.toString().getBytes();
      int width = locations.length / counter.get();
      int height = counter.get();
      int startIndex = builder.indexOf("S");
      Grid grid = new Grid(locations, width, height, startIndex);
      grid.locations[startIndex] = (byte) startPipe(grid);
      List<Integer> path = searchPart1(startIndex, grid);

      return new Result(path, grid);
   }

   List<Integer> searchPart1(int location, Grid map) {
      SortedSet<Integer> visited = new TreeSet<>();
      List<Integer> path = new ArrayList<>();
      visited.add(location);
      path.add(location);

      while (true) {
         Directions directions = directions(location, map.width());
         List<Integer> options = pipeRoutes(location, map, directions);
         options.removeIf(visited::contains);

         if (options.isEmpty()) return path;

         location = options.get(0);
         visited.add(location);
         path.add(location);
      } 
   }

   char startPipe(Grid map) {
      Directions directions = directions(map.startIndex(), map.width());

      if (northSouth('|', (char) map.locations[directions.south()])
         && northSouth((char) map.locations[directions.north()], '|')) return '|';
      if (westEast('-', (char) map.locations[directions.east()])
         && westEast((char) map.locations[directions.west()], '-')) return '-';
      if (northSouth('F', (char) map.locations[directions.south()])
         && westEast('F', (char) map.locations[directions.east()])) return 'F';
      if (northSouth((char) map.locations[directions.north()], 'L')
         && westEast('L', (char) map.locations[directions.east()])) return 'L';
      if (northSouth((char) map.locations[directions.north()], 'J')
         && westEast((char) map.locations[directions.west()], 'J')) return 'J';
      if (northSouth('7', (char) map.locations[directions.south()])
         && westEast((char) map.locations[directions.west()], '7')) return '7';

      return 'S';
   }

   List<Integer> pipeRoutes(int location, Grid map, Directions directions) {
      List<Integer> options = new ArrayList<>();
      char current = (char) map.locations()[location];

      if (directions.north() >= 0) {
         char north = (char) map.locations[directions.north()];
         if (northSouth(north, current)) options.add(directions.north());
      }
      if (directions.south() < map.locations.length) {
         char south = (char) map.locations[directions.south()];
         if (northSouth(current, south)) options.add(directions.south());
      }
      if (directions.x() > 0) {
         char west = (char) map.locations[directions.west()];
         if (westEast(west, current)) options.add(directions.west());
      }
      if (directions.x() < map.width() - 1) {
         char east = (char) map.locations[directions.east()];
         if (westEast(current, east)) options.add(directions.east());
      }

      return options;
   }

   boolean northSouth(char north, char south) {
      return switch (north) {
         case '|', 'F', '7' -> (south == '|' || south == 'L' || south == 'J');
         default -> false;
      };
   }

   boolean westEast(char west, char east) {
      return switch (west) {
         case '-', 'F', 'L' -> (east == '-' || east == '7' || east == 'J');
         default -> false;
      };
   }

   public SortedSet<Integer> part2(Result result) {
      Grid map = result.map();
      SortedSet<Integer> port = new TreeSet<>();
      SortedSet<Integer> starboard = new TreeSet<>();
      List<Integer> path = result.path();
      SortedSet<Integer> trail = new TreeSet<>(path);
      
      for (int index = 1, count = path.size(); index < count; index++) {
         int aft = path.get(index - 1);
         int bow = path.get(index);
         Orientation orientation = orientation(aft, bow, map.width(), map.heigt());
         int bowPort = bow + orientation.port();
         int bowStarboard = bow + orientation.starboard();
         int aftPort = aft + orientation.port();
         int aftStarboard = aft + orientation.starboard();

         if (!trail.contains(bowPort)) port.add(bowPort);
         if (!trail.contains(aftPort)) port.add(aftPort);
         if (!trail.contains(bowStarboard)) starboard.add(bowStarboard);
         if (!trail.contains(aftStarboard)) starboard.add(aftStarboard);
      }

      SortedSet<Integer> siders = port.size() < starboard.size() ? port : starboard;
      SortedSet<Integer> outsiders = port.size() > starboard.size() ? port : starboard;
      SortedSet<Integer> insiders = new TreeSet<>();
      Function<Integer, List<Integer>> router = location -> insideRoutes(location, map, trail, outsiders);

      for (Integer side : siders) {
         if (insiders.contains(side)) continue;

         List<Integer> locations = Graph.breadthFirstSearch(side, router, location -> false);
         insiders.add(side);
         insiders.addAll(locations);
      }

      return insiders;
   }

   List<Integer> insideRoutes(int location, Grid map, 
         SortedSet<Integer> trail, SortedSet<Integer> outsiders) {
      Directions directions = directions(location, map.width());
      List<Integer> routes = new ArrayList<>();

      if (directions.north() >= 0) routes.add(directions.north());
      if (directions.south() < map.locations().length) routes.add(directions.south());
      if (directions.x() > 0) routes.add(directions.west);
      if (directions.x() < map.width() - 1) routes.add(directions.east());

      routes.removeIf(trail::contains);
      routes.removeIf(outsiders::contains);

      return routes;
   }

   static Directions directions(int location, int width) {
      int x = location % width;
      int y = location / width;
      int north = (y - 1) * width + x;
      int south = (y + 1) * width + x;
      int west = y * width + (x - 1);
      int east = y * width + (x + 1);

      return new Directions(x, y, north, south, west, east);
   }

   static Orientation orientation(int aft, int bow, int width, int height) {
      int x = bow % width;
      int y = bow / width;
      int dx = (aft % width) - (bow % width);
      int dy = (aft / width) - (bow / width);

      int port = 0;                    
      int portX = x + dy;                             
      int portY = y - dx;                             
      port += (portX >= 0 && portX < width) ? dy : 0; 
      port -= (portY >= 0 && portY < height) ? dx * width : 0;

      int starboard = 0;
      int starboardX = x - dy;
      int starboardY = y + dx;
      starboard -= (starboardX >= 0 && starboardX < width) ? dy : 0;
      starboard += (starboardY >= 0 && starboardY < height) ? dx * width : 0;

      return new Orientation(port, starboard);
   }
}
