package com.adventofcode.y2023;

import java.util.Map;
import java.util.stream.LongStream;

public class Day6 {
   public long part(Map<Long, Long> timeDistanceMap) {
      long result = 1;

      for (Map.Entry<Long, Long> entry : timeDistanceMap.entrySet()) {
         final long time = entry.getKey();
         final long recordDistance = entry.getValue();
         long count = LongStream.range(1, time)
               .map(chargeTime -> chargeTime * (time - chargeTime))
               .filter(distance -> distance > recordDistance)
               .count();
         result *= count;
      }

      return result;
   }
}
