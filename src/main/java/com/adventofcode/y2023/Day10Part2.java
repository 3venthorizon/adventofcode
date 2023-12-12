package com.adventofcode.y2023;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.adventofcode.y2023.Day10Part1.Grid;

public class Day10Part2 {
   public long part2(Day10Part1.Result result) {
      SortedSet<Integer> outsideLocations = new TreeSet<>();

      return 0L;
   }

   void search(int location, Grid map, Set<Integer> path) {
      SortedSet<Integer> outsideLocations = new TreeSet<>();
      
      while (true) {
         List<Integer> options = routes(location, map);
         options.removeAll(path);

         if (options.isEmpty()) return;

         location = options.get(0);
         path.add(location);
      } 
   }

   List<Integer> routes(int location, Grid map) {
      List<Integer> options = new ArrayList<>();

      int x = location % map.width();
      int y = location / map.width();
      int north = (y - 1) * map.width() + x;
      int south = (y + 1) * map.width() + x;
      int west = y * map.width() + (x - 1);
      int east = y * map.width() + (x + 1);

      if (north > 0)                      options.add(north);
      if (south < map.locations().length) options.add(south);
      if (x > 0)                          options.add(west);
      if (x < map.width() - 1)            options.add(east);

      return options;
   }
}
