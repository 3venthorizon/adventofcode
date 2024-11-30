package com.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

class Day6Test {
   Day6 day6 = new Day6();

   @Test
   void testExample1() {
      Map<Long, Long> timeDistanceMap = Map.of(7L, 9L, 15L, 40L, 30L, 200L);
      long result = day6.part(timeDistanceMap);

      assertEquals(288L, result);
   }

   @Test
   void testPart1() {
      Map<Long, Long> timeDistanceMap = Map.of(56L, 334L, 71L, 1135L, 79L, 1350L, 99L, 2430L);
      long result = day6.part(timeDistanceMap);

      System.out.println("Day6 - Part1: " + result);
      assertEquals(211904L, result);
   }

   @Test
   void testPart2() {
      Map<Long, Long> timeDistanceMap = Map.of(56717999L, 334113513502430L);
      long result = day6.part(timeDistanceMap);

      System.out.println("Day6 - Part2: " + result);
      assertEquals(43364472L, result);
   }
}
