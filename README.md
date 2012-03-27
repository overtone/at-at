                                            ________
                                        _,.-Y  |  |  Y-._
                                    .-~"   ||  |  |  |   "-.
                                    I" ""=="|" !""! "|"[]""|     _____
                                    L__  [] |..------|:   _[----I" .-{"-.
                                   I___|  ..| l______|l_ [__L]_[I_/r(=}=-P
                                  [L______L_[________]______j~  '-=c_]/=-^
                                   \_I_j.--.\==I|I==_/.--L_]
                                     [_((==)[`-----"](==)j
                                        I--I"~~"""~~"I--I
                                        |[]|         |[]|
                                        l__j         l__j
                                        |!!|         |!!|
                                        |..|         |..|
                                        ([])         ([])
                                        ]--[         ]--[
                                        [_L]         [_L]  -Row
                                       /|..|\       /|..|\
                                      `=}--{='     `=}--{='
                                     .-^--r-^-.   .-^--r-^-.
                              ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                                          __               __
                                   ____ _/ /_       ____ _/ /_
                                  / __ `/ __/______/ __ `/ __/
                                 / /_/ / /_ /_____/ /_/ / /_
                                 \__,_/\__/       \__,_/\__/



### at-at

Simple ahead-of-time function scheduler. Allows you to schedule the execution of an anonymous function for a point in the future.

### Basic Usage

First pull in the lib:

    (use 'overtone.at-at)

`at-at` uses `ScheduledThreadPoolExecutor`s behind the scenes which use a thread pool to run the scheduled tasks. You create a pool, specifying a number of threads with `mk-pool`. For example, here we create a pool with 5 threads:

    (def my-pool (mk-pool 5))

Next, schedule the function of your dreams. Here we schedule the function to execute in 1000 ms from now (i.e. 1 second):

    (at (+ 1000 (now)) my-pool #(println "hello from the past!"))

Another way of achieving the same result is to use `after` which takes a delaty time in ms from now:

    (after 1000 my-pool #(println "hello from the past!"))

You can also schedule functions to occur periodically. Here we schedule the function to execute every second:

    (every 1000 my-pool #(println "I am cool!"))

This returns a shceduled-fn which may easily be stopped with `cancel` or its synonym `stop`:

    (stop *1)

Finally, it's also possible to start a periodic repeating fn with an inital delay:

    (every 1000 my-pool #(println "I am cool!") 2000)

### Resetting a pool.

When necessary it's possible to stop and reset a given pool:

    (stop-and-reset-pool! my-pool)

### Install

Fetch at-at from github: https://github.com/overtone/at-at or pull from clojars: `[overtone/at-at "X.Y.Z"]`

### History

at-at was extracted from the awesome music making wonder that is Overtone (http://github.com/overtone/overtone)


### Authors

* Sam Aaron
* Jeff Rose


(Ascii art borrowed from http://www.sanitarium.net/jokes/getjoke.cgi?132)
