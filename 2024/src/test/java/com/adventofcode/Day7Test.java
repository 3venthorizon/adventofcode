package com.adventofcode;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day7Test {
   Day7 day7 = new Day7();
   
   @Test
   void testExample1() {
      long result = day7.part1("day7_example1.input");
      assertEquals(3749L, result);
   }

   @Test
   void testExample2() {
      long result = day7.part2("day7_example1.input");
      assertEquals(11387L, result);
   }

   @Test
   void testPart1() {
      long result = day7.part1("day7.input");
      assertEquals(1582598718861L, result);
   }

   @Test
   void testPart2() {
      long result = day7.part2("day7.input");
      assertEquals(165278151522644L, result);
   }
}
