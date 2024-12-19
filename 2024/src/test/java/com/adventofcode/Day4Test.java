package com.adventofcode;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day4Test {
   Day4 day4 = new Day4();

   @Test
   void testExample1() {
      long result = day4.part1("day4_example1.input");
      assertEquals(18L, result);
   }

   @Test
   void testExample2() {
      long result = day4.part2("day4_example2.input");
      assertEquals(9L, result);
   }

   @Test
   void testPart1() {
      long result = day4.part1("day4.input");
      assertEquals(2406L, result);
   }

   @Test
   void testPart2() {
      long result = day4.part2("day4.input");
      assertEquals(1807L, result);
   }
}
