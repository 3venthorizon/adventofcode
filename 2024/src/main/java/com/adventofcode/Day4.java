package com.adventofcode;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;

/// https://adventofcode.com/2024/day/4
public class Day4 {
   enum Direction {
      NORTH(0, -1),
      SOUTH(0, 1),
      WEST(-1, 0),
      EAST(1, 0),
      NORTH_WEST(-1, -1),
      NORTH_EAST(1, -1),
      SOUTH_WEST(-1, 1),
      SOUTH_EAST(1, 1);

      public final int xdir;
      public final int ydir;

      Direction(int xdir, int ydir) {
         this.xdir = xdir;
         this.ydir = ydir;
      }
   }

   record Grid(byte[] locations, int width, int heigt) {}

   Grid loadGrid(String filename) {
      StringBuilder builder = new StringBuilder();
      AtomicInteger counter = new AtomicInteger();
      Consumer<String> consumer = builder::append;
      consumer = consumer.andThen(_ -> counter.incrementAndGet());
      FileLineStreamer.read(filename).forEach(consumer);

      byte[] locations = builder.toString().getBytes();
      int width = locations.length / counter.get();
      int height = counter.get();
      return new Grid(locations, width, height);
   }

   public long part1(String filename) {
      Grid grid = loadGrid(filename);
      long count = 0L;

      for (int location = 0, end = grid.locations().length; location < end; location++) {
         byte value = grid.locations()[location];
         if (value != 'X') continue;

         int position = location;
         count += Stream.of(Direction.values())
               .filter(direction -> isDirection("XMAS", grid, position, direction.xdir, direction.ydir))
               .count();
      }

      return count;
   }

   public long part2(String filename) {
      Grid grid = loadGrid(filename);
      long count = 0L;
      List<Direction> directionList = List.of(
            Direction.NORTH_WEST, Direction.NORTH_EAST, 
            Direction.SOUTH_WEST, Direction.SOUTH_EAST);

      for (int location = 0, end = grid.locations().length; location < end; location++) {
         byte value = grid.locations()[location];
         if (value != 'A') continue;
         int position = location;
         
         long matches = directionList.stream()
               .filter(direction -> isDirection("AM", grid, position, direction.xdir, direction.ydir))
               .filter(direction -> isDirection("AS", grid, position, -direction.xdir, -direction.ydir))
               .count();
         if (matches == 2) count++;
      }

      return count;
   }

   static boolean isDirection(String search, Grid grid, int location, int xdir, int ydir) {
      int x = location % grid.width();
      int y = location / grid.width();
      byte[] word = search.getBytes();

      for (int index = 1; index < word.length; index++) {
         int ypos = y + (index * ydir);
         int xpos = x + (index * xdir);
         if (xpos < 0 || xpos >= grid.width()) return false;
         if (ypos < 0 || ypos >= grid.locations().length) return false;

         int position = xpos + (ypos * grid.width());
         if (position < 0 || position >= grid.locations().length) return false;
         if (word[index] != grid.locations()[position]) return false;
      }

      return true;
   }
}
