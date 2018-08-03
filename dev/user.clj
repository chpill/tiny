(ns user
  (:require [figwheel.main]))


(def cljs-options {:main "tiny.core"})

(defn go! []
  (figwheel.main/start {:watch-dirs ["src/"]}
                       {:id "dev" :options cljs-options}))
