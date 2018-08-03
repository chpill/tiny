(ns tiny.core
  (:require react-dom
            goog.object))


(def e js/React.createElement)

(defn app []
  (e "div" #js {:className "Plop"
                :style #js {:backgroundColor "papayawhip"
                            :margin 10}}
     (e "p" nil "texte 1")
     (e "p" nil "texte 2")))




(defn app-div! []
  (or (js/document.getElementById "tiny-demo-app")
      (doto (js/document.createElement "div")
        (goog.object/set "id" "tiny-demo-app")
        js/document.body.append)))




(js/ReactDOM.render (app)
                    (app-div!))

