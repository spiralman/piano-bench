(ns piano-bench.core
  (:require [reagent.core :as reagent]))

(def black-width 33.15)
(def white-width 69.19)

(defn black-key []
  [:g {:stroke "#000000" :stroke-width "1" :fill "#000000"}
   [:path {:d "M0,0 L8.35795057e-07,203.122781 C8.44881404e-07,205.33103 1.78205875,207.12117 3.99814921,207.12117 L29.1475731,207.12117 C31.3556894,207.12117 33.1457214,205.330596 33.1457214,203.122781 L33.1457206,0 L0,0 Z"}]])

(defn white-key []
  [:g {:stroke "#000000" :stroke-width "1" :fill "#FFFFFF"}
   [:path {:d "M0,324.998255 C0,327.760984 2.24331036,330.000618 5.00408559,330.000618 L64.1897584,330.000618 C66.9534386,330.000618 69.193844,327.761793 69.193844,324.998255 L69.193844,0 L0,0 L0,324.998255 Z"}]])

(defn keyboard []
  [:svg {:width "100%" :height "100%" :view-box "0 0 900 400" :fill "none"}
   [:g {:transform "translate(0,5)"}
    [:g {:transform "translate(0)"}
     [white-key]]
    [:g {:transform (str "translate(" (- white-width (/ black-width 2)) ")")}
     [black-key]]]])

(defn app []
  [:div "Hello world"
   [keyboard]])

(defn render []
  (reagent/render [app]
                  (js/document.getElementById "piano-bench")))
