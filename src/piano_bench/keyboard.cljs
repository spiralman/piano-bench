(ns piano-bench.keyboard
  (:require [leipzig.scale :as scale]))

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

(defn black-key [start-key on-press white-key]
  (let [name (black-key-name white-key)
        x-off (- (* (- (inc white-key) start-key) white-width)
                 (/ black-width 2))
        center (+ x-off (/ black-width 2))]
    [:g {:key name
         :transform (str "translate(" x-off ")")
         :stroke "#000000" :stroke-width "1" :fill "#000000"
         :on-click #(on-press center name)}
     [:path {:d "M0,0 L8.35795057e-07,203.122781 C8.44881404e-07,205.33103 1.78205875,207.12117 3.99814921,207.12117 L29.1475731,207.12117 C31.3556894,207.12117 33.1457214,205.330596 33.1457214,203.122781 L33.1457206,0 L0,0 Z"}]]))

(defn white-key [start-key on-press key]
  (let [name (white-key-name key)
        x-off (* (- key start-key) white-width)
        center (+ x-off (/ white-width 2))]
    [:g {:key name
         :transform (str "translate(" x-off ")")
         :stroke "#000000" :stroke-width "1" :fill "#FFFFFF"
         :on-click #(on-press center name)}
     [:path {:d "M0,324.998255 C0,327.760984 2.24331036,330.000618 5.00408559,330.000618 L64.1897584,330.000618 C66.9534386,330.000618 69.193844,327.761793 69.193844,324.998255 L69.193844,0 L0,0 L0,324.998255 Z"}]]))

(defn popup [center key-name]
  (let [x-off (- center (/ white-width 2))]
    [:g {:transform (str "translate(" x-off ",-81)")
         :fill "#00FF00"}
     [:path {:d "M8.00456499,0 L61.1854375,0 C65.6062366,0 69.1900024,3.57425806 69.1900024,8.00456499 L69.1900024,61.1854375 C69.1900024,65.6062366 65.6157444,69.1900024 61.1854375,69.1900024 L45.4916992,69.1900024 L34.8994141,80.8984375 L24.3071289,69.1900024 L8.00456499,69.1900024 C3.58376582,69.1900024 0,65.6157444 0,61.1854375 L0,8.00456499 C0,3.58376582 3.57425806,0 8.00456499,0 Z"}]]))

(defn keyboard [start-key pressed on-press]
  (println "hello? 2" on-press)
  (let [scale (/ 900 (* 8 white-width))
        end-key (+ start-key 8)
        keys (range start-key end-key)
        black-keys (keep has-black (range (dec start-key) end-key))]
    [:svg {:width "100%" :height "100%" :view-box "0 0 900 800" :fill "none"}
     [:g {:transform (str "translate(0,145) scale(" scale ")")}
      (map (partial white-key start-key on-press) keys)
      (map (partial black-key start-key on-press) black-keys)
      (if-let [{:keys [x-off key-name]} pressed]
        [popup x-off key-name])]]))
