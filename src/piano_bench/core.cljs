(ns piano-bench.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]
            [piano-bench.keyboard :as kb]))

(defn nth-octave [n]
  (* 7 (dec n)))

(defonce app-db (reagent/atom {}))

(rf/reg-event-db
 :initialize
 (fn [db _]
   {:start-key (- (nth-octave 4) 2)}))

(rf/reg-event-db
 :key-pressed
 (fn [db [_ x-off key-name]]
   (assoc db :pressed {:x-off x-off
                       :key-name key-name})))

(rf/reg-sub
 :start-key
 (fn [db _]
   (:start-key db)))

(rf/reg-sub
 :pressed
 (fn [db _]
   (:pressed db)))

(defn app []
  [:div
   [:h1 "Piano Bench"]
   [kb/keyboard
    @(rf/subscribe [:start-key])
    @(rf/subscribe [:pressed])
    #(rf/dispatch [:key-pressed %1 %2])]])

(defn render []
  (reagent/render [app]
                  (js/document.getElementById "piano-bench")))

(defn re-load []
  (let [old-app-el (js/document.getElementById "piano-bench")
        parent (.-parentNode old-app-el)
        new-app-el (js/document.createElement "DIV")]
    (.remove old-app-el)
    (.setAttribute new-app-el "id" "piano-bench")
    (.appendChild parent new-app-el)
    (rf/dispatch-sync [:initialize])
    (render)))
