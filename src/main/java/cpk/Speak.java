package cpk;

import io.reactivex.Observable;

import java.util.AbstractMap;
import java.util.concurrent.TimeUnit;

import static io.reactivex.Observable.just;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Speak implements Spike {
    protected final Observable<String> alice =
            speak("To be, or not to be: that is the question", 110);;

    protected final Observable<String> bob =
            speak("Though this be madness, yet there are methods in't", 90);

    protected final Observable<String> jane =
            speak("There are more things in Heaven and Earth, Horatio, than are dreamt of in your philosophy", 100);


    private Observable<String> speak(String quote, long millisPerChar) {
        var tokens = quote.replaceAll("[:,]", "").split(" ");

        var words = Observable.fromArray(tokens);

        var absoluteDelay = words
            .map(String::length)
            .map(len -> len * millisPerChar)
            .scan((total, current) -> total + current);

        return words
            .zipWith(absoluteDelay.startWith(0L), AbstractMap.SimpleEntry::new)
            .flatMap(pair -> just(pair.getKey()).delay(pair.getValue(), TimeUnit.MILLISECONDS));
    }


    @Override
    public void run(Args args) throws Exception {
        Observable.concat(
            alice.map("Alice: "::concat),
            bob.map("Bob: "::concat),
            jane.map("Jane: "::concat)
        ).subscribe(System.out::println);

        SECONDS.sleep(30);
    }
}
