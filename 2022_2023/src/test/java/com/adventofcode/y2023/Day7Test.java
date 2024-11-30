package com.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day7Test {
   Day7 day7 = new Day7();

   @Test
   void testExample1() {
      long result = day7.part1("2023/day7/example1.input");

      assertEquals(6440L, result);
   }

   @Test
   void testPart1() {
      long result = day7.part1("2023/day7/data.input");
      System.out.println("Day7 - Part1: " + result);

      assertEquals(248113761L, result);
   }

   @Test
   void testExample2() {
      long result = day7.part2("2023/day7/example1.input");

      assertEquals(5905L, result);
   }

   @Test
   void testPart2() {
      long result = day7.part2("2023/day7/data.input");
      System.out.println("Day7 - Part2: " + result);

      assertEquals(246285222L, result);
   }
}
