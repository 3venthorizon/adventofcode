package com.adventofcode.y2023;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Day10 {
   record Grid(byte[] locations, int width, int heigt, int startIndex) {}


   public long part1(String fileName) {
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
      counter.set(0);

      Function<Integer, List<Integer>> router = location -> routes(location, grid);
      Predicate<Integer> destination = location -> destination(location, grid, counter);
      List<Integer> path = Graph.breadthFirstSearch(startIndex, router, destination);

      return path.size() / 2L + 1L;
   }

   char startPipe(Grid map) {
      int x = map.startIndex % map.width;
      int y = map.startIndex / map.width;
      int north = (y - 1) * map.width + x;
      int south = (y + 1) * map.width + x;
      int west = y * map.width + (x - 1);
      int east = y * map.width + (x + 1);

      if (northSouth('|', map.locations[south])
         && northSouth(map.locations[north], '|')) return '|';
      if (westEast('-', map.locations[east])
         && westEast(map.locations[west], '-')) return '-';
      if (northSouth('F', map.locations[south])
         && westEast('F', map.locations[east])) return 'F';
      if (northSouth(map.locations[north], 'L')
         && westEast('L', map.locations[east])) return 'L';
      if (northSouth(map.locations[north], 'J')
         && westEast(map.locations[west], 'J')) return 'J';
      if (northSouth('7', map.locations[south])
         && westEast(map.locations[west], '7')) return '7';

      return 'S';
   }

   List<Integer> routes(int location, Grid map) {
      List<Integer> options = new ArrayList<>();

      int x = location % map.width;
      int y = location / map.width;
      int north = (y - 1) * map.width + x;
      int south = (y + 1) * map.width + x;
      int west = y * map.width + (x - 1);
      int east = y * map.width + (x + 1);

      if (north > 0 
         && northSouth(map.locations[north], map.locations[location]))  options.add(north);
      if (south < map.locations.length
         && northSouth(map.locations[location], map.locations[south]))  options.add(south);
      if (x > 0
         && westEast(map.locations[west], map.locations[location]))     options.add(west);
      if (x < map.width() - 1
         && westEast(map.locations[location], map.locations[east]))     options.add(east);

      options = options.subList(0, 1);

      System.out.println("Location: " + location + " [" 
         + String.valueOf((char) map.locations[location]) + "] -> [" 
         + String.valueOf((char) map.locations[options.get(0)]) + "]");

      return options;
   }

   boolean northSouth(int north, int south) {
      return switch (north) {
         case '|', 'F', '7' -> (south == '|' || south == 'L' || south == 'J');
         default -> false;
      };
   }

   boolean westEast(int west, int east) {
      return switch (west) {
         case '-', 'F', 'L' -> (east == '-' || east == '7' || east == 'J');
         default -> false;
      };
   }

   boolean destination(int location, Grid map, AtomicInteger counter) {
      int count = counter.incrementAndGet();
      if (count <= 4) return false;
      
      List<Integer> options = routes(location, map);
      return options.contains(map.startIndex);
   }

   public long part2(String fileName) {
      return 0L;
   }
}
