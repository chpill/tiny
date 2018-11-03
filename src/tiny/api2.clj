(ns tiny.api2
  (:require [hicada.compiler])
  (:import [cljs.tagged_literals JSValue]))


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


(defn source-file-from-macro-env [amp-env]
  ;; TODO, put this in cljc
  ;; #?(:clj (if (:ns amp-env)
  ;;           (:file amp-env)
  ;;           *file*)
  ;;    :cljs (:file amp-env))

  (if (:ns amp-env)
    (:file amp-env)
    *file*))


(defn get-source-from-macro-env [amp-env]
  (let [{:keys [ns line]} amp-env]
    (JSValue. {:fileName (str (:name ns))
               :lineNumber line})))


(defn t-helper
  ([type config amp-env] `(internal-t ~type
                                      ~config
                                      ~(get-source-from-macro-env amp-env))))

(defmacro t [type & [config]]
  (t-helper type (or config {}) &env))

