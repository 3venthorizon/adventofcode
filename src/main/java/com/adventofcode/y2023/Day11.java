package com.adventofcode.y2023;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day11 {
   static final Pattern PATTERN_GALAXY = Pattern.compile("#");

   static class Galaxy {
      long serial, latitude, longitude;

      Galaxy(int serial, long latitude, long longitude) {
         this.serial = serial;
         this.latitude = latitude;
         this.longitude = longitude;
      }
   }

   public long part1(String fileName) {
      return processUniverse(fileName, 2);
   }

   long processUniverse(String fileName, long multiplier) {
      AtomicInteger counter = new AtomicInteger();
      AtomicLong latitude = new AtomicLong();
      List<Galaxy> galaxies = FileLineStreamer.read(fileName)
            .map(line -> extract(line, counter, latitude, multiplier))
            .flatMap(List::stream)
            .collect(Collectors.toList());
      Collections.sort(galaxies, Comparator.comparing(galaxy -> galaxy.longitude));

      for (int index = 1, count = galaxies.size(); index < count; index++) {
         Galaxy previous = galaxies.get(index - 1);   Galaxy next = galaxies.get(index);
         long dlong = next.longitude - previous.longitude;
         
         if (dlong < 2) continue;

         long columns = (dlong - 1L) * multiplier - dlong + 1L;
         galaxies.subList(index, count).forEach(galaxy -> galaxy.longitude += columns);
      }

      long sum = 0L;

      for (int outer = 0, count = galaxies.size(); outer < count; outer++) {
         Galaxy outerGalaxy = galaxies.get(outer);

         for (int inner = outer + 1; inner < count; inner++) {
            Galaxy innerGalaxy = galaxies.get(inner);
            sum += calculateDistance(outerGalaxy, innerGalaxy);
         }
      }
      
      return sum;
   }

   long calculateDistance(Galaxy left, Galaxy right) {
      long dlat = Math.abs(left.latitude - right.latitude);
      long dlong = Math.abs(left.longitude - right.longitude);
      return dlat + dlong;
   }

   public long part2(String fileName) {
      return processUniverse(fileName, 1_000_000L);
   }

   List<Galaxy> extract(String line, AtomicInteger counter, AtomicLong latitude, long multiplier) {
      int serial = counter.get();
      long lat = latitude.incrementAndGet();
      List<Galaxy> galaxies = new ArrayList<>();
      Matcher matcher = PATTERN_GALAXY.matcher(line);

      while (matcher.find()) {
         galaxies.add(new Galaxy(counter.incrementAndGet(), lat, matcher.start()));
      }

      if (serial == counter.get()) latitude.addAndGet(multiplier - 1L);

      return galaxies;
   }
}
