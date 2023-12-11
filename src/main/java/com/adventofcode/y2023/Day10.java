package com.adventofcode.y2023;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Day10 {
   record Grid(byte[] locations, int width, int heigt, int startIndex) {}

   public long part1(String fileName) {
      StringBuilder builder = new StringBuilder();
      AtomicInteger counter = new AtomicInteger();
      Consumer<String> consumer = builder::append;
      consumer = consumer.andThen(line -> counter.incrementAndGet());
      FileLineStreamer.read(fileName).forEach(consumer);

      byte[] locations = builder.toString().getBytes();
      int width = locations.length / counter.get();
      int height = counter.get();
      int startIndex = builder.indexOf("S");
      Grid grid = new Grid(locations, width, height, startIndex);
      counter.set(0);

      Function<Integer, List<Integer>> router = location -> routes(location, grid);
      Predicate<Integer> destination = location -> destination(location, grid, counter);

      return 0L;
   }

   List<Integer> routes(int location, Grid map) {
      List<Integer> options = new ArrayList<>();

      int x = location % map.width();
      int y = location / map.width();
      int north = (y - 1) * map.width() + x;
      int south = (y + 1) * map.width() + x;
      int west = y * map.width() + (x - 1);
      int east = y * map.width() + (x + 1);

      if (north > 0) options.add(north);
      if (south < map.locations.length);
      if (x > 0);
      if (x < map.width() - 1);


      return options;
   }

   

   boolean destination(int location, Grid map, AtomicInteger counter) {
      int count = counter.incrementAndGet();
      if (count <= 2) return false;
      return false;
   }

   public long part2(String fileName) {
      return 0L;
   }
}
