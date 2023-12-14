package com.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;

import org.junit.jupiter.api.Test;

import com.adventofcode.y2023.Day10.Grid;

class Day10Test {
   Day10 day10 = new Day10();

   @Test
   void testExample1() {
      Day10.Result result = day10.part1("2023/day10/example1.input");
      
      printMap(result);
      assertEquals(8L, result.path().size() / 2L);
   }
   
   @Test
   void testPart1() {
      Day10.Result result = day10.part1("2023/day10/data.input");
      System.out.println("Day10 - Part1: " + result.path().size() / 2L);
      printMap(result);

      assertEquals(6613L, result.path().size() / 2);
   }

   @Test
   void testPart2() {
      Day10.Result result = day10.part1("2023/day10/data.input");
      SortedSet<Integer> insiders = day10.part2(result);

      System.out.println("Day10 - Part2: " + insiders.size());

      insiders.stream().forEach(insider -> result.map().locations()[insider] = '*');
      result.path().addAll(insiders);

      printMap(result);
      assertEquals(511L, insiders.size());
   }

   void printMap(Day10.Result result) {
      Grid map = result.map();
      List<Integer> path = result.path();
      StringBuilder rowBuilder = new StringBuilder(map.width());
      char[] dots = new char[map.width()];
      Arrays.fill(dots, '.');
      String dotted = new String(dots);
      rowBuilder.append(dotted);

      for (int height = 0; height < map.heigt(); height++) {
         rowBuilder.replace(0, map.width(), dotted);

         for (int width = 0; width < map.width(); width++) {
            int index = height * map.width() + width;
            if (path.contains(index)) rowBuilder.setCharAt(width, (char) map.locations()[index]);
         }

         String row = rowBuilder.toString();
         row = row.replace('-', '\u2550');
         row = row.replace('|', '\u2551');
         row = row.replace('F', '\u2554');
         row = row.replace('L', '\u255A');
         row = row.replace('7', '\u2557');
         row = row.replace('J', '\u255D');
         System.out.println(row);
      }
   }
}
