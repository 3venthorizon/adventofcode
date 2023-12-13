package com.adventofcode.y2023;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.adventofcode.y2023.Day10Part1.Grid;

public class Day10Part2 {
   public SortedSet<Integer> part2(Day10Part1.Result result) {
      return new TreeSet<>(Graph.breadthFirstSearch(0, location -> routes(location, result), location -> false));
   }

   List<Integer> routes(int location, Day10Part1.Result result) {
      Grid map = result.map();
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

      options.removeIf(result.path()::contains);
      return options;
   }
}
