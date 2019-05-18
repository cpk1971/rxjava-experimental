package cpk;

import io.reactivex.Observable;
import lombok.Data;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static io.reactivex.Observable.*;
import static java.util.stream.Collectors.*;

@SuppressWarnings({"Unused", "WeakerAccess"})
public class DeckOfCardsInterview implements Spike {
    enum Suit {
        Club,
        Diamond,
        Heart,
        Spade
    }

    enum Rank {
        Ace("A"),
        Deuce("2"),
        Trey("3"),
        Four("4"),
        Five("5"),
        Six("6"),
        Seven("7"),
        Eight("8"),
        Nine("9"),
        Ten("10"),
        Jack("J"),
        Queen("Q"),
        King("K");

        Rank(String displayName) {
            this.displayName = displayName;
        }
        private final String displayName;

        public String getDisplayName() {
            return displayName;
        }
    }

    @Data
    static class Card {
        private final Suit suit;
        private final Rank rank;

        @Override
        public String toString() {
            return suit + " " + rank.getDisplayName();
        }
    }

    static class Deck {
        private final Set<Card> cards;

        Deck() {
            this.cards = Stream.of(Suit.values())
                    .flatMap(suit -> Stream.of(Rank.values())
                            .map(rank -> new Card(suit, rank)))
                .collect(toCollection(LinkedHashSet::new));
        }

        Deck(Stream<Card> cards) {
            this.cards = cards.collect(toCollection(LinkedHashSet::new));
        }

        public void printAll() {
            cards.forEach(System.out::println);
        }

        public Stream<Card> stream() {
            return cards.stream();
        }

        public int size() {
            return cards.size();
        }

    }

    static class RiffleShuffler {
        private final Random random;

        @SuppressWarnings("unused")
        public RiffleShuffler() {
            this.random = new Random();
        }

        public RiffleShuffler(long seed) {
            this.random = new Random(seed);
        }

        public Deck shuffle(Deck origin) {
            long jitteredHalf = Math.max(0, origin.size() / 2 + random.nextInt(11) - 5);

            var firstHalf = makeHalf(origin.stream().limit(jitteredHalf)::iterator);
            var secondHalf = makeHalf(origin.stream().skip(jitteredHalf)::iterator);

            var result =
                concat(firstHalf, secondHalf)
                    .toList()
                    .blockingGet()
                    .stream();

            return new Deck(result);
        }

        public Observable<Card> makeHalf(Iterable<Card> cardSource) {
            return Observable
                .fromIterable(cardSource)
                .flatMap(o -> just(o).delay(random.nextInt(5), TimeUnit.MILLISECONDS));
        }

    }

    public static void demo1() {
        new Deck().printAll();
    }

    public static void demo2(long seed) {
        new RiffleShuffler(seed).shuffle(new Deck()).printAll();
    }

    @Override
    public void run(Args args) {
        demo1();

        System.out.println("---------");

        Long seed = Optional.of(args)
            .map(Args::getRaw)
            .filter(a -> a.length >= 2)
            .map(a -> a[1])
            .map(Long::parseLong)
            .orElse(10L);

        demo2(seed);
    }
}
