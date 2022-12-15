package com.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.adventofcode.y2022.Day6;

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
