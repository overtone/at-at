(ns overtone.at-at
  (:import [java.util.concurrent ScheduledThreadPoolExecutor TimeUnit]))

(defn- cpu-count
  "Get the number of CPUs on this machine."
  []
  (.availableProcessors (Runtime/getRuntime)))

(defn- mk-pool*
  [num-threads]
  (ScheduledThreadPoolExecutor. num-threads))

(defn mk-pool
  "Returns an atom storing a newly created pool of threads to schedule new
  events for. Pool size defaults to the cpu count + 2"
  ([] (mk-pool (+ 2 (cpu-count))))
  ([num-threads]
     (atom (mk-pool* num-threads))))

(defonce default-pool* (mk-pool))

(defn every
  "Calls fun every ms-period, and takes an optional initial-delay for the first
  call in ms. Default pool is used if none explicity specified. Returns a
  scheduled-fn which may be cancelled with stop-scheduled-fn"
  ([ms-period fun] (every ms-period fun 0))
  ([ms-period fun initial-delay-or-pool] (if (number? initial-delay-or-pool)
                                           (every ms-period fun initial-delay-or-pool default-pool*)
                                           (every ms-period fun 0 initial-delay-or-pool)))
  ([ms-period fun initial-delay pool]
     (let [initial-delay (long initial-delay)
           ms-period     (long ms-period)]
       (.scheduleAtFixedRate @pool fun initial-delay ms-period TimeUnit/MILLISECONDS))))

(defn- stop-and-reset-pool
  [pool shutdown-immediately?]
  (let [num-threads (.getCorePoolSize pool)
        new-pool (mk-pool* num-threads)]
    (if shutdown-immediately?
      (.shutdownNow pool)
      (.shutdown pool))

    new-pool))

(defn stop-and-reset-pool!
  "Shuts down a given pool (passed in as an atom) either immediately or not
  depending on whether the optional shutdown-immediately? param is used. The
  pool is then reset to a fresh new pool preserving the original size. If called
  with no params, the default pool is used.

  Example usage:
  (stop-and-reset-pool!) ;=> default pool is reset gracefullly
  (stop-and-reset-pool! true) ;=> default pool is reset immediately
  (stop-and-reset-pool! pool) ;=> pool is reset gracefully
  (stop-and-reset-pool! pool true) ;=> pool is reset immediately"
  ([] (stop-and-reset-pool! default-pool* false))
  ([pool-or-bool] (if (or (= true pool-or-bool)
                          (= false pool-or-bool))
                    (stop-and-reset-pool! default-pool* pool-or-bool)
                    (stop-and-reset-pool! pool-or-bool false)))
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
  immediately if ms-time is in the past. Default pool is used if none
  explicitly. Use (now) to get the current time in ms.

  Example usage:
  (at (+ 1000 (now)) #(println \"hello from the past\")) ;=> prints 1s from now"
  ([ms-time fun] (at ms-time fun default-pool*))
  ([ms-time fun pool]
     (let [delay-time (- ms-time (now))]
       (if (<= delay-time 0)
         (fun)
         (.schedule @pool fun (long delay-time) TimeUnit/MILLISECONDS)))))
