package com.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Day4Test {
   Day4 day4 = new Day4();

   @Test
   void testExample1() {
      long result = day4.part1("2023/day4/example1.input");

      assertEquals(13L, result);
   }

   @Test
   void testPart1() {
      long result = day4.part1("2023/day4/data.input");

      System.out.println("Day4 - Part1: " + result);
      assertEquals(32609L, result);
   }

   @Test
   void testExample2() {
      long result = day4.part2("2023/day4/example1.input");

      assertEquals(30L, result);
   }

   @Test
   void testPart2() {
      long result = day4.part2("2023/day4/data.input");

      System.out.println("Day4 - Part2: " + result);
   }
}
