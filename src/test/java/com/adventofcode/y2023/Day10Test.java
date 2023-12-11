package com.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day10Test {
   Day10 day10 = new Day10();

   @Test
   void testExample1() {
      long result = day10.part1("2023/day10/example1.input");
      
      assertEquals(4L, result);
   }

   @Test
   void testPart1() {
      long result = day10.part1("2023/day10/data.input");
      System.out.println("Day10 - Part1: " + result);
   }

   @Test
   void testPart2() {
   }
}
