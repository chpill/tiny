(ns tiny.api2
  (:require [hicada.compiler])
  (:import [cljs.tagged_literals JSValue]))

;; From Roman01la https://gist.github.com/roman01la/98d23f56468e266d86314aadabb56f12

;; Sablono's stuff
;; Converting Clojure data into ClojureScript (JS)
;; ====================================================
(defprotocol IJSValue
  (to-js [x]))

(defn- to-js-map [m]
  (JSValue. (into {} (map (fn [[k v]] [k (to-js v)])) m)))

(extend-protocol IJSValue
  clojure.lang.Keyword
  (to-js [x]
    (if (qualified-keyword? x)
      (throw (ex-info "Cannot use qualified keyword in raw js prop!" {}))
      (name x)))
  clojure.lang.PersistentArrayMap
  (to-js [x]
    (to-js-map x))
  clojure.lang.PersistentHashMap
  (to-js [x]
    (to-js-map x))
  clojure.lang.PersistentVector
  (to-js [x]
    (JSValue. (mapv to-js x)))
  Object
  (to-js [x]
    x)
  nil
  (to-js [_]
    nil))



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
                                 :array-children? false}
                           {} &env))



;; (defmacro defc [sym args & body]
;;   (if (empty? args)
;;     `(defn ~sym [] ~@body)
;;     `(defn ~sym [props#]
;;        (let [~args (.-args props#)]
;;          ~@body))))


(defmacro defc [sym args & body]
  (if (empty? args)
    `(let [ctor# (fn ~sym [] ~@body)]
       (defn ~sym [& args#]
         (react/createElement ctor#)))
    `(let [ctor# (fn ~sym [props#]
                   (let [~args (.-args props#)]
                     ~@body))]
       ;; TODO only emit the right signature?
       (defn ~sym [& args#]
         (react/createElement ctor# (cljs.core/js-obj "args" args#))))))
