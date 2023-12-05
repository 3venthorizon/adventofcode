package com.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Day5Test {
   Day5 day5 = new Day5();

   @Test
   void testExample1() {
      long result = day5.part1("2023/day5/example1.input");

      assertEquals(35L, result);
   }

   @Test
   void testPart1() {
      long result = day5.part1("2023/day5/data.input");

      System.out.println("Day5 - Part1: " + result);
      assertEquals(621354867L, result);
   }

   @Test
   void testPart2() {

   }
}
