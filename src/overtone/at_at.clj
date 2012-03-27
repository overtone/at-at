(ns overtone.at-at
  (:import [java.util.concurrent ScheduledThreadPoolExecutor TimeUnit]))

(defn- cpu-count
  "Get the number of CPUs on this machine."
  []
  (.availableProcessors (Runtime/getRuntime)))

(defn- sched-thread-pool
  "Create a new scheduled thread pool containing num-threads threads."
  [num-threads]
  (ScheduledThreadPoolExecutor. num-threads))

(defn mk-pool
  "Returns an atom storing a newly created pool of threads to schedule new
  events for. Pool size defaults to the cpu count + 2"
  ([] (mk-pool (+ 2 (cpu-count))))
  ([num-threads]
     (atom (sched-thread-pool num-threads))))

(defn every
  "Calls fun every ms-period, and takes an optional initial-delay for
  the first call in ms.  Returns a scheduled-fn which may be cancelled
  with cancel"
  ([pool ms-period fun] (every pool ms-period fun 0))
  ([pool ms-period fun initial-delay]
     (let [initial-delay (long initial-delay)
           ms-period     (long ms-period)]
       (.scheduleAtFixedRate @pool fun initial-delay ms-period TimeUnit/MILLISECONDS))))

(defn- stop-and-reset-pool
  [pool shutdown-immediately?]
  (let [num-threads (.getCorePoolSize pool)
        new-pool (sched-thread-pool num-threads)]
    (if shutdown-immediately?
      (.shutdownNow pool)
      (.shutdown pool))

    new-pool))

(defn stop-and-reset-pool!
  "Shuts down a given pool (passed in as an atom) either immediately
  or not depending on whether the optional shutdown-immediately? param
  is used. The pool is then reset to a fresh new pool preserving the
  original size. If called with no params, the default pool is used.

  Example usage:
  (stop-and-reset-pool! pool)      ;=> pool is reset gracefullly
  (stop-and-reset-pool! pool true) ;=> pool is reset immediately"
  ([pool] (stop-and-reset-pool! pool false))
  ([pool shutdown-immediately?]
     (swap! pool stop-and-reset-pool shutdown-immediately?)))

(defn cancel
  "Cancel/stop scheduled fn if it hasn't already executed"
  ([sched-fn] (cancel sched-fn false))
  ([sched-fn cancel-immediately?]
     (.cancel sched-fn cancel-immediately?)))

(def stop cancel)

(defn now
  "Return the current time in ms"
  []
  (System/currentTimeMillis))

(defn at
  "Schedules fun to be executed at ms-time (in milliseconds). Executes
  immediately if ms-time is in the past.  Use (now) to get the current
  time in ms.

  Example usage:
  (at (+ 1000 (now)) #(println \"hello from the past\")) ;=> prints 1s
                                                         ;   from now"
  ([pool ms-time fun]
     (let [delay-time (- ms-time (now))]
       (if (<= delay-time 0)
         (.execute @pool fun)
         (.schedule @pool fun (long delay-time) TimeUnit/MILLISECONDS)))))

(defn after
  "Schedules fun to be executed after delay-ms (in milliseconds) from
  now. Executes immediately if ms-time is 0 or negative.

  Example usage:
  (after 1000 #(println \"hello from the past\")) ;=> prints 1s
                                                  ;   from now"
  ([pool delay-ms fun]
     (if (<= delay-ms 0)
       (.execute @pool fun)
       (.schedule @pool fun (long delay-ms) TimeUnit/MILLISECONDS))))
