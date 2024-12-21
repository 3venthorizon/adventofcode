package com.adventofcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/// https://adventofcode.com/2024/day/5
public class Day5 {
   record Rule(String before, String after) {}

   public long part1(String filename) {
      Map<String, List<Rule>> pageRuleMap = new HashMap<>();
      List<List<String>> updates = new ArrayList<>();
      FileLineStreamer.read(filename).forEach(line -> append(line, pageRuleMap, updates));
      long total = 0L;

      OUTER: for (List<String> update : updates) {
         for (String page : update) {
            List<Rule> rules = pageRuleMap.get(page);
            if (rules == null) continue;
            if (!rules.stream().allMatch(rule -> ruleMatch(rule, update))) continue OUTER;
         }

         int index = update.size() / 2;
         long pageNumber = Long.parseLong(update.get(index));
         total += pageNumber;
      }

      return total;
   }

   boolean ruleMatch(Rule rule, List<String> update) {
      int beforeIndex = update.indexOf(rule.before());
      if (beforeIndex < 0) return true;
      int aftterIndex = update.indexOf(rule.after());
      if (aftterIndex < 0) return true;
      return beforeIndex < aftterIndex;
   }

   public long part2(String filename) {
      Map<String, List<Rule>> pageRuleMap = new HashMap<>();
      List<List<String>> updates = new ArrayList<>();
      FileLineStreamer.read(filename).forEach(line -> append(line, pageRuleMap, updates));
      long total = 0L;

      for (List<String> update : updates) {
         boolean isSorted = true;

         for (String page : update) {
            List<Rule> rules = pageRuleMap.get(page);
            if (rules == null) continue;
            isSorted &= rules.stream().allMatch(rule -> ruleMatch(rule, update));
         }

         if (isSorted) continue;

         List<String> sorted = new ArrayList<>(update);
         sorted.sort((left, right) -> compare(pageRuleMap, left, right));
         System.out.println(update + " : " + sorted);
         int index = sorted.size() / 2;
         long pageNumber = Long.parseLong(sorted.get(index));
         total += pageNumber;
      }

      return total;
   }

   int compare(Map<String, List<Rule>> pageRuleMap, String left, String right) {
      List<Rule> rules = pageRuleMap.get(left);
      if (rules == null) return 0;
      Rule rule = rules.stream()
            .filter(match -> right.equals(match.before()) || right.equals(match.after()))
            .findFirst()
            .orElse(null);
      if (rule == null) return 0;
      return right.equals(rule.after()) ? 1 : -1;
   }

   void append(String line, Map<String, List<Rule>> pageRuleMap, List<List<String>> updates) {
      if (line.contains("|")) {
         String[] pages = line.split("\\|");
         Rule rule = new Rule(pages[0], pages[1]);
         pageRuleMap.computeIfAbsent(pages[0], _ -> new ArrayList<>()).add(rule);
         pageRuleMap.computeIfAbsent(pages[1], _ -> new ArrayList<>()).add(rule);
      } else if (line.contains(",")) {
         String[] pages = line.split(",");
         updates.add(List.of(pages));
      }
   }
}
