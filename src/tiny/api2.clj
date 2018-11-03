(ns tiny.api2
  (:require [hicada.compiler])
  )


(defn custom-args-for-cljs-components
  [[tag props children :as args]]
  [tag props children])


(defmacro e
  [body]
  (hicada.compiler/compile body {:create-element `react-create-element
                                 :transform-fn (comp
                                                custom-args-for-cljs-components)
                                 ;; :default-ns 'react-dom
                                 ;; :no-string-tags? true
                                 ;; raw-js-props
                                 :rewrite-for? true
                                 :array-children? false}
                           {} &env))
