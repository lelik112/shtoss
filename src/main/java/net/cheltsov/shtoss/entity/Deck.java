package net.cheltsov.shtoss.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum Deck {
    SMALL(6), BIG(2);

    private static final int PLAYING_CARD_BACK = 0x1F0A0;
    private static final int NUMBER_OF_SUIT = 4;
    private static final int STEP = 16;
    private final Set<Integer> deck = new HashSet<>(STEP * NUMBER_OF_SUIT, 1);

    Deck(int smallestCard) {
        for (int j = 0, k = 0; k < NUMBER_OF_SUIT; j += STEP, k++) {
            deck.add(1 + j);
            for (int i = smallestCard; i < STEP - 1; i++) {
                deck.add(i + j);
            }
        }
    }

    public Set<Integer> getNewDeck() {
         HashSet newDeck = new HashSet<>(STEP * NUMBER_OF_SUIT, 1);
         newDeck.addAll(deck);
         return newDeck;
    }

    public List<Integer> getUserDeck(Suit suit) {
        final int suitBack = suit.ordinal() * STEP;
        return deck.stream()
                .filter(x -> x > suitBack && x < suitBack + STEP)
                .map(x -> x += PLAYING_CARD_BACK)
                .collect(Collectors.toList());
    }
}
