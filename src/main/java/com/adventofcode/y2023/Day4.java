package com.adventofcode.y2023;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.LongStream;

public class Day4 {
   static final Pattern PATTERN_NUMBER = Pattern.compile("\\s\\s?\\d+");

   public long part1(String fileName) {
      return FileLineStreamer.read(fileName)
            .mapToLong(this::cardMatches)
            .filter(matches -> matches > 0L)
            .map(matches -> 1L << (matches - 1L))
            .sum();
   }

   public long part2(String fileName) {
      Map<Long, Long> cardCounterMap = FileLineStreamer.read(fileName)
            .collect(HashMap::new, this::accumulate, this::combine);
      return cardCounterMap.values().stream()
            .reduce(Long::sum)
            .orElse(0L);
   }

   void accumulate(Map<Long, Long> cardCounterMap, String line) {
      Matcher numberMatcher = PATTERN_NUMBER.matcher(line);
      if (!numberMatcher.find()) return;
      Long cardNumber = Long.parseLong(numberMatcher.group().trim());
      long count = cardCounterMap.compute(cardNumber, (key, value) -> value == null ? 1L : value + 1L);
      long matches = cardMatches(line);

      LongStream.rangeClosed(cardNumber + 1, cardNumber + matches)
            .forEach(card -> cardCounterMap.compute(card, (key, value) -> value == null ? count : value + count));
   }

   Map<Long, Long> combine(Map<Long, Long> left, Map<Long, Long> right) {
      Map<Long, Long> combine = new HashMap<>(left);
      combine.putAll(right);

      return combine;
   }

   long cardMatches(String line) {
      int colonIndex = line.indexOf(':');
      int pipeIndex = line.indexOf('|');
      String winningNumbers = line.substring(colonIndex + 1, pipeIndex - 1);
      String cardNumbers = line.substring(pipeIndex + 1);
      long matches = 0L;
      Matcher winningMatcher = PATTERN_NUMBER.matcher(winningNumbers);

      while (winningMatcher.find()) {
         String number = winningMatcher.group();
         if (cardNumbers.contains(number)) matches++;
      }

      return matches;
   }
}
