(ns user
  (:require [figwheel.main]))


(def cljs-options {:main "tiny.core"
                   :closure-defines {'goog.DEBUG true}})

(defn go! []
  (figwheel.main/start {:watch-dirs ["src/"]
                        :open-url false
                        :log-file "figwheel-main.log"}
                       {:id "dev" :options cljs-options}))
