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

<!-- badges -->
[![cljdoc badge](https://cljdoc.org/badge/overtone/at-at)](https://cljdoc.org/d/overtone/at-at) [![Clojars Project](https://img.shields.io/clojars/v/overtone/at-at.svg)](https://clojars.org/overtone/at-at)
<!-- /badges -->

Simple ahead-of-time function scheduler. Allows you to schedule the execution of an anonymous function for a point in the future.

<!-- installation -->
## Installation

To use the latest release, add the following to your `deps.edn` ([Clojure CLI](https://clojure.org/guides/deps_and_cli))

```
overtone/at-at {:mvn/version "1.4.65"}
```

or add the following to your `project.clj` ([Leiningen](https://leiningen.org/))

```
[overtone/at-at "1.4.65"]
```
<!-- /installation -->

### Basic Usage

First pull in the lib:

```clj
(require '[overtone.at-at :as at])
```

`at-at` uses `ScheduledThreadPoolExecutor`s behind the scenes which use a thread pool to run the scheduled tasks. You therefore need create a pool before you can get going:

```clj
(def my-pool (at/mk-pool))
```

It is possible to pass in extra options `:cpu-count`, `:stop-delayed?` and `:stop-periodic?` to further configure your pool. See `mk-pool`'s docstring for further info.

Next, schedule the function of your dreams. Here we schedule the function to execute in 1000 ms from now (i.e. 1 second):

```clj
(at/at (+ 1000 (at/now)) #(println "hello from the past!") my-pool)
```

You may also specify a description for the scheduled task with the optional `:desc` key.

Another way of achieving the same result is to use `after` which takes a delaty time in ms from now:

```clj
(at/after 1000 #(println "hello from the past!") my-pool)
```

You can also schedule functions to occur periodically. Here we schedule the function to execute every second:

```clj
(at/every 1000 #(println "I am cool!") my-pool)
```

This returns a scheduled-fn which may easily be stopped `stop`:

```clj
(at/stop *1)
```

Or more forcefully killed with `kill`.

It's also possible to start a periodic repeating fn with an initial delay:

```clj
(at/every 1000 #(println "I am cool!") my-pool :initial-delay 2000)
```

Finally, you can also schedule tasks for a fixed delay (vs a rate):

```clj
(at/interspaced 1000 #(println "I am cool!") my-pool)
```

This means that it will wait 1000 ms after the task is completed before 
starting the next one.

### Resetting a pool.

When necessary it's possible to stop and reset a given pool:

```clj
(at/stop-and-reset-pool! my-pool)
```

You may forcefully reset the pool using the `:kill` strategy:

```clj
(at/stop-and-reset-pool! my-pool :strategy :kill)
```

### Viewing running scheduled tasks.

`at-at` keeps an eye on all the tasks you've scheduled. You can get a set of the current jobs (both scheduled and recurring) using `scheduled-jobs` and you can pretty-print a list of these job using `show-schedule`. The ids shown in the output of `show-schedule` are also accepted in `kill` and `stop`, provided you also specify the associated pool. See the `kill` and `stop` docstrings for more information.

```clj
(def tp (at/mk-pool))
(at/after 10000 #(println "hello") tp :desc "Hello printer")
(at/every 5000 #(println "I am still alive!") tp :desc "Alive task")
(at/show-schedule tp)
;; [6][RECUR] created: Thu 12:03:35s, period: 5000ms,  desc: "Alive task"
;; [5][SCHED] created: Thu 12:03:32s, starts at: Thu 12:03:42s, desc: "Hello printer"
```

### History

at-at was extracted from the awesome music making wonder that is Overtone (http://github.com/overtone/overtone)

### Release Tooling

This project uses the Lambda Island release tooling. See `bin/proj --help` for options. To release a new version to Clojars:

```
bin/proj release
```

### Authors

* Sam Aaron
* Jeff Rose
* Michael Neale
* Alexander Oloo
* Arne Brasseur
* Daniel MacDougall 
* Josh Comer

(Ascii art borrowed from http://www.sanitarium.net/jokes/getjoke.cgi?132)

<!-- license -->
## License

Copyright &copy; 2011-2023 Sam Aaron, Jeff Rose, and contributors

Available under the terms of the Eclipse Public License 1.0, see LICENSE.txt
<!-- /license -->
