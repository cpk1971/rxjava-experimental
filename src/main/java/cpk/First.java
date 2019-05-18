package cpk;

import io.reactivex.Observable;

import java.math.BigInteger;

public class First implements Spike {
    private void log(Object msg) {
        System.out.printf("%s: %s\n", Thread.currentThread().getName(), msg);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void run(Args args) {
        log("Before");

        Observable
            .range(5, 3)
            .subscribe(this::log);

        log("After");

        var factorials = Observable.range(2, 100)
            .scan(BigInteger.ONE, (big, cur) -> big.multiply(BigInteger.valueOf(cur)));

        factorials.subscribe(this::log);
    }
}
