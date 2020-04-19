(ns piano-bench.test.runner
  (:require [figwheel.main.testing :refer-macros [run-tests-async]]
            [piano-bench.test.notes]))

(defn -main [& args]
  (run-tests-async 10000))
