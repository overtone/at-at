(ns at-at.walkthrough
  (:require
   [overtone.at-at :as at]))

(def my-pool (at-at/mk-pool))

(at/at (+ 1000 (at/now)) #(println "hello from the past!") my-pool)
(at/after 1000 #(println "hello from the past!") my-pool)

(at/every 1000 #(println "I am cool!") my-pool)
(at/every 1000 #(println "I am cool!") my-pool :initial-delay 2000)
(at/show-schedule my-pool)
(at/stop (first (at/scheduled-jobs my-pool)))

(at/interspaced 1000 #(println "I am cool!") my-pool)
(at/stop-and-reset-pool! my-pool)
(at/stop-and-reset-pool! my-pool :strategy :kill)

(def tp (at/mk-pool))
(at/after 10000 #(println "hello") tp :desc "Hello printer")
(at/every 5000 #(println "I am still alive!") tp :desc "Alive task")
(at/show-schedule tp)

(run! at/stop (at/scheduled-jobs tp))
