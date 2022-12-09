package com.adventofcode;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class Day6Test {
   
   Day6 day6 = new Day6();

   @Test
   void testPart1() throws IOException {
      System.out.println(day6.distinctPosition(4));
   }
   
   @Test
   void testPart2() throws IOException {
      System.out.println(day6.distinctPosition(14));
   }
}
