(ns tiny.api
  (:require [clojure.spec.alpha :as s]))



;; from rum/core.clj

(defn- fn-body? [form]
  (when (and (seq? form)
             (vector? (first form)))
    (if (= '< (second form))
      (throw (IllegalArgumentException. "Mixins must be given before argument list"))
      true)))



(defn- parse-defc
  ":name  :doc?  <? :mixins* :bodies+
   symbol string <  exprs    fn-body?"
  [xs]
  (when-not (instance? clojure.lang.Symbol (first xs))
    (throw (IllegalArgumentException. "First argument to defc must be a symbol")))
  (loop [res  {}
         xs   xs
         mode nil]
    (let [x    (first xs)
          next (next xs)]
      (cond
        (and (empty? res) (symbol? x))
        (recur {:name x} next nil)
        (fn-body? xs)        (assoc res :bodies (list xs))
        (every? fn-body? xs) (assoc res :bodies xs)
        (string? x)          (recur (assoc res :doc x) next nil)
        (= '< x)             (recur res next :mixins)
        (= mode :mixins)
        (recur (update-in res [:mixins] (fnil conj []) x) next :mixins)
        :else
        (throw (IllegalArgumentException. (str "Syntax error at " xs)))))))


(defmacro defc [name arg-list & bodies]
  `(def ~name
     (tiny.api/build-class (fn ~arg-list
                             ~@bodies))))


(comment (macroexpand-1 '(defc plop [lala]
                           (inc lala)
                           (inc 2)))

         (defc plop :a)

         ;; Does not work... why?
         (s/fdef defc
           {:args (s/cat :name symbol? :arg-list vector? :bodies any?)
            :ret var?}))
