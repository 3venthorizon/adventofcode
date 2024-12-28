package com.adventofcode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://adventofcode.com/2024/day/3
 */
public class Day3 {
   static final Pattern PATTERN_MUL = Pattern.compile("mul\\(\\d+,\\d+\\)");
   static final Pattern PATTERN_NUMBERS = Pattern.compile("\\d+,\\d+");

   public long part1(String filename) {
      return FileLineStreamer.read(filename)
            .map(this::extractMul)
            .flatMap(List::stream)
            .mapToLong(this::executeMul)
            .sum();
   }

   public long part2(String filename) {
      StringBuilder stringBuilder = new StringBuilder();
      FileLineStreamer.read(filename)
            .forEach(line -> stringBuilder.append(line).append('\n'));
      String instructions = stringBuilder.toString();
      instructions = instructions.replaceAll("don't\\(\\)(.|\n)+?do\\(\\)", "skip");
      
      return extractMul(instructions).stream()
            .mapToLong(this::executeMul)
            .sum();
   }

   List<String> extractMul(String line) {
      Matcher matcher = PATTERN_MUL.matcher(line);
      List<String> list = new ArrayList<>();

      while (matcher.find()) {
         list.add(matcher.group());
      }

      return list;
   }

   long executeMul(String mulString) {
      Matcher matcher = PATTERN_NUMBERS.matcher(mulString);
      matcher.find();
      String group = matcher.group();
      String[] numbers = group.split(",");
      return Long.parseLong(numbers[0]) * Long.parseLong(numbers[1]);
   }
}
