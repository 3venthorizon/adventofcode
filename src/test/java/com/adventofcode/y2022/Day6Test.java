package com.adventofcode.y2022;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class Day6Test {
   Day6 test = new Day6();

   @Test
   void testPart1() throws IOException {
      System.out.println(test.distinctPosition(4));
   }
   
   @Test
   void testPart2() throws IOException {
      System.out.println(test.distinctPosition(14));
   }
}
