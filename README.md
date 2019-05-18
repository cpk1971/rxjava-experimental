# Hi

These are silly experiments in [RxJava](https://github.com/ReactiveX/RxJava).

## What it is about

More about [ReactiveX](http://reactivex.io)

[The Reactive Manifesto,](https://www.reactivemanifesto.org) propaganda about the modern evolution of the [Actor Model](https://en.wikipedia.org/wiki/Actor_model).

## Credit for some of the experiments

First, Speak, and Speak2 come from a cool book called [Reactive Programming With RxJava](https://www.amazon.com/gp/product/B01LZQGIIC/ref=ppx_yo_dt_b_d_asin_title_o00?ie=UTF8&psc=1)
which is a bit outdated since it was about RxJava 1.x, but I've adapted some of it to RxJava 2.x.

DeckOfCardsInterview comes from a discussion with a coworker.

## How to Run

You need JDK 11+.  If you have it, and you're on a Mac or any other type of Unix, just go to the root and do

`./gradlew run --args '<experiment name> <experiment args...>`

For experiment name you can use:

```
1
speak
speak2
deck
```

If you don't give it an experiment name you'll get `deck`.

### First

Just an warmup.  It shows that ReactiveX is by default synchronous.
If you run it you'll see some derpage followed by all the factorials between 1 and 100

[Source](src/main/java/cpk/First.java)

### Speak & speak2

Examples from the *Reactive Programming With RxJava* book.  If I pissed anyone off kiping their
sample code let me know and I'll delete it.

These examples are asynchronous becuse they use the [Delay](http://reactivex.io/documentation/operators/delay.html)
utility, which in RxJava means you're now asynchronous.  

I added some RxJava 2 nonsense to the book examples, most notable for applications like this
you want to use blockingForEach() or some such on the final Observable rather than subscribe().

[Speak](src/main/java/cpk/Speak.java)
[Speak2](src/main/java/cpk/Speak2.java)
 

### Deck

This one was stimulated by an interview question asked by a colleague.  I suggested
you could use ReactiveX to solve the human shuffle problem, and it would be amazingly
simple.  Well, here it is.

It would be interesting to apply a statistical function to the randomness.

Handling the delay wasn't quite as simple as I had hoped but maybe I'll take another
crack at it later.

---

You can give it two parameters--1 is a seed value (for repeatability) and 2 is a 
bound for how much the jitter can vary.  
 
It prints out the deck in order (that was part of the assignment) and then 
does the human shuffle simulation.

---

For instance I ran it like this:

`./gradlew run --args 'deck 777 50'`

And I got the following:

```
Club A
Club 2
Club 3
Club 4
Club 5
Club 6
Club 7
Club 8
Club 9
Club 10
Club J
Club Q
Club K
Diamond A
Diamond 2
Diamond 3
Diamond 4
Diamond 5
Diamond 6
Diamond 7
Diamond 8
Diamond 9
Diamond 10
Diamond J
Diamond Q
Diamond K
Heart A
Heart 2
Heart 3
Heart 4
Heart 5
Heart 6
Heart 7
Heart 8
Heart 9
Heart 10
Heart J
Heart Q
Heart K
Spade A
Spade 2
Spade 3
Spade 4
Spade 5
Spade 6
Spade 7
Spade 8
Spade 9
Spade 10
Spade J
Spade Q
Spade K
---------
Club A
Heart 2
Club 2
Heart 3
Club 3
Heart 4
Heart 5
Club 4
Club 5
Heart 6
Club 6
Heart 7
Club 7
Heart 8
Club 8
Heart 9
Club 9
Club 10
Heart 10
Club J
Club Q
Heart J
Club K
Heart Q
Diamond A
Diamond 2
Heart K
Spade A
Spade 2
Spade 3
Diamond 3
Spade 4
Spade 5
Diamond 4
Diamond 5
Spade 6
Diamond 6
Diamond 7
Diamond 8
Spade 7
Diamond 9
Spade 9
Spade 8
Spade 10
Diamond 10
Spade J
Diamond J
Spade Q
Diamond Q
Diamond K
Heart A
Spade K
```