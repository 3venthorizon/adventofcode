package com.adventofcode;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Day1Test {
   Day1 day1 = new Day1();

   @Test
   void testExamplePart1() {
      long result = day1.part1("day1_example1.input");
      assertEquals(11L, result);
   }

   @Test
   void testExamplePart2() {
      long result = day1.part2("day1_example1.input");
      assertEquals(31L, result);
   }

   @Test
   void testExtract() {
      String line = "66446   27795";
      Day1.Numbers numbers = day1.extract(line);

      assertEquals(66446L, numbers.left());
      assertEquals(27795L, numbers.right());
   }

   @Test
   void testPart1() {
      assertEquals(3569916L, day1.part1("day1.input"));
   }

   @Test
   void testPart2() {
      assertEquals(26407426L, day1.part2("day1.input"));
   }
}
