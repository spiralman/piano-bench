(ns piano-bench.test.core
  (:require [cljs.test :refer-macros [deftest is testing run-tests]]
            [piano-bench.notes :as notes]))

(deftest nth-octave-4-starts-at-C4
  (is (= (notes/nth-octave 4)
         21)))
