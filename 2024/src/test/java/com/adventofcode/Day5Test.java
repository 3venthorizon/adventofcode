package com.adventofcode;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day5Test {
   Day5 day5 = new Day5();

   @Test
   void testExample1() {
      long result = day5.part1("day5_example1.input");
      assertEquals(143L, result);
   }

   @Test
   void testExample2() {
      long result = day5.part2("day5_example1.input");
      assertEquals(123L, result);
   }

   @Test
   void testPart1() {
      long result = day5.part1("day5.input");
      assertEquals(5588L, result);
   }

   @Test
   void testPart2() {
      long result = day5.part2("day5.input");
      assertEquals(5331L, result);
   }
}
