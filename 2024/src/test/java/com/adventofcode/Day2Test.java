package com.adventofcode;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day2Test {
   Day2 day2 = new Day2();

   @Test
   void testExamplePart1() {
      long result = day2.part1("day2_example1.input");
      assertEquals(2L, result);
   }

   @Test
   void testExamplePart2() {
      long result = day2.part2("day2_example1.input");
      assertEquals(4L, result);
   }

   @Test
   void testPart1() {
      long result = day2.part1("day2.input");
      assertEquals(486L, result);
   }

   @Test
   void testPart2() {
      long result = day2.part2("day2.input");
      assertEquals(540L, result);
   }
}
