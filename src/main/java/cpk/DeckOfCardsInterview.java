package cpk;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
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

    // Simulates a manual shuffle
    static class RiffleShuffler {
        private final Random random;
        private final int cardJitter;

        @SuppressWarnings("unused")
        public RiffleShuffler() {
            this.random = new Random(10L);
            this.cardJitter = 5;
        }

        public RiffleShuffler(long seed, int cardJitter) {
            this.random = new Random(seed);
            this.cardJitter = cardJitter;
        }

        public Deck shuffle(Deck origin) {
            // divides the deck "roughly" in two.  of course if you have a tiny deck
            // the shuffle will not work, but this kind of shuffle doesn't work on decks that
            // small anyway
            long jitteredHalf = Math.max(0, origin.size() / 2 + random.nextInt(11) - 5);

            // so this builds your deck halves.  see the method below for an explanation,
            // but basically this simulates the imprecision of the human thumb admitting the
            // cards through at an uneven rate
            new AbsoluteDelayer(random, 5);
            var firstHalf = makeHalf(origin.stream().limit(jitteredHalf)::iterator, new AbsoluteDelayer(random, cardJitter));
            var secondHalf = makeHalf(origin.stream().skip(jitteredHalf)::iterator, new AbsoluteDelayer(random, cardJitter));

            // so this will simulate the actual shuffle...the makeHalf() part keeps the original
            // cards in order, they're wired up with absolute delay generator to simulate the
            // travel of cards through time, and then merge will take whichever half has a card
            // available given the delay
            var result = merge(firstHalf, secondHalf)
                .toList()
                .blockingGet()
                .stream();

            return new Deck(result);
        }

        // Creates an observable composed of a delayed observable from each card.
        public Observable<Card> makeHalf(Iterable<Card> cardSource, AbsoluteDelayer absoluteDelayer) {
            return Observable
                .fromIterable(cardSource)
                .delay(absoluteDelayer::delay);
        }
    }

    // because the delay time in ReactiveX is ABSOLUTE, it means that if you want
    // cumulative delay based on the previous delays, you have to keep track of it
    //
    // You can use the trick illustrated in the Speak example, but I liked this better
    // because it was simpler.
    static class AbsoluteDelayer {
        private final Random random;
        private final int bound;

        private long next;

        public AbsoluteDelayer(Random random, int bound) {
            this.random = random;
            this.bound = bound;
            this.next = 0;
            // warm it up once because if you don't you'll never grab a card from the
            // second half as the first card
            this.next = next();
        }


        public long next() {
            long toReturn = next;
            next += random.nextInt(bound) + 1;
            return toReturn;
        }

        public <T> ObservableSource<T> delay(T item) {
            return just(item).delay(next(), TimeUnit.MILLISECONDS);
        }
    }

    public static void demo1() {
        new Deck().printAll();
    }

    public static void demo2(long seed, int jitter) {
        new RiffleShuffler(seed, jitter).shuffle(new Deck()).printAll();
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

        int jitter = Optional.of(args)
            .map(Args::getRaw)
            .filter(a -> a.length >= 3)
            .map(a -> a[2])
            .map(Integer::parseInt)
            .orElse(5);

        demo2(seed, jitter);
    }
}
