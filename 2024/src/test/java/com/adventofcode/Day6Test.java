package com.adventofcode;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day6Test {
   Day6 day6 = new Day6();

   @Test
   void testExample1() {
      long result = day6.part1("day6_example1.input");
      assertEquals(41L, result);
   }

   @Test
   void testExample2() {
      long result = day6.part2("day6_example1.input");
      assertEquals(6L, result);
   }

   @Test
   void testPart1() {
      long result = day6.part1("day6.input");
      assertEquals(4973L, result);
   }

   @Test
   void testPart2() {
      long result = day6.part2("day6.input");
      assertEquals(1618L, result);
   }
}
