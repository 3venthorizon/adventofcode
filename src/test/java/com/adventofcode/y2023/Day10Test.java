package com.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.SortedSet;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.adventofcode.y2023.Day10Part1.Grid;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Day10Test {
   static Day10Part1.Result result;

   Day10Part1 day10part1 = new Day10Part1();

   @Test
   void testExample1() {
      Day10Part1.Result result = day10part1.part1("2023/day10/example1.input");
      
      assertEquals(8L, result.path().size() / 2);
   }
   
   @Test
   @Order(1)
   void testPart1() {
      result = day10part1.part1("2023/day10/data.input");
      System.out.println("Day10 - Part1: " + result);
   }

   @Test
   @Order(2)
   void testPart2() {
   }

   void printMap(SortedSet<Integer> path, Grid map) {
      StringBuilder rowBuilder = new StringBuilder(map.width());
      char[] dots = new char[map.width()];
      Arrays.fill(dots, '.');
      String dotted = new String(dots);
      rowBuilder.append(dotted);

      for (int height = 0; height < map.heigt(); height++) {
         rowBuilder.replace(0, map.width(), dotted);

         for (int width = 0; width < map.width(); width++) {
            int index = height * map.width() + width;
            if (!path.contains(index)) continue;

            rowBuilder.setCharAt(width, (char) map.locations()[index]);
         }

         System.out.println(rowBuilder.toString());
      }
   }
}
