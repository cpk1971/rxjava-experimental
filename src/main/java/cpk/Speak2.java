package cpk;

import io.reactivex.Observable;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static io.reactivex.Observable.just;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Speak2 extends Speak {

    @Override
    public void run(Args args) throws Exception {
        var rnd = new Random();
        var quotes = just(
                alice.map("Alice: "::concat),
                bob.map("Bob "::concat),
                jane.map("Jane "::concat))
            .flatMap(innerObs -> just(innerObs)
            .delay(rnd.nextInt(5), TimeUnit.SECONDS));

        Observable
            .switchOnNext(quotes)
            .subscribe(System.out::println)
            .dispose();

        SECONDS.sleep(30);

    }


}
