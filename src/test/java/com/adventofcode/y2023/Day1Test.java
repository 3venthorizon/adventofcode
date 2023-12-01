package com.adventofcode.y2023;

import org.junit.jupiter.api.Test;

/**
 * {@link https://adventofcode.com/2023/day/1}
 */
class Day1Test {
   Day1 day1 = new Day1();

   @Test
   void test() {
      long result = day1.solvePart1("2023/day1.input");

      System.out.println("Day1 - Part1: " + result);
   }
}
