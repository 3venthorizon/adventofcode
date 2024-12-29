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

      while (guard != null) {
         locations.add(guard.location());
         guard = move(grid, guard);
      }

      return locations.size();
   }

   Guard move(Grid grid, Guard guard) {
      int position = guard.location();
      Direction direction = guard.direction();
      int location = move(grid, position, direction);
      if (location < 0) return null;

      while ('#' == grid.locations()[location]) {
         direction = turnRight(direction);
         location = move(grid, position, direction);
         if (location < 0) return null;
      }

      return new Guard(location, direction);
   }

   int move(Grid grid, int location, Direction direction) {
      int x = location % grid.width();
      int y = location / grid.width();
      int xpos = x + direction.xdir;
      int ypos = y + direction.ydir;

      if (xpos < 0 || xpos >= grid.width()) return -1;
      if (ypos < 0 || ypos >= grid.heigt()) return -1;

      int position = xpos + (ypos * grid.width());
      return (position < 0 || position >= grid.locations().length) ? -1 : position;
   }

   Direction turnRight(Direction direction) {
      int ordinal = (direction.ordinal() + 1) % Direction.values().length;
      return Direction.values()[ordinal];
   } 

   public long part2(String filename) {
      Grid grid = loadGrid(filename);
      Guard guard = new Guard(grid.start(), Direction.NORTH);
      Set<Integer> locations = new HashSet<>();
      long count = 0L;
      long obstacles = 0L;
      
      while (guard != null) {
         locations.add(guard.location());
         int obstacle = obstacle(grid, guard, locations);
         if (obstacle >= 0) {
            grid.locations()[obstacle] = 'O';
            obstacles++;
         }

         guard = move(grid, guard);
      }

      print(grid, guard);
      
      for (byte location : grid.locations()) {
         if (location == 'O') count++;
      }

      System.out.println("Count: " + count + "\nObstacles: " + obstacles + "\n");
      return count;
   }

   int obstacle(Grid grid, Guard guard, Set<Integer> visited) {
      Direction direction = guard.direction();
      int location = move(grid, guard.location(), direction); //place obstacle directly ahead
      if (location < 0) return -1; //exit board

      direction = turnRight(direction);

      if ('#' == grid.locations()[location]) { //existing obstacle
         location = move(grid, guard.location(), direction); //place obstacle to the right
         if (location < 0 || '#' == grid.locations()[location]) return -1; //exit board

         direction = turnRight(direction); //180
      }

      final int obstacle = location;
      if (visited.contains(obstacle)) return -1;
      
      int position = guard.location();
      Set<Guard> locations = new HashSet<>();
      
      while (position >= 0) {
         location = move(grid, position, direction);
         if (location < 0) return -1; //exit board

         while ('#' == grid.locations()[location] || location == obstacle) {
            direction = turnRight(direction);
            location = move(grid, position, direction);
            if (location < 0) return -1; //exit board
         }

         position = location;
         //the guard, we call him Loopy...
         Guard loopy = new Guard(position, direction);
         if (!locations.add(loopy)) return obstacle;
      }

      return -1;
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

   void print(Grid grid, Guard guard) {
      for (int height = 0; height < grid.heigt(); height++) {
         String line = new String(grid.locations(), height * grid.width(), grid.width());
         System.out.println(line);
      }

      System.out.println();
   }
}
