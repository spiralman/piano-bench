(ns ^:figwheel-hooks
    piano-bench.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]
            [breaking-point.core :as bp]
            [piano-bench.notation :as notation]
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

(def topbar-height 30)
(def topbar-margin 20)
(def topbar-total (+ topbar-height topbar-margin))

(rf/reg-sub
 :box-size
 (fn [_ _]
   [(rf/subscribe [::bp/screen-width])
    (rf/subscribe [::bp/screen-height])])
   (fn [[screen-width screen-height] _]
     (let [layout-height (- screen-height topbar-total)
           max-height (/ layout-height 2)]
       (min screen-width max-height))))

(defn split-layout [top bottom]
  (let [size @(rf/subscribe [:box-size])]
    [:div
     [:div {:style {:margin "auto" :width size :height size}}
      (replace {:pb/size size} top)]
     [:div {:style {:margin "auto" :width size :height size}}
      (replace {:pb/size size} bottom)]]))

(defn app []
  [:div
   [:h1 {:style {:height topbar-height
                 :margin-top topbar-margin
                 :margin-bottom topbar-margin}}
    "Piano Bench"]
   [split-layout
    [notation/stave :pb/size]
    [kb/keyboard
     :pb/size
     @(rf/subscribe [:start-key])
     @(rf/subscribe [:pressed])
     #(rf/dispatch [:key-pressed %1 %2])]]])

(defn ^:after-load render []
  (reagent/render [app]
                  (js/document.getElementById "piano-bench")))

(defonce start-up
  (do
    (rf/dispatch-sync [:initialize])
    (rf/dispatch-sync [::bp/set-breakpoints
                       {:breakpoints [:all 0]
                        :debounce-ms 150}])
    (render)
    true))
