(ns piano-bench.core
  (:require [reagent.core :as reagent]
            [leipzig.scale :as scale]))

(def black-width 33.15)
(def white-width 69.19)

(def wholes (comp (scale/from 24) scale/major))
(def notes ["C" "D" "E" "F" "G" "A" "B"])

(defn has-black [key]
  (if (and
       (= 2
          (- (wholes (inc key))
             (wholes key)))
       ;; Pianos don't have a G#0/Ab0
       (not= key -3))
    key))

(defn nth-octave [n]
  (* 7 (dec n)))

(defn octave [white-key]
  (if (>= white-key 0)
    (inc (int (/ white-key 7)))
    0))

(defn note [white-key]
  (nth notes (mod white-key 7)))

(defn white-key-name [white-key]
  (str
   (note white-key)
   (octave white-key)))

(defn black-key-name [white-key]
  (let [base-name (note white-key)]
    (str
     (case base-name
       ("A" "D") (str (note (inc white-key)) "b")
       (str base-name "#"))
     (octave white-key))))

(defn black-key [start-key white-key]
  [:g {:transform (str "translate(" (- (* (- (inc white-key) start-key) white-width)
                                       (/ black-width 2)) ")")
       :stroke "#000000" :stroke-width "1" :fill "#000000"
       :on-click #(println (black-key-name white-key))}
   [:path {:d "M0,0 L8.35795057e-07,203.122781 C8.44881404e-07,205.33103 1.78205875,207.12117 3.99814921,207.12117 L29.1475731,207.12117 C31.3556894,207.12117 33.1457214,205.330596 33.1457214,203.122781 L33.1457206,0 L0,0 Z"}]])

(defn white-key [start-key key]
  [:g {:transform (str "translate(" (* (- key start-key) white-width) ")")
       :stroke "#000000" :stroke-width "1" :fill "#FFFFFF"
       :on-click #(println (white-key-name key))}
   [:path {:d "M0,324.998255 C0,327.760984 2.24331036,330.000618 5.00408559,330.000618 L64.1897584,330.000618 C66.9534386,330.000618 69.193844,327.761793 69.193844,324.998255 L69.193844,0 L0,0 L0,324.998255 Z"}]])

(defn keyboard [start-key]
  (let [end-key (+ start-key 8)
        keys (range start-key end-key)
        black-keys (keep has-black (range (dec start-key) end-key))]
    [:svg {:width "100%" :height "100%" :view-box "0 0 900 400" :fill "none"}
     [:g {:transform "translate(0,5)"}
      (map (partial white-key start-key) keys)
      (map (partial black-key start-key) black-keys)]]))

(defn app []
  [:div
   [:h1 "Piano Bench"]
   [keyboard (- (nth-octave 4) 2)]])

(defn render []
  (reagent/render [app]
                  (js/document.getElementById "piano-bench")))
