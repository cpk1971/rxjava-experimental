package cpk;

import io.reactivex.Observable;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static io.reactivex.Observable.just;

public class Speak2 extends Speak {

    @Override
    public void run(Args args) {
        var rnd = new Random();
        var quotes = just(
                alice.map("Alice: "::concat),
                bob.map("Bob "::concat),
                jane.map("Jane "::concat))
            .flatMap(innerObs -> just(innerObs)
            .delay(rnd.nextInt(5), TimeUnit.SECONDS));

        Observable
            .switchOnNext(quotes)
            .blockingForEach(System.out::println);
    }


}
