package com.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

import com.adventofcode.y2023.Day10Part1.Grid;

class Day10Test {
   static Day10Part1.Result result;

   Day10Part1 day10part1 = new Day10Part1();
   Day10Part2 day10Part2 = new Day10Part2();

   @Test
   void testExample1() {
      Day10Part1.Result result = day10part1.part1("2023/day10/example1.input");
      
      printMap(result);
      assertEquals(8L, result.path().size() / 2L);
   }
   
   @Test
   void testPart1() {
      result = day10part1.part1("2023/day10/data.input");
      System.out.println("Day10 - Part1: " + result.path().size() / 2L);
      printMap(result);

      assertEquals(6613L, result.path().size() / 2);
   }

   @Test
   void testPart2() {
      result = day10part1.part1("2023/day10/data.input");
      SortedSet<Integer> outsiders = day10Part2.part2(result);
      
      printMap(result);
      System.out.println("Total: " + result.map().locations().length);
      System.out.println("Outsiders: " + outsiders.size());
      System.out.println("Paths: " + result.path().size());
      long insiders = result.map().locations().length - outsiders.size() - result.path().size();
      System.out.println("Insiders: " + insiders);
   }

   void printMap(Day10Part1.Result result) {
      Grid map = result.map();
      SortedSet<Integer> path = result.path();
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
