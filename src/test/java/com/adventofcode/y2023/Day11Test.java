package com.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day11Test {
   Day11 day11 = new Day11();

   @Test
   void testExample1() {
      long result = day11.part1("2023/day11/example1.input");

      System.out.println("Day11 - Example1: " + result);
      assertEquals(374L, result);
   }

   @Test
   void testPart1() {
      long result = day11.part1("2023/day11/data.input");

      System.out.println("Day11 - Part1: " + result);
      assertEquals(9918828L, result);
   }

   @Test
   void testPart2() {
      long result = day11.part2("2023/day11/data.input");

      System.out.println("Day11 - Part2: " + result);
   }
}
