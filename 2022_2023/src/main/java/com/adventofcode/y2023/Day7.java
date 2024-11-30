package com.adventofcode.y2023;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day7 {
   record Hand(String cards, int bid) {}

   enum Type { HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE_KIND, FULL_HOUSE, FOUR_KIND, FIVE_KIND }

   static final String CARD_ORDER_RANK = "23456789TJQKA";
   static final String CARD_JOKER_RANK = "J23456789TQKA";

   public long part1(String fileName) {
      List<Hand> hands = FileLineStreamer.read(fileName)
            .map(this::extractHand)
            .sorted(this::comparePart1)
            .toList();
      
      long winnings = 0L;
      
      for (int index = 0, count = hands.size(); index < count; index++) {
         Hand hand = hands.get(index);
         winnings += hand.bid * (index + 1L);
      }
      
      return winnings;
   }

   public long part2(String fileName) {
      List<Hand> hands = FileLineStreamer.read(fileName)
            .map(this::extractHand)
            .sorted(this::comparePart2)
            .toList();
      
      long winnings = 0L;
      
      for (int index = 0, count = hands.size(); index < count; index++) {
         Hand hand = hands.get(index);
         winnings += hand.bid * (index + 1L);
      }
      
      return winnings;
   }

   Hand extractHand(String line) {
      String[] handBid = line.split(" ");
      return new Hand(handBid[0], Integer.parseInt(handBid[1]));
   }

   int comparePart1(Hand left, Hand right) {
      int compared = Integer.compare(classify(left).ordinal(), classify(right).ordinal());
      if (compared != 0) return compared;

      return rankCard(CARD_ORDER_RANK, left, right);
   }

   int comparePart2(Hand left, Hand right) {
      int compared = Integer.compare(joker(left).ordinal(), joker(right).ordinal());
      if (compared != 0) return compared;

      return rankCard(CARD_JOKER_RANK, left, right);
   }

   int compare(Hand left, Hand right) {
      int compared = Integer.compare(classify(left).ordinal(), classify(right).ordinal());
      if (compared != 0) return compared;

      for (int index = 0, length = left.cards.length(); index < length; index++) {
         int leftRank = CARD_ORDER_RANK.indexOf(left.cards.charAt(index));
         int rightRank = CARD_ORDER_RANK.indexOf(right.cards.charAt(index));

         compared = Integer.compare(leftRank, rightRank);
         if (compared != 0) return compared;
      }

      return 0;
   }

   int rankCard(String cardRank, Hand left, Hand right) {
      for (int index = 0, length = left.cards.length(); index < length; index++) {
         int leftRank = cardRank.indexOf(left.cards.charAt(index));
         int rightRank = cardRank.indexOf(right.cards.charAt(index));

         int compared = Integer.compare(leftRank, rightRank);
         if (compared != 0) return compared;
      }

      return 0;
   }

   Type classify(Hand hand) {
      Map<Integer, Long> cardCountMap = hand.cards.chars().boxed()
            .collect(Collectors.groupingBy(Integer::intValue, Collectors.counting()));

      if (cardCountMap.size() == 1) return Type.FIVE_KIND;
      if (cardCountMap.size() == 5) return Type.HIGH_CARD;
      if (cardCountMap.size() == 4) return Type.ONE_PAIR;
      if (cardCountMap.size() == 2) {
         boolean fourKind = cardCountMap.values().stream().mapToLong(Long::longValue).anyMatch(value -> value == 4);
         return fourKind ? Type.FOUR_KIND : Type.FULL_HOUSE;
      }

      boolean trheeKind = cardCountMap.values().stream().mapToLong(Long::longValue).anyMatch(value -> value == 3);
      return trheeKind ? Type.THREE_KIND : Type.TWO_PAIR;
   }

   Type joker(Hand hand) {
      String noJokers = hand.cards.replace("J", "");
      Type bestType = classify(hand);

      if (noJokers.length() == 5) return bestType;
      if (noJokers.length() == 0) return Type.FIVE_KIND;

      for (int index = 0, length = noJokers.length(); index < length; index++) {
         String cards = hand.cards.replace('J', noJokers.charAt(index));
         Hand replaceHand = new Hand(cards, hand.bid);
         Type joker = classify(replaceHand);

         if (joker.ordinal() > bestType.ordinal()) bestType = joker;
      }

      return bestType;
   }
}
