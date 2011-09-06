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

Next, schedule the function of your dreams. Here we schedule the function to execute in 1000 ms from now (i.e. 1 second):

    (at (+ 1000 (now)) #(println "hello from the past!"))

You can also schedule functions to occur periodically. Here we schedule the function to execute every second:

    (every 1000 #(println "I am cool!"))

This can easily be stopped with `cancel` or its synonym `stop`:

    (stop *1)

Finally, it's also possible to start a periodic repeating fn with an inital delay:

    (every 1000 #(println "I am cool!") 2000)


### Pools

`at-at` uses `ScheduledThreadPoolExecutor`s behind the scenes which use a thread pool to run the scheduled tasks. There is a default pool which is created for you with `(+ cpu-count 2)` threads. However, you may create your own and run them separately.

For example, here we create a pool with 5 threads:

    (def my-pool (mk-pool 5))

We can then use this pool to run our own periodic

    (every 1000 #(println "I am super cool!") my-pool)

And then throw in our old friend running on the default pool:

    (every 1000 #(println "I used to be cool until you made your own pool!"))

When necessary it's possible to stop and reset a given pool:

    (stop-and-reset-pool! my-pool)

We can also stop and reset the default pool:

    (stop-and-reset-pool!)

### Install

Fetch at-at from github: https://github.com/overtone/at-at or pull from clojars: `[overtone/at-at "0.0.1"]`

### History

at-at was extracted from the awesome music making wonder that is Overtone (http://github.com/overtone/overtone)


### Authors

* Sam Aaron
* Jeff Rose


(Ascii art borrowed from http://www.sanitarium.net/jokes/getjoke.cgi?132)
