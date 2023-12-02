package com.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * {@link https://adventofcode.com/2023/day/2}
 */
class Day2Test {
   Day2 day2 = new Day2();

   @Test
   void testExample1() {
      long result = day2.part1("2023/day2/example1.input");

      assertEquals(8L, result);
   }
   
   @Test
   void testPart1() {
      long result = day2.part1("2023/day2/data.input");
      
      System.out.println("Day2 - Part1: " + result);
      assertEquals(2331L, result);
   }

   @Test
   void testExample2() {
      long result = day2.part2("2023/day2/example1.input");

      assertEquals(2286L, result);
   }

   @Test
   void testPart2() {
      long result = day2.part2("2023/day2/data.input");
      
      System.out.println("Day2 - Part2: " + result);
      assertEquals(71585L, result);
   }
}
