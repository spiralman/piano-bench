(ns piano-bench.notation
  (:require [reagent.core :as r]
            [vexflow]))

(def VF (.-Flow vexflow))

(defn build-note []
  (VF.StaveNote. #js {:clef "treble"
                      :keys #js ["C/4"]
                      :duration "q"}))

(defn render-phrase [context size]
  (let [stave (-> (VF.Stave. 0 0 198)
                  (.addClef "treble")
                  (.setContext context))
        voice (VF.Voice. #js {:num_beats 1 :beat_value 4})]
    (.addTickables voice #js [(build-note)])
    (-> (VF.Formatter.)
        (.joinVoices #js [voice])
        (.format #js [voice] 200))
    (.draw stave)
    (.draw voice context stave)))

(defn build-context [el]
  (let [renderer (VF.Renderer. el VF.Renderer.Backends.SVG)
        context (.getContext renderer)]
    [renderer context]))

(defn stave-inner []
  (let [vf (r/atom nil)
        update (fn [comp]
                 (let [{:keys [renderer context]} @vf
                       {:keys [size]} (r/props comp)]
                   (.resize renderer size size)
                   (.clear context)
                   (.setViewBox context 0 0 200 200)
                   (render-phrase context size)))]
    (r/create-class
     {:display-name "notation"
      :component-did-mount
      (fn [this]
        (let [[renderer context] (build-context (r/dom-node this))]
          (reset! vf {:renderer renderer :context context})
          (update this)))
      :component-did-update
      (fn [this]
        (update this))
      :reagent-render
      (fn []
        [:div {}])})))

(defn stave [size]
  (if (> size 0)
    [stave-inner {:size size}]
    [:div]))
