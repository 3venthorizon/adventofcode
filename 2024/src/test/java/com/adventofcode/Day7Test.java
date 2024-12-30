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
   void testPart1() {
      long result = day7.part1("day7.input");
      assertEquals(1582598718861L, result);
   }

   @Test
   void testPart2() {

   }
}
