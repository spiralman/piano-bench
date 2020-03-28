(ns piano-bench.notation
  (:require [reagent.core :as r]
            [vexflow]))

(.log js/console (.-Flow vexflow))

(defn stave []
  [:div {} "Stave goes here"])
