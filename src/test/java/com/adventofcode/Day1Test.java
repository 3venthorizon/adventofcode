package com.adventofcode;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;

class Day1Test {

   Day1 day1 = new Day1();
   
   @Test
   void testMaxSum() throws IOException, URISyntaxException {
      System.out.println(day1.maxSum());
   }
   
   @Test
   void testTop3Sum() throws IOException, URISyntaxException {
      System.out.println(day1.top3Sum());
   }

}
