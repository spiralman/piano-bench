(ns piano-bench.core
  (:require [reagent.core :as reagent]))

(defn app []
  [:div "Hello world"])

(defn render []
  (reagent/render [app]
                  (js/document.getElementById "piano-bench")))
