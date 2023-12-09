package com.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

class Day8Test {
   Day8 day8 = new Day8();

   @Test
   void testExample1() {
      long result = day8.part1("2023/day8/example1.input");

      assertEquals(2L, result);
   }

   @Test
   void testExample2() {
      long result = day8.part1("2023/day8/example2.input");

      assertEquals(6L, result);
   }

   @Test
   void testPart1() {
      long result = day8.part1("2023/day8/data.input");

      System.out.println("Day8 - Part1: " + result);
      assertEquals(12361L, result);
   }

   @Test
   void testExample3() {
      long result = day8.part2("2023/day8/example3.input");

      assertEquals(6L, result);
   }

   @Test
   void testPart2() {
      long result = day8.part2("2023/day8/data.input");

      System.out.println("Day8 - Part2: " + result);
      assertEquals(18215611419223L, result);
   }
}
