package com.adventofcode.y2023;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day11 {
   static final Pattern PATTERN_GALAXY = Pattern.compile("#");

   record Galaxy(int serial, int latitude, int logitude) {}

   public long part1(String fileName) {
      AtomicInteger counter = new AtomicInteger();
      AtomicInteger latitude = new AtomicInteger();
      List<Galaxy> galaxies = FileLineStreamer.read(fileName)
            .map(line -> extract(line, counter, latitude))
            .flatMap(List::stream)
            .toList();
      return 0L;
   }

   public long part2(String fileName) {
      return 0L;
   }

   List<Galaxy> extract(String line, AtomicInteger counter, AtomicInteger latitude) {
      int serial = counter.get();
      int lat = latitude.incrementAndGet();
      List<Galaxy> galaxies = new ArrayList<>();
      Matcher matcher = PATTERN_GALAXY.matcher(line);

      while (matcher.find()) {
         serial = counter.incrementAndGet();
         galaxies.add(new Galaxy(serial, lat, matcher.start()));
      }

      if (serial == counter.get()) latitude.incrementAndGet();

      return galaxies;
   }
}
