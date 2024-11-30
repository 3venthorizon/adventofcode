package com.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

class Day9Test {
   Day9 day9 = new Day9();

   @Test
   void testExample1() {
      long result = day9.part1("2023/day9/example1.input");

      assertEquals(114L, result);
   }

   @Test
   void testPart1() {
      long result = day9.part1("2023/day9/data.input");

      System.out.println("Day9 - Part1: " + result);
      assertEquals(1666172641L, result);
   }

   @Test
   void testExample2() {
      long result = day9.part2("2023/day9/example1.input");

      assertEquals(2L, result);
   }

   @Test
   void testBackwardExtrapolation() {
      assertEquals(5, day9.extrapolate(List.of(10, 13, 16, 21, 30, 45), day9::backward));
   }

   @Test
   void testPart2() {
      long result = day9.part2("2023/day9/data.input");

      System.out.println("Day9 - Part2: " + result);
      assertEquals(933L, result);
   }
}
