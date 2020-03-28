(ns piano-bench.notation
  (:require [reagent.core :as r]
            [vexflow]))

(def VF (.-Flow vexflow))

(defn build-note []
  (VF.StaveNote. #js {:clef "treble"
                      :keys #js ["C/4"]
                      :duration "q"}))

(defn render-phrase [context]
  (let [stave (-> (VF.Stave. 10 0 400)
                  (.addClef "treble")
                  (.setContext context))
        voice (VF.Voice. #js {:num_beats 1 :beat_value 4})]
    (.addTickables voice #js [(build-note)])
    (-> (VF.Formatter.)
        (.joinVoices #js [voice])
        (.format #js [voice] 300))
    (.draw stave)
    (.draw voice context stave)))

(defn build-context [el]
  (let [vf (VF.Renderer. el VF.Renderer.Backends.SVG)
        context (.getContext vf)]
    (.resize vf 300 300)
    context))

(defn stave []
  (let [context (r/atom nil)]
    (r/create-class
     {:display-name "notation"
      :component-did-mount
      (fn [this]
        (let [context (build-context (r/dom-node this))]
          (render-phrase context)))
      :reagent-render
      (fn []
        [:div {}])})))
