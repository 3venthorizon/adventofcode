package com.adventofcode;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day3Test {
   Day3 day3 = new Day3();

   @Test
   void testExample1() {
      long result = day3.part1("day3_example1.input");
      assertEquals(161L, result);
   }

   @Test
   void testExample2() {
      long result = day3.part2("day3_example2.input");
      assertEquals(48L, result);
   }

   @Test
   void testPart1() {
      long result = day3.part1("day3.input");
      assertEquals(155955228L, result);
   }

   @Test
   void testPart2() {
      long result = day3.part2("day3.input");
      assertEquals(100189366L, result);
   }
}
