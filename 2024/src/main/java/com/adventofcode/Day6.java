package com.adventofcode;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/// https://adventofcode.com/2024/day/6
public class Day6 {
   enum Direction {
      NORTH(0, -1),
      EAST(1, 0),
      SOUTH(0, 1),
      WEST(-1, 0);

      public final int xdir;
      public final int ydir;

      Direction(int xdir, int ydir) {
         this.xdir = xdir;
         this.ydir = ydir;
      }
   }

   record Grid(byte[] locations, int width, int heigt, int start) {}

   record Guard(int location, Direction direction) {}

   public long part1(String filename) {
      Grid grid = loadGrid(filename);
      Set<Integer> locations = new HashSet<>();
      Guard guard = new Guard(grid.start(), Direction.NORTH);

      print(grid, guard);

      while (guard != null) {
         locations.add(guard.location());
         guard = move(grid, guard);
      }

      return locations.size();
   }

   Guard move(Grid grid, Guard guard) {
      int location = guard.location();
      int x = location % grid.width();
      int y = location / grid.heigt();
      Direction direction = guard.direction();
      int xpos = x + direction.xdir;
      int ypos = y + direction.ydir;

      if (xpos < 0 || xpos >= grid.width()) return null;
      if (ypos < 0 || ypos >= grid.heigt()) return null;

      int position = xpos + (ypos * grid.width());
      if (position < 0 || position >= grid.locations().length) return null;
      if ('#' == grid.locations()[position]) {
         int ordinal = (direction.ordinal() + 1) % Direction.values().length;
         direction = Direction.values()[ordinal];
         return move(grid, new Guard(location, direction));
      }

      return new Guard(position, direction);
   }

   void print(Grid grid, Guard guard) {
      for (int height = 0; height < grid.heigt(); height++) {
         String line = new String(grid.locations(), height * grid.width(), grid.width());
         System.out.println(line);
      }
   }

   public long part2(String filename) {
      return 0L;
   }

   Grid loadGrid(String filename) {
      StringBuilder builder = new StringBuilder();
      AtomicInteger counter = new AtomicInteger();
      Consumer<String> consumer = builder::append;
      consumer = consumer.andThen(_ -> counter.incrementAndGet());
      FileLineStreamer.read(filename).forEach(consumer);

      String board = builder.toString();
      int start = board.indexOf('^');
      byte[] locations = board.getBytes();
      int width = locations.length / counter.get();
      int height = counter.get();
      return new Grid(locations, width, height, start);
   }
}
