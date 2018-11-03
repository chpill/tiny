(ns user
  (:require [figwheel.main]))


(def cljs-options {:main 'tiny.core
                   :closure-defines {'goog.DEBUG true}
                   :install-deps true
                   :npm-deps {:react "16.7.0-alpha.0"
                              :react-dom "16.7.0-alpha.0"}})

(defn go! []
  (figwheel.main/start {:watch-dirs ["src/"]
                        :open-url false
                        :log-file "figwheel-main.log"}
                       {:id "dev" :options cljs-options}))



(comment

  (go!)

  (do (require '[cljs.build.api])
      (cljs.build.api/build "src/" cljs-options))

  )


