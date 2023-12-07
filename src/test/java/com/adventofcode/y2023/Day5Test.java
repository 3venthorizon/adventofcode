package com.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Day5Test {
   Day5Part1 day5part1 = new Day5Part1();
   Day5Part2 day5part2 = new Day5Part2();

   @Test
   void testExample1() {
      long result = day5part1.part1("2023/day5/example1.input");

      assertEquals(35L, result);
   }

   @Test
   void testPart1() {
      long result = day5part1.part1("2023/day5/data.input");

      System.out.println("Day5 - Part1: " + result);
      assertEquals(621354867L, result);
   }

   @Test
   void testExample2() {
      long result = day5part2.part2("2023/day5/example1.input");

      assertEquals(46L, result);
   }

   @Test
   void testPart2() {
      long result = day5part2.part2("2023/day5/data.input");

      System.out.println("Day5 - Part2: " + result);
      assertEquals(15880236L, result);
   }
}
