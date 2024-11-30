package com.adventofcode.y2022;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;

class Day1Test {
   Day1 test = new Day1();
   
   @Test
   void testMaxSum() throws IOException, URISyntaxException {
      System.out.println(test.maxSum());
   }
   
   @Test
   void testTop3Sum() throws IOException, URISyntaxException {
      System.out.println(test.top3Sum());
   }

}
